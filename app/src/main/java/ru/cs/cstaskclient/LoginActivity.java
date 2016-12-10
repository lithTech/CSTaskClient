package ru.cs.cstaskclient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResult;
import ru.cs.cstaskclient.dto.GridQueryResultUsers;
import ru.cs.cstaskclient.dto.SessionUser;
import ru.cs.cstaskclient.fragments.SettingsFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.AuthApi;
import ru.cs.cstaskclient.repository.CookieInterceptor;
import ru.cs.cstaskclient.repository.ReceiveCookiesInterceptor;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PREF_AUTH_LOGIN = "pref_auth_login";
    public static final String PREF_AUTH_PASSWORD = "pref_auth_password";
    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    final AuthApi authApi = ApiManager.getAuthApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);
        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.login);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.card_view);
        mProgressView = findViewById(R.id.login_progress);

        SharedPreferences pref = getSharedPreferences(SettingsFragment.PREF_FILE, MODE_PRIVATE);
        mLoginView.setText(pref.getString(PREF_AUTH_LOGIN, ""));
        mPasswordView.setText(pref.getString(PREF_AUTH_PASSWORD, ""));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);
        final Activity thisActivity = this;
        // Store values at the time of the login attempt.
        final String login = mLoginView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Call<String> jsessidCall = authApi.jSessionIdCall();
            jsessidCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    CookieInterceptor.setSessionCookie(ReceiveCookiesInterceptor.cookieToString());
                    Call<String> auth = authApi.auth(login, password);
                    auth.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body().contains("loginError"))
                            {
                                Toast.makeText(thisActivity, R.string.login_error, Toast.LENGTH_LONG).show();
                                showProgress(false);
                                return;
                            }
                            fetchUserInfo(login, new ru.cs.cstaskclient.util.Callback() {
                                @Override
                                public void done(Object o) {
                                    saveAuthData(login, password);
                                    startActivity(new Intent(thisActivity, MainActivity.class));
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(thisActivity, R.string.login_error, Toast.LENGTH_LONG).show();
                            showProgress(false);
                        }
                    });
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(thisActivity, R.string.login_error_network, Toast.LENGTH_LONG).show();
                    showProgress(false);
                }
            });
        }
    }

    private void saveAuthData(String login, String password) {
        SharedPreferences pref = getSharedPreferences(SettingsFragment.PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(PREF_AUTH_LOGIN, login);
        ed.putString(PREF_AUTH_PASSWORD, password);
        ed.commit();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        findViewById(R.id.email_sign_in_button).setVisibility(show?View.GONE:View.VISIBLE);
        findViewById(R.id.login_progress).setVisibility(show?View.VISIBLE:View.GONE);
    }

    public void fetchUserInfo(String login, final ru.cs.cstaskclient.util.Callback onSuccess) {
        Call<GridQueryResultUsers> usersCall = ApiManager.getUserApi()
                .findUsers(GridQueryRequest.getSimple("login", "eq", login));
        final Activity thisActivity = this;
        usersCall.enqueue(new Callback<GridQueryResultUsers>() {
            @Override
            public void onResponse(Call<GridQueryResultUsers> call, Response<GridQueryResultUsers> response) {
                final SessionUser user = response.body().data.get(0);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(ApiManager.API_URL+"/"+user.avatarLink)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        Toast.makeText(thisActivity, R.string.login_error_network, Toast.LENGTH_LONG).show();
                        showProgress(false);
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                        user.avatar = response.body().bytes();
                        SessionUser.CURRENT = user;

                        onSuccess.done(user);
                    }
                });

            }

            @Override
            public void onFailure(Call<GridQueryResultUsers> call, Throwable t) {
                Toast.makeText(thisActivity, R.string.login_error_network, Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

}


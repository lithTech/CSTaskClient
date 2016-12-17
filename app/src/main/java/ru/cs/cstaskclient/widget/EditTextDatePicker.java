package ru.cs.cstaskclient.widget;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {

    EditText editText;
    private int day;
    private int month;
    private int year;
    private Context context;

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText _editText) {
        this.editText = _editText;
    }

    public EditTextDatePicker(Context context, EditText editText) {
        Activity act = (Activity) context;
        this.editText = editText;
        this.editText.setOnClickListener(this);
        this.editText.setOnFocusChangeListener(this);
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.editText.setShowSoftInputOnFocus(false);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDisplay() {
        String daypad = "0";
        if (day > 9)
            daypad = "";
        editText.setText(new StringBuilder()
                .append(daypad).append(day)
                .append(".").append(month + 1)
                .append(".").append(year));
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b)
            onClick(view);
    }
}
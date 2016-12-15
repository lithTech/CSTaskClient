package ru.cs.cstaskclient.widget;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {

    EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;

    public EditText getEditText() {
        return _editText;
    }

    public void setEditText(EditText _editText) {
        this._editText = _editText;
    }

    public EditTextDatePicker(Context context, EditText editText) {
        Activity act = (Activity) context;
        this._editText = editText;
        this._editText.setOnClickListener(this);
        this._editText.setOnFocusChangeListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDisplay() {
        String daypad = "0";
        if (_day > 9)
            daypad = "";
        _editText.setText(new StringBuilder()
                .append(daypad).append(_day)
                .append(".").append(_month + 1)
                .append(".").append(_year));
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b)
            onClick(view);
    }
}
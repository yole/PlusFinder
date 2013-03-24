package ru.yole.plusfinder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author yole
 */
public class NumberPickerComponent extends LinearLayout {
    private EditText myEditText;
    private int myMinValue;

    public NumberPickerComponent(Context context) {
        this(context, null);
    }

    public NumberPickerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.number_picker, this);
        myEditText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.decreaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(Math.max(myMinValue, getValue()-1));
            }
        });
        findViewById(R.id.increaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(getValue()+1);
            }
        });
    }

    public void setMinValue(int minValue) {
        myMinValue = minValue;
    }

    public int getValue() {
        return Integer.valueOf(myEditText.getText().toString());
    }

    public void setValue(int value) {
        myEditText.setText(Integer.toString(value));
    }
}

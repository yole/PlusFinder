package ru.yole.plusfinder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author yole
 */
public class NumberPickerFragment extends Fragment {
    private EditText myEditText;
    private int myValue;
    private int myMinValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.number_picker, container, false);
        myEditText = (EditText) view.findViewById(R.id.editText);
        myEditText.setText(Integer.toString(myValue));
        view.findViewById(R.id.decreaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(Math.max(myMinValue, getValue()-1));
            }
        });
        view.findViewById(R.id.increaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(getValue()+1);
            }
        });
        return view;
    }

    public void setMinValue(int minValue) {
        myMinValue = minValue;
    }

    public int getValue() {
        return Integer.valueOf(myEditText.getText().toString());
    }

    public void setValue(int value) {
        if (myEditText != null) {
            myEditText.setText(Integer.toString(value));
        }
        else {
            myValue = value;
        }
    }
}

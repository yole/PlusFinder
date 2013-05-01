package ru.yole.plusfinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import ru.yole.plusfinder.model.PlayerCharacter;
import ru.yole.plusfinder.model.StatNames;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yole
 */
public class EditCharacterActivity extends Activity {
    private PlayerCharacter myCharacter;
    private final Map<String, NumberPickerComponent> myPickers = new HashMap<String, NumberPickerComponent>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_character);
        GridLayout grid = (GridLayout) findViewById(R.id.characterGrid);
        int row = 1;

        for (String s : StatNames.STAT_NAMES) {
            TextView label = new TextView(this);
            label.setText(s);
            label.setTextSize(TypedValue.COMPLEX_UNIT_PT, 14);
            label.setGravity(Gravity.CENTER_VERTICAL);
            grid.addView(label, new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(0)));

            NumberPickerComponent picker = new NumberPickerComponent(this);
            picker.setValue(myCharacter == null ? PlayerCharacter.getStatDefault(s) : myCharacter.getStat(s));
            picker.setMinValue(PlayerCharacter.getStatMinValue(s));
            grid.addView(picker, new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(1)));

            myPickers.put(s, picker);
            row++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                saveCharacter();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCharacter() {
        if (myCharacter == null) {
            myCharacter = new PlayerCharacter();
        }
        updateCharacter(myCharacter);
        new DatabaseHelper(this).saveCharacter(myCharacter);
    }

    private void updateCharacter(PlayerCharacter character) {
        EditText nameView = (EditText) findViewById(R.id.name);
        character.setName(nameView.getText().toString());
        for (Map.Entry<String, NumberPickerComponent> entry : myPickers.entrySet()) {
            character.setStat(entry.getKey(), entry.getValue().getValue());
        }
    }
}
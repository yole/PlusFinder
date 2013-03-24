package ru.yole.plusfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ru.yole.plusfinder.model.PlayerCharacter;

/**
 * @author yole
 */
public class EditCharacterActivity extends Activity {
    private PlayerCharacter myCharacter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_character);
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
    }
}
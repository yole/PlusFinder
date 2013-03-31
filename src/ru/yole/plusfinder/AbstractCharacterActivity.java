package ru.yole.plusfinder;

import android.app.Activity;
import android.os.Bundle;
import ru.yole.plusfinder.model.PlayerCharacter;

/**
 * @author yole
 */
public abstract class AbstractCharacterActivity extends Activity {
    protected DatabaseHelper myDatabaseHelper;
    protected PlayerCharacter myCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabaseHelper = new DatabaseHelper(this);
        long characterId = getIntent().getLongExtra(CharacterListActivity.CHARACTER_ID_EXTRA, -1);
        if (characterId == -1) {
            finish();
        }
        myCharacter = myDatabaseHelper.loadCharacter(characterId);
        if (myCharacter == null) {
            finish();
        }
    }
}

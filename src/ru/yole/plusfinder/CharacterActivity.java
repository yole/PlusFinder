package ru.yole.plusfinder;

import android.app.Activity;
import android.os.Bundle;
import ru.yole.plusfinder.model.PlayerCharacter;

/**
 * @author yole
 */
public class CharacterActivity extends Activity {
    private DatabaseHelper myDatabaseHelper;
    private PlayerCharacter myCharacter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_activity);
        long characterId = getIntent().getLongExtra(CharacterListActivity.CHARACTER_ID_EXTRA, -1);
        if (characterId == -1) {
            finish();
        }
        myDatabaseHelper = new DatabaseHelper(this);
        myCharacter = myDatabaseHelper.loadCharacter(characterId);
        if (myCharacter == null) {
            finish();
        }
        setTitle(myCharacter.getName());
    }
}

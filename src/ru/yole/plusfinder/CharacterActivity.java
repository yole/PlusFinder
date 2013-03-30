package ru.yole.plusfinder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import ru.yole.plusfinder.model.PlayerCharacter;
import ru.yole.plusfinder.model.Weapon;

/**
 * @author yole
 */
public class CharacterActivity extends Activity {
    private DatabaseHelper myDatabaseHelper;
    private PlayerCharacter myCharacter;
    private Spinner myWeaponSpinner;
    private TextView myAttackText;
    private TextView myDamageText;

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
        myWeaponSpinner = (Spinner) findViewById(R.id.weaponSpinner);
        ArrayAdapter<Weapon> adapter = new ArrayAdapter<Weapon>(this, R.layout.weapon_view, myCharacter.getWeapons());
        myWeaponSpinner.setAdapter(adapter);
        myAttackText = (TextView) findViewById(R.id.attackText);
        myAttackText.setText("Attack: " + myCharacter.getAttackText());
        myDamageText = (TextView) findViewById(R.id.damageText);
        myDamageText.setText("Damage: " + myCharacter.getDamageText());
    }
}

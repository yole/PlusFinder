package ru.yole.plusfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;
import ru.yole.plusfinder.model.ActiveCondition;
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
    private TextView myACText;

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
        myDamageText = (TextView) findViewById(R.id.damageText);
        myACText = (TextView) findViewById(R.id.acText);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.characterActivity);
        for (final ActiveCondition activeCondition : myCharacter.getActiveConditions()) {
            Switch conditionSwitch = new Switch(this);
            conditionSwitch.setText(activeCondition.getCondition().getName());
            conditionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    activeCondition.setActive(!activeCondition.isActive());
                    updateValues();
                }
            });
            linearLayout.addView(conditionSwitch,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        updateValues();
    }

    private void updateValues() {
        myAttackText.setText("Attack: " + myCharacter.getAttackText());
        myDamageText.setText("Damage: " + myCharacter.getDamageText());
        myACText.setText("AC: " + myCharacter.getACText());
    }
}

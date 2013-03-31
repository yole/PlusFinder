package ru.yole.plusfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;
import ru.yole.plusfinder.model.ActiveCondition;
import ru.yole.plusfinder.model.Weapon;

/**
 * @author yole
 */
public class CharacterActivity extends AbstractCharacterActivity {
    private Spinner myWeaponSpinner;
    private TextView myAttackText;
    private TextView myDamageText;
    private TextView myACText;
    private TextView mySavesText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_activity);
        setTitle(myCharacter.getName());
        myWeaponSpinner = (Spinner) findViewById(R.id.weaponSpinner);
        ArrayAdapter<Weapon> adapter = new ArrayAdapter<Weapon>(this, R.layout.weapon_view, myCharacter.getWeapons());
        myWeaponSpinner.setAdapter(adapter);
        myAttackText = (TextView) findViewById(R.id.attackText);
        myDamageText = (TextView) findViewById(R.id.damageText);
        myACText = (TextView) findViewById(R.id.acText);
        mySavesText = (TextView) findViewById(R.id.saveText);

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
        mySavesText.setText("Fort: " + myCharacter.getFortSaveText() +
                "; Ref: " + myCharacter.getRefSaveText() +
                "; Will "+ myCharacter.getWillSaveText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_inventory:
                Intent intent = new Intent(this, InventoryActivity.class);
                intent.putExtra(CharacterListActivity.CHARACTER_ID_EXTRA, myCharacter.getId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

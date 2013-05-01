package ru.yole.plusfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import ru.yole.plusfinder.model.ActiveCondition;
import ru.yole.plusfinder.model.Condition;
import ru.yole.plusfinder.model.Weapon;

import java.util.Iterator;
import java.util.List;

/**
 * @author yole
 */
public class CharacterActivity extends AbstractCharacterActivity {
    private TextView myAttackText;
    private TextView myACText;
    private TextView mySavesText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_activity);
        setTitle(myCharacter.getName());
        Spinner weaponSpinner = (Spinner) findViewById(R.id.weaponSpinner);
        weaponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Weapon weapon = myCharacter.getWeapons().get(position);
                myCharacter.setActiveWeapon(weapon);
                myDatabaseHelper.setActiveWeapon(myCharacter, weapon);
                updateValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<Weapon> adapter = new ArrayAdapter<Weapon>(this, R.layout.weapon_view, myCharacter.getWeapons());
        weaponSpinner.setAdapter(adapter);
        myAttackText = (TextView) findViewById(R.id.attackText);
        myACText = (TextView) findViewById(R.id.acText);
        mySavesText = (TextView) findViewById(R.id.saveText);

        for (final ActiveCondition activeCondition : myCharacter.getActiveConditions()) {
            addConditionSwitch(activeCondition);
        }

        updateValues();
    }

    private void addConditionSwitch(final ActiveCondition activeCondition) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.characterActivity);
        Switch conditionSwitch = new Switch(this);
        conditionSwitch.setText(activeCondition.getCondition().getName());
        conditionSwitch.setChecked(activeCondition.isActive());
        conditionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activeCondition.setActive(isChecked);
                myDatabaseHelper.setConditionActive(myCharacter, activeCondition.getCondition(), isChecked);
                updateValues();
            }
        });
        linearLayout.addView(conditionSwitch,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void updateValues() {
        myAttackText.setText("Attack: " + myCharacter.getAttackText() + ", Dmg " + myCharacter.getDamageText() +
           ", Crit "+ myCharacter.getCritText());
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

    public void onAddCondition(View view) {
        List<Condition> conditions = myDatabaseHelper.loadAllConditions();
        for (Iterator<Condition> it = conditions.iterator(); it.hasNext(); ) {
            Condition condition = it.next();
            if (myCharacter.isConditionAvailable(condition)) {
                it.remove();
            }
        }
        new PickerDialog<Condition>(conditions, "Choose Condition", new SelectionListener<Condition>() {
            @Override
            public void onSelected(Condition condition) {
                ActiveCondition activeCondition = myCharacter.addActiveCondition(condition, true);
                myDatabaseHelper.addCharacterCondition(myCharacter, condition, true);
                addConditionSwitch(activeCondition);
            }
        }).show(getFragmentManager(), "addCondition");
    }
}

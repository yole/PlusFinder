package ru.yole.plusfinder;

import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ru.yole.plusfinder.model.BaseEntity;
import ru.yole.plusfinder.model.Item;
import ru.yole.plusfinder.model.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yole
 */
public class InventoryActivity extends AbstractCharacterActivity {
    private ListView myInventoryList;
    private ArrayAdapter<BaseEntity> myAdapter;
    private List<BaseEntity> myContents = new ArrayList<BaseEntity>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);
        setTitle("Inventory of " + myCharacter.getName());
        myInventoryList = (ListView) findViewById(R.id.inventoryList);
        myAdapter = new ArrayAdapter<BaseEntity>(this, R.layout.inventory_item, myContents) {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return !(myContents.get(position) instanceof SeparatorEntity);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                BaseEntity baseEntity = myContents.get(position);
                if (baseEntity instanceof SeparatorEntity) {
                    TextView result;
                    if (convertView == null) {
                        result = (TextView) getLayoutInflater().inflate(R.layout.inventory_separator, null);
                    }
                    else {
                        result = (TextView) convertView;
                    }
                    result.setText(baseEntity.toString());
                    return result;
                }
                return super.getView(position, convertView, parent);
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                return myContents.get(position) instanceof SeparatorEntity ? 1 : 0;
            }
        };
        refreshAdapter();
        myInventoryList.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_item:
            {
                PickerDialog<Item> dialog = new PickerDialog<Item>(getAvailableItems(), getString(R.string.selectItem),
                        new SelectionListener<Item>() {
                            @Override
                            public void onSelected(Item entity) {
                                addItem(entity);
                            }
                        });
                dialog.show(getFragmentManager(), "addItem");
                return true;
            }

            case R.id.menu_add_weapon:
            {
                PickerDialog<Weapon> dialog = new PickerDialog<Weapon>(getAvailableWeapons(), getString(R.string.selectWeapon),
                        new SelectionListener<Weapon>() {
                            @Override
                            public void onSelected(Weapon entity) {
                                addWeapon(entity);
                            }
                        });
                dialog.show(getFragmentManager(), "addWeapon");
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Item> getAvailableItems() {
        final List<Item> items = myDatabaseHelper.loadAllItems();
        return minus(items, getCharacter().getInventory());
    }

    private List<Weapon> getAvailableWeapons() {
        final List<Weapon> items = myDatabaseHelper.loadAllWeapons();
        return minus(items, getCharacter().getWeapons());
    }

    private <T> List<T> minus(List<T> items, List<T> inventory) {
        for (Iterator<T> iterator = items.iterator(); iterator.hasNext(); ) {
            T item = iterator.next();
            if (inventory.contains(item)) {
                iterator.remove();
            }
        }
        return items;
    }

    public void addItem(Item item) {
        myCharacter.addItem(item);
        refreshAdapter();
        myDatabaseHelper.addCharacterItem(myCharacter, item);
    }

    private void addWeapon(Weapon weapon) {
        myCharacter.addWeapon(weapon);
        refreshAdapter();
        myDatabaseHelper.addCharacterWeapon(myCharacter, weapon);
    }

    private void refreshAdapter() {
        myContents.clear();
        myContents.add(new SeparatorEntity("Weapons"));
        myContents.addAll(myCharacter.getWeapons());
        myContents.add(new SeparatorEntity("Items"));
        myContents.addAll(myCharacter.getInventory());
        myAdapter.notifyDataSetChanged();
    }

    private static class SeparatorEntity extends BaseEntity {
        public SeparatorEntity(String name) {
            setName(name);
        }
    }
}
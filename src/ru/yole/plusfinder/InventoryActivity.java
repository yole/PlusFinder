package ru.yole.plusfinder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.yole.plusfinder.model.Item;

/**
 * @author yole
 */
public class InventoryActivity extends AbstractCharacterActivity {
    private ListView myInventoryList;
    private ArrayAdapter<Item> myAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);
        setTitle("Inventory of " + myCharacter.getName());
        myInventoryList = (ListView) findViewById(R.id.inventoryList);
        myAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_item, myCharacter.getInventory());
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
                new ItemPickerDialog().show(getFragmentManager(), "addItem");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItem(Item item) {
        myCharacter.addItem(item);
        myAdapter.notifyDataSetChanged();
        myDatabaseHelper.addCharacterItem(myCharacter, item);
    }
}
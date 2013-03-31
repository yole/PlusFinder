package ru.yole.plusfinder;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.yole.plusfinder.model.Item;

/**
 * @author yole
 */
public class InventoryActivity extends AbstractCharacterActivity {
    private ListView myInventoryList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);
        setTitle("Inventory of " + myCharacter.getName());
        myInventoryList = (ListView) findViewById(R.id.inventoryList);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.inventory_item, myCharacter.getInventory());
        myInventoryList.setAdapter(adapter);
    }
}
package ru.yole.plusfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import ru.yole.plusfinder.model.Item;

import java.util.*;

/**
 * @author yole
 */
public class ItemPickerDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final List<Item> items = databaseHelper.loadAllItems();
        filterExistingItems(items);
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getName();

        }
        builder.setTitle(getActivity().getString(R.string.selectItem))
                .setItems(itemNames,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (getActivity() instanceof InventoryActivity) {
                                    ((InventoryActivity) getActivity()).addItem(items.get(which));
                                }
                            }
                        });
        return builder.create();
    }

    private void filterExistingItems(List<Item> items) {
        if (getActivity() instanceof InventoryActivity) {
            InventoryActivity inventoryActivity = (InventoryActivity) getActivity();
            for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
                Item item = iterator.next();
                if (inventoryActivity.getCharacter().getInventory().contains(item)) {
                    iterator.remove();
                }
            }
        }
    }
}

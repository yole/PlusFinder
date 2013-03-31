package ru.yole.plusfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import ru.yole.plusfinder.model.BaseEntity;

import java.util.List;

/**
 * @author yole
 */
public class PickerDialog<T extends BaseEntity> extends DialogFragment {
    private List<T> myEntities;
    private SelectionListener<T> mySelectionListener;
    private String myDialogTitle;

    public PickerDialog(List<T> entities, String dialogTitle, SelectionListener<T> selectionListener) {
        myEntities = entities;
        mySelectionListener = selectionListener;
        myDialogTitle = dialogTitle;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] itemNames = new String[myEntities.size()];
        for (int i = 0; i < myEntities.size(); i++) {
            itemNames[i] = myEntities.get(i).getName();
        }
        builder.setTitle(myDialogTitle)
                .setItems(itemNames,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySelectionListener.onSelected(myEntities.get(which));
                            }
                        });
        return builder.create();
    }
}

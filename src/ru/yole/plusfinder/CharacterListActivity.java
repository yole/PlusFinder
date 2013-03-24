package ru.yole.plusfinder;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class CharacterListActivity extends ListActivity {
    private DatabaseHelper myDatabaseHelper;
    private MyLoaderCallbacks myLoaderCallbacks = new MyLoaderCallbacks();
    private SimpleCursorAdapter myCursorAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myDatabaseHelper = new DatabaseHelper(this);
        myCursorAdapter = new SimpleCursorAdapter(this, R.layout.character, null,
                new String[] { "name" }, new int[] { R.id.name }, 0);
        setListAdapter(myCursorAdapter);
        getLoaderManager().initLoader(0, null, myLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.character_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, myLoaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_character:
                Intent intent = new Intent(this, EditCharacterActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return myDatabaseHelper.getAllCharactersLoader(CharacterListActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            myCursorAdapter.changeCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            myCursorAdapter.changeCursor(null);
        }
    }
}

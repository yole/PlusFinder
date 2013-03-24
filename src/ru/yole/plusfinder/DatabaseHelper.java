package ru.yole.plusfinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import ru.yole.plusfinder.model.PlayerCharacter;

/**
 * @author yole
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "plusfinder.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CHARACTERS = "Characters";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder createStatement = new StringBuilder("create table Characters(" +
                "_id integer primary key autoincrement, name text not null");
        for (String statName : PlayerCharacter.STAT_NAMES) {
            createStatement.append(",").append(statName).append(" integer");
        }
        createStatement.append(")");
        db.execSQL(createStatement.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Characters");
        onCreate(db);
    }

    public Loader<Cursor> getAllCharactersLoader(Context context) {
        return new SQLiteCursorLoader(context, this, "select _id, name from " + TABLE_CHARACTERS,
                new String[0]);
    }

    public void saveCharacter(PlayerCharacter character) {
        ContentValues contentValues = toContentValues(character);
        if (character.getId() == -1) {
            long id = getWritableDatabase().insert(TABLE_CHARACTERS, null, contentValues);
            character.setId(id);
        }
        else {
            getWritableDatabase().update(TABLE_CHARACTERS, contentValues, "_id=?",
                    new String[] { String.valueOf(character.getId()) });
        }
    }

    private ContentValues toContentValues(PlayerCharacter character) {
        ContentValues values = new ContentValues();
        values.put("name", character.getName());
        for (String statName : PlayerCharacter.STAT_NAMES) {
            values.put(statName, character.getStat(statName));
        }
        return values;
    }
}

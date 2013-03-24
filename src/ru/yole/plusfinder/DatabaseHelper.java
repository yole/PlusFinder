package ru.yole.plusfinder;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

/**
 * @author yole
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "plusfinder.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CHARACTERS = "Characters";

    private static final String DATABASE_CREATE = "create table " + TABLE_CHARACTERS +
            "(_id integer primary key autoincrement, " +
            "name text not null," +
            "str integer," +
            "dex integer," +
            "con integer," +
            "int integer," +
            "wis integer," +
            "cha integer," +
            "fort integer," +
            "ref integer," +
            "will integer)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Loader<Cursor> getAllCharactersLoader(Context context) {
        return new SQLiteCursorLoader(context, this, "select _id, name from " + TABLE_CHARACTERS,
                new String[0]);
    }
}

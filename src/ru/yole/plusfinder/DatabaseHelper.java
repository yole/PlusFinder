package ru.yole.plusfinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yole.plusfinder.model.PlayerCharacter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * @author yole
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "plusfinder.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_CHARACTERS = "Characters";
    private static final String TABLE_WEAPONS = "Weapons";

    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
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

        db.execSQL("create table Weapons(_id integer primary key autoincrement, name text not null," +
                "damageDiceCount int default '1', damageDiceSize int, attackModifier int default '0', " +
                "damageModifier int default '0', critRange int default '20', critModifier int default '2')");

        loadJsonToTable(db, "weapons.json", TABLE_WEAPONS);
    }

    private void loadJsonToTable(SQLiteDatabase db, String assetFileName, String tableName) {
        try {
            JSONArray jsonArray = loadJsonFromAssets(assetFileName);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                Iterator it = jsonObject.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object value = jsonObject.get(key);
                    if (value instanceof String) {
                        contentValues.put(key, (String) value);
                    }
                    else if (value instanceof Integer) {
                        contentValues.put(key, (Integer) value);
                    }
                    else {
                        throw new RuntimeException("Unrecognized object type in JSON: " + value);
                    }
                }
                db.insert(tableName, null, contentValues);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray loadJsonFromAssets(String assetFileName) throws IOException, JSONException {
        InputStream weaponsStream = myContext.getAssets().open(assetFileName);
        StringBuilder weaponsJsonText = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(weaponsStream));
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            weaponsJsonText.append(line);
        }
        return new JSONArray(weaponsJsonText.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Characters");
        if (oldVersion >= 3) {
            db.execSQL("drop table Weapons");
        }
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

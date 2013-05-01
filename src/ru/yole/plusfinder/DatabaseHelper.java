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
import ru.yole.plusfinder.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author yole
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "plusfinder.db";
    private static final int DATABASE_VERSION = 15;

    private static final String TABLE_CHARACTERS = "Characters";
    private static final String TABLE_WEAPONS = "Weapons";
    private static final String TABLE_CHARACTER_WEAPONS = "CharacterWeapons";
    private static final String TABLE_CONDITIONS = "Conditions";
    private static final String TABLE_CHARACTER_CONDITIONS = "CharacterConditions";
    private static final String TABLE_ITEMS = "Items";
    private static final String TABLE_CHARACTER_ITEMS = "CharacterItems";

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
                "damageModifier int default '0', critRange int default '20', critModifier int default '2'," +
                "isMissile int default '0')");
        db.execSQL("create table CharacterWeapons(_id integer primary key autoincrement, characterId integer, " +
                "weaponId integer, active integer)");
        db.execSQL("create table CharacterConditions(_id integer primary key autoincrement, characterId integer, " +
                "conditionId integer, active integer)");
        createTableFromBean(db, TABLE_CONDITIONS, Condition.class, new Condition());
        createTableFromBean(db, TABLE_ITEMS, Item.class, new Item());
        db.execSQL("create table " + TABLE_CHARACTER_ITEMS + "(_id integer primary key autoincrement, characterId integer," +
                "itemId integer)");

        loadJsonToTable(db, "weapons.json", TABLE_WEAPONS);
        loadJsonToTable(db, "conditions.json", TABLE_CONDITIONS);
        loadJsonToTable(db, "items.json", TABLE_ITEMS);
    }

    private void createTableFromBean(SQLiteDatabase db, String tableName, Class entityClass, Object defaultValueSource) {
        StringBuilder builder = new StringBuilder("create table ");
        builder.append(tableName).append("(_id integer primary key autoincrement, name text not null");
        for (Method method : entityClass.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("set")) {
                String fieldName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                int defaultValue = 0;
                try {
                    Method getMethod = entityClass.getMethod("g" + name.substring(1));
                    defaultValue = (Integer) getMethod.invoke(defaultValueSource);
                } catch (NoSuchMethodException ignored) {
                } catch (InvocationTargetException ignored) {
                } catch (IllegalAccessException ignored) {
                }
                builder.append(",").append(fieldName).append(" int default '").append(defaultValue).append("'");
            }
        }
        builder.append(")");
        db.execSQL(builder.toString());
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
                    else if (value instanceof Boolean) {
                        contentValues.put(key, ((Boolean) value) ? 1 : 0);
                    } else {
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
        List<String> allTables = Arrays.asList(TABLE_CHARACTERS,
                TABLE_WEAPONS, TABLE_CHARACTER_WEAPONS,
                TABLE_CONDITIONS, TABLE_CHARACTER_CONDITIONS,
                TABLE_ITEMS, TABLE_CHARACTER_ITEMS);
        for(String tableName: allTables) {
            db.execSQL("drop table if exists " + tableName);
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

    public PlayerCharacter loadCharacter(long characterId) {
        Cursor query = getReadableDatabase().query(TABLE_CHARACTERS, null, "_id=?", new String[]{Long.toString(characterId)},
                null, null, null);
        if (!query.moveToFirst()) {
            return null;
        }
        PlayerCharacter pc = new PlayerCharacter();
        String[] columnNames = query.getColumnNames();
        for (int i = 0; i < columnNames.length; i++) {
            String column = columnNames[i];
            if (column.equals("name")) {
                pc.setName(query.getString(i));
            }
            else if (column.equals("_id")) {
                pc.setId(query.getLong(i));
            }
            else {
                pc.setStat(column, query.getInt(i));
            }
        }
        loadCharacterConditions(pc);
        loadCharacterItems(pc);
        loadCharacterWeapons(pc);
        return pc;
    }

    private void loadCharacterConditions(PlayerCharacter pc) {
        String query = "select cc.active, c.* from Conditions c inner join CharacterConditions cc on c._id=cc.conditionId where cc.characterId=?";
        Cursor c = getReadableDatabase().rawQuery(query, new String[] { Long.toString(pc.getId())} );
        if (c.moveToFirst()) {
            do {
                boolean active = c.getInt(0) != 0;
                Condition condition = new Condition();
                loadEntityFromCursor(c, condition);
                pc.addActiveCondition(condition, active);
            } while(c.moveToNext());
        }
    }

    private void loadCharacterItems(PlayerCharacter pc) {
        String query = "select i.* from Items i inner join CharacterItems ci on i._id=ci.itemId where ci.characterId=?";
        Cursor c = getReadableDatabase().rawQuery(query, new String[] { Long.toString(pc.getId())} );
        pc.setInventory(loadEntitiesFromCursor(Item.class, c));
    }

    private void loadCharacterWeapons(PlayerCharacter pc) {
        String query = "select cw.active, w.* from Weapons w inner join CharacterWeapons cw on w._id=cw.weaponId where cw.characterId=?";
        Cursor c = getReadableDatabase().rawQuery(query, new String[] { Long.toString(pc.getId())} );
        Weapon activeWeapon = null;
        List<Weapon> weapons = new ArrayList<Weapon>();
        if (c.moveToFirst()) {
            do {
                Weapon weapon = new Weapon();
                loadEntityFromCursor(c, weapon);
                weapons.add(weapon);
                if (c.getInt(0) != 0) {
                    activeWeapon = weapon;
                }
            } while(c.moveToNext());
        }
        Weapon unarmedStrike = loadUnarmedStrike();
        weapons.add(unarmedStrike);
        pc.setWeapons(weapons);
        if (activeWeapon != null) {
            pc.setActiveWeapon(activeWeapon);
        }
        else {
            pc.setActiveWeapon(unarmedStrike);
        }
    }

    private Weapon loadUnarmedStrike() {
        Weapon weapon = new Weapon();
        if (!loadEntityByName(weapon, TABLE_WEAPONS, "Unarmed Strike")) return null;
        return weapon;
    }

    private <T extends BaseEntity> boolean loadEntityByName(T entity, String tableName, String entityName) {
        Cursor query = getReadableDatabase().query(tableName, null, "name=?", new String[] { entityName },
                null, null, null);
        if (!query.moveToFirst()) {
            return false;
        }
        loadEntityFromCursor(query, entity);
        return true;
    }

    public List<Condition> loadAllConditions() {
        return loadAllEntities(Condition.class, TABLE_CONDITIONS, null);
    }

    public List<Item> loadAllItems() {
        return loadAllEntities(Item.class, TABLE_ITEMS, "name");
    }

    public List<Weapon> loadAllWeapons() {
        return loadAllEntities(Weapon.class, TABLE_WEAPONS, "name");
    }

    private <T extends BaseEntity> List<T> loadAllEntities(Class<T> entityClass, String tableName, String orderBy) {
        Cursor query = getReadableDatabase().query(tableName, null, null, null, null, null, orderBy);
        return loadEntitiesFromCursor(entityClass, query);
    }

    private <T extends BaseEntity> List<T> loadEntitiesFromCursor(Class<T> entityClass, Cursor query) {
        List<T> result = new ArrayList<T>();
        if (query.moveToFirst()) {
            do {
                T entity;
                try {
                    entity = entityClass.newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                loadEntityFromCursor(query, entity);
                result.add(entity);
            } while(query.moveToNext());
        }
        return result;
    }

    private <T extends BaseEntity> void loadEntityFromCursor(Cursor query, T entity) {
        String[] columnNames = query.getColumnNames();
        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            if (columnName.equals("name")) {
                entity.setName(query.getString(i));
            }
            else if (columnName.equals("_id")) {
                entity.setId(query.getLong(i));
            } else {
                String setterName = "set" + Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1);
                try {
                    Method setter = entity.getClass().getDeclaredMethod(setterName, int.class);
                    setter.invoke(entity, query.getInt(i));

                } catch (NoSuchMethodException ignored) {
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void addCharacterItem(PlayerCharacter character, Item item) {
        ContentValues values = new ContentValues();
        values.put("characterId", character.getId());
        values.put("itemId", item.getId());
        getWritableDatabase().insert(TABLE_CHARACTER_ITEMS, null, values);
    }

    public void addCharacterWeapon(PlayerCharacter character, Weapon weapon) {
        ContentValues values = new ContentValues();
        values.put("characterId", character.getId());
        values.put("weaponId", weapon.getId());
        values.put("active", 0);
        getWritableDatabase().insert(TABLE_CHARACTER_WEAPONS, null, values);
    }

    public void addCharacterCondition(PlayerCharacter character, Condition condition, boolean active) {
        ContentValues values = new ContentValues();
        values.put("characterId", character.getId());
        values.put("conditionId", condition.getId());
        values.put("active", active ? 1 : 0);
        getWritableDatabase().insert(TABLE_CHARACTER_CONDITIONS, null, values);
    }

    public void setConditionActive(PlayerCharacter character, Condition condition, boolean active) {
        ContentValues values = new ContentValues();
        values.put("active", active ? 1 : 0);
        getWritableDatabase().update(TABLE_CHARACTER_CONDITIONS, values, "characterId=? and conditionId=?",
                new String[] { String.valueOf(character.getId()), String.valueOf(condition.getId()) });
    }

    public void setActiveWeapon(PlayerCharacter character, Weapon weapon) {
        ContentValues values = new ContentValues();
        values.put("active", 1);
        getWritableDatabase().update(TABLE_CHARACTER_WEAPONS, values, "characterId=? and weaponId=?",
                new String[] { String.valueOf(character.getId()), String.valueOf(weapon.getId())});
        values.put("active", 0);
        getWritableDatabase().update(TABLE_CHARACTER_WEAPONS, values, "characterId=? and weaponId<>?",
                new String[] { String.valueOf(character.getId()), String.valueOf(weapon.getId())});
    }
}

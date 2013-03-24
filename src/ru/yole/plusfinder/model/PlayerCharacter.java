package ru.yole.plusfinder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yole
 */
public class PlayerCharacter {
    private long myId = -1;
    private String myName;
    private final Map<String, Integer> myStats = new HashMap<String, Integer>();

    public static final String[] STAT_NAMES = { "Str", "Dex", "Con", "Int", "Wis", "Cha", "BAB", "Fort", "Ref", "Will" };

    public long getId() {
        return myId;
    }

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    public void setId(long id) {
        myId = id;
    }

    public int getStat(String name) {
        Integer stat = myStats.get(name);
        return stat == null ? 0 : stat;
    }

    public void setStat(String name, int value) {
        myStats.put(name, value);
    }

    public static int getStatMinValue(String statName) {
        return isAbility(statName) ? 3 : 0;
    }

    private static boolean isAbility(String statName) {
        for (int i = 0; i < 6; i++) {
            if (statName.equals(STAT_NAMES[i])) {
                return true;
            }
        }
        return false;
    }

    public static int getStatDefault(String statName) {
        return isAbility(statName) ? 10 : 0;
    }
}

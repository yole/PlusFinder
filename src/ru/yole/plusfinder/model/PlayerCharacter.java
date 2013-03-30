package ru.yole.plusfinder.model;

import java.util.*;

/**
 * @author yole
 */
public class PlayerCharacter {
    private long myId = -1;
    private String myName;
    private final Map<String, Integer> myStats = new HashMap<String, Integer>();
    private final List<Weapon> myWeapons = new ArrayList<Weapon>();
    private Weapon myActiveWeapon;

    public static final String STAT_STR = "Str";
    public static final String STAT_DEX = "Dex";

    public static final String STAT_BAB = "BAB";

    public static final String[] STAT_NAMES = {STAT_STR, STAT_DEX, "Con", "Int", "Wis", "Cha", STAT_BAB, "Fort", "Ref", "Will" };

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

    public Weapon getActiveWeapon() {
        return myActiveWeapon;
    }

    public void setActiveWeapon(Weapon activeWeapon) {
        myActiveWeapon = activeWeapon;
    }

    public List<Weapon> getWeapons() {
        return myWeapons;
    }

    public void setWeapons(Collection<Weapon> weapons) {
        myWeapons.clear();
        myWeapons.addAll(weapons);
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

    public String getAttackText() {
        int attackBonus = getStat(STAT_BAB);
        StringBuilder result = new StringBuilder();
        do {
            if (result.length() > 0) {
                result.append("/");
            }
            int attackBonusModifier = myActiveWeapon.isMissile() ? getBonus(STAT_DEX) : getBonus(STAT_STR);
            result.append("+");
            result.append(attackBonus + attackBonusModifier);
            attackBonus -= 5;
        } while(attackBonus > 0);
        return result.toString();
    }

    public int getBonus(String statStr) {
        return (getStat(statStr) - 10) / 2;
    }

    public String getDamageText() {
        if (myActiveWeapon == null) {
            return "-";
        }
        return myActiveWeapon.getDamageDiceCount() + "d" + myActiveWeapon.getDamageDiceSize();
    }
}

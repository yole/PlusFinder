package ru.yole.plusfinder.model;

import java.util.*;

/**
 * @author yole
 */
public class PlayerCharacter extends BaseEntity {
    private final Map<String, Integer> myStats = new HashMap<String, Integer>();
    private final List<Weapon> myWeapons = new ArrayList<Weapon>();
    private Weapon myActiveWeapon;
    private final List<ActiveCondition> myActiveConditions = new ArrayList<ActiveCondition>();
    private final List<Item> myInventory = new ArrayList<Item>();

    public static final String STAT_STR = "Str";
    public static final String STAT_DEX = "Dex";
    public static final String STAT_CON = "Con";
    public static final String STAT_WIS = "Wis";

    public static final String STAT_BAB = "BAB";

    public static final String STAT_FORT = "Fort";
    public static final String STAT_REF = "Ref";
    public static final String STAT_WILL = "Will";

    public static final String[] STAT_NAMES = {
            STAT_STR, STAT_DEX, STAT_CON, "Int", STAT_WIS, "Cha",
            STAT_BAB,
            STAT_FORT, STAT_REF, STAT_WILL
    };

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

    public void addWeapon(Weapon weapon) {
        myWeapons.add(weapon);
    }

    public void addActiveCondition(Condition condition) {
        myActiveConditions.add(new ActiveCondition(condition));
    }

    public List<ActiveCondition> getActiveConditions() {
        return myActiveConditions;
    }

    public void addItem(Item item) {
        myInventory.add(item);
    }

    private List<Condition> getCurrentConditions() {
        List<Condition> result = new ArrayList<Condition>();
        for (ActiveCondition condition : myActiveConditions) {
            if (condition.isActive()) {
                Condition c = condition.getCondition();
                if (c.missileOnly() && !myActiveWeapon.isMissile()) {
                    continue;
                }
                if (c.meleeOnly() && myActiveWeapon.isMissile()) {
                    continue;
                }
                result.add(c);
            }
        }
        return result;
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
        int extraAttacks = 0;
        for (Condition condition : getCurrentConditions()) {
            extraAttacks += condition.getExtraAttacks();
        }
        StringBuilder result = new StringBuilder();
        do {
            if (result.length() > 0) {
                result.append("/");
            }
            int attackBonusModifier = myActiveWeapon.isMissile() ? getBonus(STAT_DEX) : getBonus(STAT_STR);
            for (Condition condition : getCurrentConditions()) {
                attackBonusModifier += multiplyByBab(condition, condition.getAttackBonus());
            }
            result.append("+");
            result.append(attackBonus + attackBonusModifier);
            if (extraAttacks > 0) {
                extraAttacks--;
            }
            else {
                attackBonus -= 5;
            }
        } while(attackBonus > 0);
        return result.toString();
    }

    private int multiplyByBab(Condition condition, int bonus) {
        int multiplier = condition.getBabMultiplier();
        if (multiplier > 0) {
            return (getStat(STAT_BAB) / multiplier + 1) * bonus;
        }
        return bonus;
    }

    public int getBonus(String statStr) {
        return (getEffectiveStat(statStr) - 10) / 2;
    }

    public int getEffectiveStat(String statName) {
        int stat = getStat(statName);
        for (Condition condition : getCurrentConditions()) {
            if (statName.equals(STAT_STR)) {
                stat += condition.getStrBonus();
            }
            else if (statName.equals(STAT_DEX)) {
                stat += condition.getDexBonus();
            }
        }
        return stat;
    }

    public String getDamageText() {
        if (myActiveWeapon == null) {
            return "-";
        }
        String baseDamage = myActiveWeapon.getDamageDiceCount() + "d" + myActiveWeapon.getDamageDiceSize();
        int damageBonus = myActiveWeapon.getDamageModifier();
        for (Condition condition : getCurrentConditions()) {
            damageBonus += multiplyByBab(condition, condition.getDamageBonus());
        }
        if (damageBonus > 0) {
            return baseDamage + "+" + damageBonus;
        }
        if (damageBonus < 0) {
            return baseDamage + Integer.toString(damageBonus);
        }
        return baseDamage;
    }

    public int getACText() {
        int currentAC = 10;
        int dexBonus = getBonus(STAT_DEX);
        for (Item item : myInventory) {
            currentAC += item.getAcBonus();
            int maxDexBonus = item.getMaxDexBonus();
            if (maxDexBonus >= 0) {
                dexBonus = Math.min(dexBonus, maxDexBonus);
            }
        }
        for (Condition condition : getCurrentConditions()) {
            currentAC += condition.getAcBonus();
            if (condition.isLoseAcDexBonus()) {
                dexBonus = 0;
            }
        }
        currentAC += dexBonus;
        return currentAC;
    }

    public List<Item> getInventory() {
        return myInventory;
    }

    public void setInventory(List<Item> inventory) {
        myInventory.clear();
        myInventory.addAll(inventory);
    }

    public String getFortSaveText() {
        return getSaveText(STAT_FORT, STAT_CON);
    }

    public String getRefSaveText() {
        return getSaveText(STAT_REF, STAT_DEX);
    }

    public String getWillSaveText() {
        return getSaveText(STAT_WILL, STAT_WIS);
    }

    private String getSaveText(String saveStatName, String abilityName) {
        int result = getStat(saveStatName);
        result += getBonus(abilityName);
        for (Condition condition : getCurrentConditions()) {
            result += condition.getSaveBonus();
        }
        if (result >= 0) {
            return "+" + result;
        }
        return Integer.toString(result);
    }


}

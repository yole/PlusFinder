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

    public void reset() {
        myWeapons.clear();
        myActiveWeapon = null;
        myActiveConditions.clear();
        myInventory.clear();
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

    public void addWeapon(Weapon weapon) {
        myWeapons.add(weapon);
    }

    public ActiveCondition addActiveCondition(Condition condition, boolean active) {
        ActiveCondition activeCondition = new ActiveCondition(condition, active);
        myActiveConditions.add(activeCondition);
        return activeCondition;
    }

    public List<ActiveCondition> getActiveConditions() {
        return myActiveConditions;
    }

    public boolean isConditionAvailable(Condition condition) {
        for (ActiveCondition activeCondition : myActiveConditions) {
            if (activeCondition.getCondition().equals(condition)) {
                return true;
            }
        }
        return false;
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
            if (statName.equals(StatNames.STAT_NAMES[i])) {
                return true;
            }
        }
        return false;
    }

    public static int getStatDefault(String statName) {
        return isAbility(statName) ? 10 : 0;
    }

    public String getAttackText() {
        int attackBonus = getStat(StatNames.STAT_BAB);
        int extraAttacks = 0;
        for (Condition condition : getCurrentConditions()) {
            extraAttacks += condition.getExtraAttacks();
        }

        int attackBonusModifier = myActiveWeapon.isMissile() ? getBonus(StatNames.STAT_DEX) : getBonus(StatNames.STAT_STR);
        BonusCalculator calculator = new BonusCalculator();
        for (Condition condition : getCurrentConditions()) {
            calculator.addBonus(condition.getBonusType(), multiplyByBab(condition, condition.getAttackBonus()));
        }
        attackBonusModifier += calculator.getResult();

        StringBuilder result = new StringBuilder();
        do {
            if (result.length() > 0) {
                result.append("/");
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
            return (getStat(StatNames.STAT_BAB) / multiplier + 1) * bonus;
        }
        return bonus;
    }

    public int getBonus(String statStr) {
        return (getEffectiveStat(statStr) - 10) / 2;
    }

    public int getEffectiveStat(String statName) {
        int stat = getStat(statName);
        BonusCalculator calculator = new BonusCalculator();
        for (Condition condition : getCurrentConditions()) {
            calculator.addBonus(condition.getBonusType(), condition.getBonus(statName));
        }
        return stat + calculator.getResult();
    }

    public String getDamageText() {
        if (myActiveWeapon == null) {
            return "-";
        }
        String baseDamage = myActiveWeapon.getDamageDiceCount() + "d" + myActiveWeapon.getDamageDiceSize();
        int damageBonus = myActiveWeapon.getDamageModifier();
        BonusCalculator calculator = new BonusCalculator();
        for (Condition condition : getCurrentConditions()) {
            calculator.addBonus(condition.getBonusType(), multiplyByBab(condition, condition.getDamageBonus()));
        }
        damageBonus += calculator.getResult();
        if (damageBonus > 0) {
            return baseDamage + "+" + damageBonus;
        }
        if (damageBonus < 0) {
            return baseDamage + Integer.toString(damageBonus);
        }
        return baseDamage;
    }

    public String getCritText() {
        if (myActiveWeapon == null) {
            return "-";
        }
        int critRange = myActiveWeapon.getCritRange();
        int critModifier = myActiveWeapon.getCritModifier();
        StringBuilder result = new StringBuilder();
        if (critRange != 20) {
            result.append(critRange).append("-20/");
        }
        result.append("x").append(critModifier);
        return result.toString();
    }

    public int getACText() {
        int currentAC = 10;
        int dexBonus = getBonus(StatNames.STAT_DEX);
        for (Item item : myInventory) {
            currentAC += item.getAcBonus();
            int maxDexBonus = item.getMaxDexBonus();
            if (maxDexBonus >= 0) {
                dexBonus = Math.min(dexBonus, maxDexBonus);
            }
        }
        BonusCalculator calculator = new BonusCalculator();
        for (Condition condition : getCurrentConditions()) {
            calculator.addBonus(condition.getBonusType(), condition.getAcBonus());
            if (condition.isLoseAcDexBonus()) {
                dexBonus = 0;
            }
        }
        currentAC += calculator.getResult();
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
        return getSaveText(StatNames.STAT_FORT, StatNames.STAT_CON);
    }

    public String getRefSaveText() {
        return getSaveText(StatNames.STAT_REF, StatNames.STAT_DEX);
    }

    public String getWillSaveText() {
        return getSaveText(StatNames.STAT_WILL, StatNames.STAT_WIS);
    }

    private String getSaveText(String saveStatName, String abilityName) {
        int result = getStat(saveStatName);
        result += getBonus(abilityName);
        BonusCalculator calculator = new BonusCalculator();
        for (Condition condition : getCurrentConditions()) {
            calculator.addBonus(condition.getBonusType(), condition.getSaveBonus());
            calculator.addBonus(condition.getBonusType(), condition.getBonus(saveStatName));
        }
        if (result >= 0) {
            return "+" + result;
        }
        return Integer.toString(result);
    }
}

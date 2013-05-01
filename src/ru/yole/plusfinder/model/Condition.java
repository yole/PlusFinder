package ru.yole.plusfinder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yole.plusfinder.model.StatNames.STAT_NAME_SET;

/**
 * @author yole
 */
public class Condition extends BaseEntity {
    private int myAttackBonus;
    private int myDamageBonus;
    private int myAcBonus;
    private int myBabMultiplier;
    private int myMeleeOnly;
    private int myMissileOnly;
    private int myLoseACDexBonus;
    private int mySaveBonus;
    private String myBonusType;
    private int myExtraAttacks;

    private final Map<String, Integer> myBonuses = new HashMap<String, Integer>();

    public int getBonus(String statName) {
        Integer value = myBonuses.get(statName);
        return value == null ? 0 : value;
    }

    public int getAttackBonus() {
        return myAttackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        myAttackBonus = attackBonus;
    }

    public int getDamageBonus() {
        return myDamageBonus;
    }

    public void setDamageBonus(int damageBonus) {
        myDamageBonus = damageBonus;
    }

    public int getAcBonus() {
        return myAcBonus;
    }

    public void setAcBonus(int acBonus) {
        myAcBonus = acBonus;
    }

    public boolean isLoseAcDexBonus() {
        return myLoseACDexBonus != 0;
    }

    public void setLoseACDexBonus(int loseACDexBonus) {
        myLoseACDexBonus = loseACDexBonus;
    }

    public int getSaveBonus() {
        return mySaveBonus;
    }

    public void setSaveBonus(int saveBonus) {
        mySaveBonus = saveBonus;
    }

    public String getBonusType() {
        return myBonusType;
    }

    public void setBonusType(String bonusType) {
        myBonusType = bonusType;
    }

    public int getExtraAttacks() {
        return myExtraAttacks;
    }

    public void setExtraAttacks(int extraAttacks) {
        myExtraAttacks = extraAttacks;
    }

    public int getBabMultiplier() {
        return myBabMultiplier;
    }

    public void setBabMultiplier(int babMultiplier) {
        myBabMultiplier = babMultiplier;
    }

    public boolean meleeOnly() {
        return myMeleeOnly != 0;
    }

    public void setMeleeOnly(int meleeOnly) {
        myMeleeOnly = meleeOnly;
    }

    public boolean missileOnly() {
        return myMissileOnly != 0;
    }

    public void setMissileOnly(int missileOnly) {
        myMissileOnly = missileOnly;
    }

    @Override
    public Collection<String> getFieldNames() {
        return STAT_NAME_SET;
    }

    @Override
    public void setField(String fieldName, int value) {
        myBonuses.put(fieldName, value);
    }
}

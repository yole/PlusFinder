package ru.yole.plusfinder.model;

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
    private int myStrBonus;
    private int myDexBonus;
    private int mySaveBonus;
    private int myExtraAttacks;

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

    public int getStrBonus() {
        return myStrBonus;
    }

    public void setStrBonus(int strBonus) {
        myStrBonus = strBonus;
    }

    public int getDexBonus() {
        return myDexBonus;
    }

    public void setDexBonus(int dexBonus) {
        myDexBonus = dexBonus;
    }

    public int getSaveBonus() {
        return mySaveBonus;
    }

    public void setSaveBonus(int saveBonus) {
        mySaveBonus = saveBonus;
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

}

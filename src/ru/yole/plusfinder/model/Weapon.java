package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Weapon extends Named {
    private int myDamageDiceCount = 1;
    private int myDamageDiceSize;
    private int myAttackModifier;
    private int myDamageModifier;
    private int myCritRange = 20;
    private int myCritModifier = 2;
    private int myIsMissile;

    public int getDamageDiceCount() {
        return myDamageDiceCount;
    }

    public void setDamageDiceCount(int damageDiceCount) {
        myDamageDiceCount = damageDiceCount;
    }

    public int getDamageDiceSize() {
        return myDamageDiceSize;
    }

    public void setDamageDiceSize(int damageDiceSize) {
        myDamageDiceSize = damageDiceSize;
    }

    public int getAttackModifier() {
        return myAttackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        myAttackModifier = attackModifier;
    }

    public int getDamageModifier() {
        return myDamageModifier;
    }

    public void setDamageModifier(int damageModifier) {
        myDamageModifier = damageModifier;
    }

    public int getCritRange() {
        return myCritRange;
    }

    public void setCritRange(int critRange) {
        myCritRange = critRange;
    }

    public int getCritModifier() {
        return myCritModifier;
    }

    public void setCritModifier(int critModifier) {
        myCritModifier = critModifier;
    }

    public boolean isMissile() {
        return myIsMissile > 0;
    }

    public void setIsMissile(int isMissile) {
        myIsMissile = isMissile;
    }

    @Override
    public String toString() {
        return getName();
    }
}

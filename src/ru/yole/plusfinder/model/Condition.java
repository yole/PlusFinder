package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Condition extends BaseEntity {
    private int myAttackBonus;
    private int myDamageBonus;
    private int myBabMultiplier;
    private int myMeleeOnly;
    private int myMissileOnly;

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

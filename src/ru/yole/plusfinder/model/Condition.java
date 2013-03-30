package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Condition extends Named {
    private int myAttackBonus;

    public int getAttackBonus() {
        return myAttackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        myAttackBonus = attackBonus;
    }
}

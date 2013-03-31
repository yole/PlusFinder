package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Item extends Named {
    private int myAcBonus;

    public int getAcBonus() {
        return myAcBonus;
    }

    public void setAcBonus(int acBonus) {
        myAcBonus = acBonus;
    }
}

package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Item extends Named {
    private int myAcBonus;
    private int myMaxDexBonus = -1;

    public int getAcBonus() {
        return myAcBonus;
    }

    public void setAcBonus(int acBonus) {
        myAcBonus = acBonus;
    }

    public int getMaxDexBonus() {
        return myMaxDexBonus;
    }

    public void setMaxDexBonus(int maxDexBonus) {
        myMaxDexBonus = maxDexBonus;
    }
}

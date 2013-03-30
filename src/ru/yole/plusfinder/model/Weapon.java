package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Weapon {
    private final String myName;
    private int myDamageDiceCount = 1;
    private int myDamageDiceSize;
    private int myAttackModifier;
    private int myDamageModifier;
    private int myCritRange = 20;
    private int myCritModifier = 2;

    public Weapon(String name) {
        myName = name;
    }
}

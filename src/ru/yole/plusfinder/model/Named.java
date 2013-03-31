package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class Named {
    private String myName;

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    @Override
    public String toString() {
        return myName;
    }
}

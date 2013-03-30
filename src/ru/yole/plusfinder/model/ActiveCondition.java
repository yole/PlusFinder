package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public class ActiveCondition {
    private final Condition myCondition;
    private boolean myActive;

    public ActiveCondition(Condition condition) {
        myCondition = condition;
    }

    public Condition getCondition() {
        return myCondition;
    }

    public boolean isActive() {
        return myActive;
    }

    public void setActive(boolean active) {
        myActive = active;
    }
}

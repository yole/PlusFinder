package ru.yole.plusfinder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yole
 */
public class BonusCalculator {
    private int myUntypedBonus;
    private Map<String, Integer> myTypedBonuses;

    public void addBonus(String type, int value) {
        if (type == null) {
            myUntypedBonus += value;
        }
        else if (value != 0) {
            if (myTypedBonuses == null) {
                myTypedBonuses = new HashMap<String, Integer>();
            }
            Integer prevBonus = myTypedBonuses.get(type);
            if (prevBonus == null || prevBonus < value) {
                myTypedBonuses.put(type, value);
            }
        }
    }

    public int getResult() {
        int result = myUntypedBonus;
        if (myTypedBonuses != null) {
            for (Integer integer : myTypedBonuses.values()) {
                result += integer;
            }
        }
        return result;
    }
}

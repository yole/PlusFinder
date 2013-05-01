package ru.yole.plusfinder.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yole
 */
public class StatNames {
    public static final String STAT_STR = "Str";
    public static final String STAT_DEX = "Dex";
    public static final String STAT_CON = "Con";
    public static final String STAT_WIS = "Wis";
    public static final String STAT_BAB = "BAB";
    public static final String STAT_FORT = "Fort";
    public static final String STAT_REF = "Ref";
    public static final String STAT_WILL = "Will";
    public static final String[] STAT_NAMES = {
            STAT_STR, STAT_DEX, STAT_CON, "Int", STAT_WIS, "Cha",
            STAT_BAB,
            STAT_FORT, STAT_REF, STAT_WILL
    };

    public static final Set<String> STAT_NAME_SET = new HashSet<String>(Arrays.asList(STAT_NAMES));
}

package com.ePurchase.enums;

/**
 * Created by Get It on 9/22/2017.
 */
public enum Departments {
    Catastrophic(0), Danger(1), Warning(2), Caution(3);

    private int level;

    private Departments(int level) {
        this.level = level;
    }

    public int getCode() {
        return level;
    }
}


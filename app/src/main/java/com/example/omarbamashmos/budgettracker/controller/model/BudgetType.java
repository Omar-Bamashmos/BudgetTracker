package com.example.omarbamashmos.budgettracker.controller.model;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public enum BudgetType {
    WEEKLY, MONTHLY, OTHER;

    private int otherDays;

    public int numberOfDays() {
        if (this==MONTHLY) {
            return 30;

        }
        else if(this==WEEKLY) {
            return 7;
        }
        else
            return otherDays;

    }

    public void setOtherDays(int num) {
        this.otherDays=num;
    }

}


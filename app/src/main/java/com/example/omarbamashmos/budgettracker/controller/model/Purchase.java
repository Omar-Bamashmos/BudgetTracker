package com.example.omarbamashmos.budgettracker.controller.model;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public class Purchase {
    private String name, memo;
   // private int id;
    private double amount;

    public Purchase(String Name, String memo, double amount) {
        //this.id=i;
        this.name=Name;
        this.memo=memo;
        this.amount=amount;

    }
    //public int getID(){return this.id; }
    public String getName() {
        return this.name;
    }
    public String getMemo() {
        return this.memo;
    }

    public double getAmount() {
        return this.amount;
    }
    //implement editting behavior if applicable
    //TODO
}


package com.example.omarbamashmos.budgettracker.controller.model;

import android.view.Display;

import java.util.ArrayList;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */
//singleton class
public class Budget {

    private static String Name, date;
    private static double limit;
    private static BudgetType budget_type;
    public static ArrayList<Purchase> purchases;
    private static int otherDays=0;
    public static int counter=1;

    private static Budget b=new Budget();

    private Budget(){}

    public void init(String name, double p, String dateP, BudgetType type) {

        Name=name;
        limit=p;
        date=dateP;
        budget_type=type;
        purchases=new ArrayList<>();


    }
    public void init(String name, double p, String dateP, BudgetType type, int numOfDays) {

        Name=name;
        limit=p;
        date=dateP;
        budget_type=type;
        purchases=new ArrayList<>();
        otherDays=numOfDays;
    }

    public static Budget getInstance(){
        return b;
    }

    //get Name
    public static  String getName() {
        return Name;
    }
    public static  String getDate() {
        return date;
    }

    //get integer
    public static double getlimit() {
        return limit;
    }

    //get date
    public static int getType() {
        int numOfDays;
        switch(budget_type){
            case MONTHLY:
                numOfDays=30;
                break;
            case WEEKLY:
                numOfDays=7;
                break;
            default:
                numOfDays=otherDays;


        }

        return numOfDays;
    }

    public  void addPurchase(Purchase purchaseToAdd) {
        purchases.add(purchaseToAdd);
    }

    public void deletePurchase(Purchase p){
        purchases.remove(p);

    }

    public static double getRemainingBalance(){
        double summer=0;
        if(purchases!=null){

            for(Purchase a: purchases){
                summer+=a.getAmount();
            }
        }


        return summer;

    }

    public ArrayList<Purchase> getPurchasesList(){

        return purchases;
    }








}


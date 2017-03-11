package com.example.omarbamashmos.budgettracker.controller.persistence_layer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Omar Ba mashmos on 2016-12-24.
 */

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String db_name="budgetTracker";

    private static final int db_version=1;
    private static final String SQL_query1=
            "CREATE TABLE "+PurchaseInfo.fields.TABLE_NAME+"("+ PurchaseInfo.fields.PURCHASE_NAME+" TEXT,"+PurchaseInfo.fields.PURCHASE_AMOUNT+" TEXT,"+PurchaseInfo.fields.PURCHASE_MEMO+" TEXT);";
    private static final String SQL_query2=
            "CREATE TABLE "+BudgetInfo.info.TABLE_NAME+"("+BudgetInfo.info.BUDGET_NAME+" TEXT,"+BudgetInfo.info.LIMIT+" TEXT,"+BudgetInfo.info.DATE+" TEXT,"+BudgetInfo.info.BUDGET_TYPE+" TEXT);";



    private static final String SQL_deleteTable1="DROP TABLE IF EXISTS "+ PurchaseInfo.fields.TABLE_NAME;
    private static final String SQL_deleteTable2="DROP TABLE IF EXISTS "+ BudgetInfo.info.TABLE_NAME;


    public UserDBHelper(Context context){

        super(context, db_name, null, db_version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_query2);
        db.execSQL(SQL_query1);


    }
    public void budgetData(String name, String limit, String date, String bt, SQLiteDatabase db){

        ContentValues content=new ContentValues();
        content.put(BudgetInfo.info.BUDGET_NAME, name+","+bt);
        content.put(BudgetInfo.info.LIMIT,limit);
        content.put(BudgetInfo.info.DATE, date);
        content.put(BudgetInfo.info.BUDGET_TYPE, bt);


        db.insert(BudgetInfo.info.TABLE_NAME, null, content);

    }

    public void addRowPurchase(String name, String amount, String memo, SQLiteDatabase db){

        ContentValues content=new ContentValues();
        content.put(PurchaseInfo.fields.PURCHASE_NAME, name);
        content.put(PurchaseInfo.fields.PURCHASE_AMOUNT,amount);
        content.put(PurchaseInfo.fields.PURCHASE_MEMO, memo);

        db.insert(PurchaseInfo.fields.TABLE_NAME, null, content);

    }



    public void deleteTable(SQLiteDatabase db){

            db.execSQL(SQL_deleteTable1);
            db.execSQL(SQL_deleteTable2);


    }

    public Cursor loadBudget(SQLiteDatabase db){

        Cursor cursor;
        String[] projections={BudgetInfo.info.BUDGET_NAME, BudgetInfo.info.LIMIT, BudgetInfo.info.DATE, BudgetInfo.info.BUDGET_TYPE};
        cursor=db.query(BudgetInfo.info.TABLE_NAME, projections, null, null, null, null, null);

        return cursor;
    }

    public Cursor load(SQLiteDatabase db){

        Cursor cursor;
        String[] projections={PurchaseInfo.fields.PURCHASE_NAME, PurchaseInfo.fields.PURCHASE_AMOUNT, PurchaseInfo.fields.PURCHASE_MEMO};
        cursor=db.query(PurchaseInfo.fields.TABLE_NAME, projections, null, null, null, null, null);

        return cursor;
    }

    public boolean TableExists(SQLiteDatabase db)
    {
        String tableName=BudgetInfo.info.TABLE_NAME;

        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void delete(SQLiteDatabase db, int entry) {
        String id=String.valueOf(entry);
        db.execSQL("delete from "+PurchaseInfo.fields.TABLE_NAME+" where Google='"+id+"'");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

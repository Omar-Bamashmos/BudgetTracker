package com.example.omarbamashmos.budgettracker.controller.Controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omarbamashmos.budgettracker.R;
import com.example.omarbamashmos.budgettracker.controller.model.Budget;
import com.example.omarbamashmos.budgettracker.controller.model.BudgetType;
import com.example.omarbamashmos.budgettracker.controller.model.Purchase;
import com.example.omarbamashmos.budgettracker.controller.persistence_layer.UserDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public class ExistingBudget extends Activity {

    private TextView remainingDays, budgetLimit, remainingBalance, expenses, date;
    private EditText purchaseName, purchaseAmount, purchaseMemo, decisionAmount;
    private Budget b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing_budget_window);

        //budget properties features

        remainingDays=(TextView)findViewById(R.id.remainingDays);
        remainingBalance=(TextView)findViewById(R.id.remainingBalance);
        budgetLimit=(TextView)findViewById(R.id.budgetLimit);
        expenses=(TextView)findViewById(R.id.expenses);
        date=(TextView)findViewById(R.id.date);

        b=Budget.getInstance();



        //update text views
        remainingDays.setText("   "+getRemainingDays(b.getDate(), b.getType())+"\n");
        remainingDays.append("  Days");


        //date.setText(b.getDate());
        budgetLimit.setText("  "+b.getlimit()+"\n");
        budgetLimit.append("    $");

        remainingBalance.setText("  "+String.valueOf(b.getlimit()-b.getRemainingBalance())+"\n");
        remainingBalance.append("    $");

        expenses.setText("  "+String.valueOf(b.getRemainingBalance())+"\n");
        expenses.append("    $");

        date.setText(" "+b.getDate());


        int screen_width=getWindowManager().getDefaultDisplay().getWidth();

        //buttons
        Button newDecision=(Button) findViewById(R.id.purchaseDesicion);
        decisionAmount=(EditText) findViewById(R.id.decision_amount);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(screen_width/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2=new LinearLayout.LayoutParams(screen_width/2-80, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin=20;
        lp2.leftMargin=20;
        newDecision.setLayoutParams(lp);
        decisionAmount.setLayoutParams(lp2);


        newDecision.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int amount=Integer.parseInt(decisionAmount.getText().toString());
                //check if value is good
                if(b.getlimit()-b.getRemainingBalance()>=amount){
                    Toast.makeText(getApplicationContext(), "Go ahead with this purchase", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No enough money :(", Toast.LENGTH_LONG).show();

                }
                decisionAmount.setText("");

            }

        });

        // view purchases
        final Button viewPurchases=(Button)findViewById(R.id.viewPurchases);
        viewPurchases.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent tran=new Intent(v.getContext(), PurchasesList.class);
                startActivity(tran);
            }

        });

        //add purchase
        purchaseName=(EditText)findViewById(R.id.purchaseName);
        purchaseAmount=(EditText)findViewById(R.id.purchaseamount);
        purchaseMemo=(EditText)findViewById(R.id.memo);

        final Button addPurchase=(Button) findViewById(R.id.addPurcahse);
        addPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfFilled()){
                    double amount=Double.parseDouble(purchaseAmount.getText().toString());
                    if(b.getlimit()-b.getRemainingBalance()<amount) {
                        Toast.makeText(getApplicationContext(), "No enough money :(", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String p_name=purchaseName.getText().toString();
                        String p_amount=purchaseAmount.getText().toString();
                        String p_memo=purchaseMemo.getText().toString();
                        Purchase newPurchase=new Purchase(p_name, p_memo, Double.parseDouble(p_amount));

                        UserDBHelper userh=new UserDBHelper(getApplicationContext());
                        SQLiteDatabase db=userh.getWritableDatabase();

                        b.addPurchase(newPurchase);
                        userh.addRowPurchase(p_name, p_amount, p_memo, db);
                    }
                    purchaseName.setText("");
                    purchaseAmount.setText("");
                    purchaseMemo.setText("");

                }


            }
        });

    }

    private boolean checkIfFilled() {
        if(purchaseName.getText().toString().equals("")||purchaseAmount.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Purchase Name or amount is missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    protected static int getRemainingDays(String date, int days){

        String[] entries=date.split("/");
        int day1=Integer.valueOf(entries[0]);
        int month1=Integer.valueOf(entries[1]);
        int year1=Integer.valueOf(entries[2]);

        //today's date'
        String[] b=(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime())).split("/");

        int day2=Integer.valueOf(b[0]);
        int month2=Integer.valueOf(b[1]);
        int year2=Integer.valueOf(b[2]);
        int count;

        if(year1==year2){

            if(month1==month2){
                count= day2-day1;

            }else{
                count= day2+(numDaysMonth(month1, year1)-day1)+getMonths(year1, month1, month2);
            }

        }else{
            count= day2+(numDaysMonth(month1, year1)-day1)+getYears(year1, year2, month1, month2);
        }

        return days-count;
    }

    private static int numDaysMonth(int month, int year){
        switch(month){
            case 1:
                return 31;
            case 2:
                if(year%4==0) return 29;
                else return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            default:
                return 31;


        }


    }

    private static int getMonths(int yr,int m1, int m2){
        int count=0;
        for(int i=m1+1; i<=m2-1; i++){
            count+=numDaysMonth(i, yr);
        }
        return count;
    }

    private static int getYears(int yr1, int yr2, int m1, int m2){
        int counter=0;
        for(int i=yr1; i<=yr2; i++){
            if(i==yr1){
                counter+=getMonths(i,m1, 13);
            }else if(i==yr2){
                counter+=getMonths(i, 1, m2+1);
            }else{
                if(i%4==0){
                    counter+=366;
                }else{
                    counter+=365;
                }

            }

        }
        return counter;


    }

}



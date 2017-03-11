package com.example.omarbamashmos.budgettracker.controller.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.omarbamashmos.budgettracker.R;
import com.example.omarbamashmos.budgettracker.controller.model.Budget;
import com.example.omarbamashmos.budgettracker.controller.model.BudgetType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.omarbamashmos.budgettracker.controller.model.BudgetType;
import com.example.omarbamashmos.budgettracker.controller.persistence_layer.UserDBHelper;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public class CreateNewBudget extends Activity {
    private Button submit, clear;
    private EditText budgetName, date, budgetLimit, budgetDur ;
    private CheckBox checkBox;
    private RadioButton month, week, otherOption;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_budget);

        //initialization
        submit=(Button)findViewById(R.id.submit);
        clear=(Button)findViewById(R.id.clear);
        checkBox=(CheckBox)findViewById(R.id.ifToday);
        budgetName=(EditText)findViewById(R.id.budgetName);
        date=(EditText)findViewById(R.id.datePicker);
        budgetLimit=(EditText)findViewById(R.id.budgetLimit);
        otherOption=(RadioButton)findViewById(R.id.other);

        //radio buttons
        month=(RadioButton)findViewById(R.id.Monthly);
        week=(RadioButton)findViewById(R.id.weekly);
        otherOption=(RadioButton)findViewById(R.id.other);
        budgetDur=(EditText)findViewById(R.id.otherBudgetDur);


        submit.setOnClickListener(new View.OnClickListener() {
            //move to the create new budget page
            @Override
            public void onClick(View view) {

                String dateBudget;
                BudgetType budget_type;


                    if(checkifAllisValid()){

                        String name=budgetName.getText().toString();

                        if(checkBox.isChecked()) {
                            dateBudget= new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());;
                        }
                        else {
                            dateBudget=date.getText().toString();
                        }

                        double limit=Double.parseDouble(budgetLimit.getText().toString());
                        if (month.isChecked()){
                            budget_type= BudgetType.MONTHLY;
                        //todo
                        }
                        else if (week.isChecked()){
                            budget_type=BudgetType.WEEKLY;
                        }
                        //if others
                        else  {

                            budget_type=BudgetType.OTHER;
                            int numberDays=Integer.parseInt(budgetDur.getText().toString());

                        }
                        //create Budget instance
                        Budget b=Budget.getInstance();
                        b.init(name, limit, dateBudget, budget_type);

                        UserDBHelper userhelper=new UserDBHelper(getApplicationContext());
                        SQLiteDatabase db=userhelper.getWritableDatabase();
                        userhelper.onCreate(db);
                        userhelper.budgetData(name, String.valueOf(limit), dateBudget, budget_type.toString(), db);

                        Intent intent = new Intent(view.getContext(), ExistingBudget.class);
                        startActivity(intent);


                    }


            }

        });

        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                budgetName.setText("");
                budgetLimit.setText("");
                budgetDur.setText("");
                checkBox.setChecked(true);
                month.setChecked(false);
                week.setChecked(false);
                otherOption.setChecked(false);
            }


        });


    }

    private boolean checkifAllisValid() {
        if (budgetName.getText().toString().equals("")) {
            System.out.println("no budget name");
            Toast.makeText(getApplicationContext(), "Please enter budget Name", Toast.LENGTH_LONG).show();

            return false;
        }
        if (budgetLimit.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter budget Limit", Toast.LENGTH_LONG).show();
            return false;
        }
        if (date.getText().toString().equals("") && !checkBox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Date is not specified", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!month.isChecked() && !week.isChecked()) {
            if (!otherOption.isChecked()) {
                Toast.makeText(getApplicationContext(), "Check budget Duration option", Toast.LENGTH_LONG).show();
                return false;
            } else if (budgetDur.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Specify budget duration", Toast.LENGTH_LONG).show();
                return false;
            }


        }
        int n;
        if (month.isChecked()) n = 30;
        else if (week.isChecked()) n = 7;
        else n = Integer.valueOf(otherOption.getText().toString());

        int remaining_days=0;
        if(!date.getText().toString().equals("")){
            remaining_days= ExistingBudget.getRemainingDays(date.getText().toString(), n);
             if (remaining_days <= 0) {
                Toast.makeText(getApplicationContext(), "Budget duration is already expired", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;

    }


}


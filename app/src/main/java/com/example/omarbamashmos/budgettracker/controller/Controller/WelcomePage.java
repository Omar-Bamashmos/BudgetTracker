package com.example.omarbamashmos.budgettracker.controller.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.omarbamashmos.budgettracker.R;
import com.example.omarbamashmos.budgettracker.controller.model.Budget;
import com.example.omarbamashmos.budgettracker.controller.model.BudgetType;
import com.example.omarbamashmos.budgettracker.controller.model.Purchase;
import com.example.omarbamashmos.budgettracker.controller.persistence_layer.UserDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WelcomePage extends Activity {

    Button createButton;
    Button existingButton;
    Button deleteButton;
    TextView todayDate;
    Context context=this;
    UserDBHelper userhelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        createButton=(Button)findViewById(R.id.createNew);
        existingButton=(Button)findViewById(R.id.existingButton);
        deleteButton=(Button)findViewById(R.id.deleteButton);
        todayDate=(TextView)findViewById(R.id.todayDate);

        String dateBudget= new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());;
        todayDate.setText(dateBudget);

        userhelper=new UserDBHelper(context);
        db=userhelper.getWritableDatabase();


        //save file here
        //check if file exists
        //if yes
        if(userhelper.TableExists(db)){
            createBudgetInstance();
            int num;
            Budget b=Budget.getInstance();
            if(b.getDate()!=null)
                num=ExistingBudget.getRemainingDays(b.getDate(), b.getType());
            else num=0;
            if(num>0){
                existingButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }else{
                existingButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        }else{
            existingButton.setEnabled(false);
            deleteButton.setEnabled(false);

        }



        //go to new budget page
        createButton.setOnClickListener(new View.OnClickListener() {
            //move to the create new budget page
            @Override
            public void onClick(View view) {
                userhelper.deleteTable(db);

                Intent intent = new Intent(view.getContext(), CreateNewBudget.class);
                startActivity(intent);
            }

        });

        existingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createBudgetInstance();

                Intent intent = new Intent(view.getContext(), ExistingBudget.class);
                startActivity(intent);
            }

        });

        //delete button
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userhelper.deleteTable(db);
            }
        });
    }
    private void createBudgetInstance(){
        Cursor cursor;
        cursor=userhelper.loadBudget(db);
        cursor.moveToFirst();
        Budget b=Budget.getInstance();

        if(cursor.moveToFirst()){

            BudgetType bt;

            switch(cursor.getString(3)){
                case "MONTHLY":
                    b.init(cursor.getString(0), Double.valueOf(cursor.getString(1)), cursor.getString(2), BudgetType.MONTHLY);
                    break;
                case "WEEKLY":
                    b.init(cursor.getString(0), Double.valueOf(cursor.getString(1)), cursor.getString(2), BudgetType.WEEKLY);
                    break;
                default:
                    String[] ff=cursor.getString(3).split(",");

                    b.init(cursor.getString(0), Double.valueOf(cursor.getString(1)), cursor.getString(2), BudgetType.OTHER, Integer.valueOf(ff[1]));
                    break;

            }

        }

        //add all purchases
        Cursor cur=userhelper.load(db);

        if(cur.moveToFirst()){
            do{
                Purchase p=new Purchase(cur.getString(0), cur.getString(2), Double.valueOf(cur.getString(1)));
                b.addPurchase(p);


            }while(cur.moveToNext());


        }

    }
}

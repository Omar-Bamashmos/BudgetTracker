package com.example.omarbamashmos.budgettracker.controller.Controller;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.omarbamashmos.budgettracker.R;
import com.example.omarbamashmos.budgettracker.controller.model.Budget;
import com.example.omarbamashmos.budgettracker.controller.model.BudgetType;
import com.example.omarbamashmos.budgettracker.controller.model.Purchase;
import com.example.omarbamashmos.budgettracker.controller.persistence_layer.UserDBHelper;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public class PurchasesList extends Activity {
    private TableLayout purchasesTable;
    private TextView number, amount, name_memo;
    int count=1;
    int screen_width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchases_list);

        screen_width=getWindowManager().getDefaultDisplay().getWidth();
        number=(TextView)findViewById(R.id.num);
        name_memo=(TextView)findViewById(R.id.name);
        amount=(TextView)findViewById(R.id.price);

        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)name_memo.getLayoutParams();
        lp.rightMargin=screen_width/2-130;
        name_memo.setLayoutParams(lp);
        populateRows();



    }

    private void populateRows(){

        //test budget
        Budget b=Budget.getInstance();


        //table
        purchasesTable=(TableLayout)findViewById(R.id.table);


        for(Purchase x: b.getPurchasesList()) {

            final Purchase temp=x;

            View row=getLayoutInflater().inflate(R.layout.purchase_row, null);
            LinearLayout nm_row=(LinearLayout)row.findViewById(R.id.name_memo_row);
            LinearLayout.LayoutParams lp_r=(LinearLayout.LayoutParams)nm_row.getLayoutParams();
            lp_r.width=screen_width/2-130;
            nm_row.setLayoutParams(lp_r);

            TextView name=(TextView)row.findViewById(R.id.nameP);
            TextView number=(TextView)row.findViewById(R.id.number);
            TextView amount=(TextView)row.findViewById(R.id.amountP);
            TextView memo=(TextView)row.findViewById(R.id.memo);
            final Button delete=(Button)row.findViewById(R.id.delete_button);


            number.setText(String.valueOf(count));
            name.setText(x.getName());
            amount.setText(String.valueOf(x.getAmount()));
            memo.setText(x.getMemo());

            final TableRow newRow=new TableRow(this);
            newRow.addView(row);
            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    purchasesTable.removeAllViews();
                    UserDBHelper userhelper=new UserDBHelper(getApplicationContext());
                    SQLiteDatabase db=userhelper.getWritableDatabase();

                    //userhelper.delete(db, 1);
                    Budget b=Budget.getInstance();
                    b.deletePurchase(temp);
                    count=1;
                    populateRows();


                }


            });



            purchasesTable.addView(newRow);

            count++;






        }


    }




}


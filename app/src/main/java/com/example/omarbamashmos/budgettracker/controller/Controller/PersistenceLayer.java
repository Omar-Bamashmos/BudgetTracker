package com.example.omarbamashmos.budgettracker.controller.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Omar Ba mashmos on 2016-12-22.
 */

public class PersistenceLayer extends Activity {
    private static File database;
    private static PersistenceLayer p=new PersistenceLayer();

    private PersistenceLayer(){}

    public void init(File f){
        database=f;
    }
    public static PersistenceLayer getInstance(){
        return p;
    }



    public static void writeLine(String line){
        try{
           FileOutputStream os=new FileOutputStream(database);
            OutputStreamWriter writer=new OutputStreamWriter(os);
            BufferedWriter bw=new BufferedWriter(writer);
            bw.append(line);
            bw.newLine();
            os.close();
            bw.close();

        }catch(FileNotFoundException t){t.printStackTrace();}
        catch(IOException e){e.printStackTrace();}


    }

    public static ArrayList<String> Load(){
       ArrayList<String> data=new ArrayList<>();
        try{
            FileInputStream is=new FileInputStream(database);
            InputStreamReader reader=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(reader);

            String line;
            while((line=br.readLine())!=null){

                data.add(line);

            }



        }catch(FileNotFoundException t){}
        catch(IOException e){}

        return data;
    }





}

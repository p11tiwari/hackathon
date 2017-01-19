package com.skipp.mgnrega;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class PreDataEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner state,year,district,block;
    ArrayList<String> stateArray,yearArray,districtArray,blockArray,dialogItems;
    String selected;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_data_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialogItems = new ArrayList<>();
        dialogItems.add("Request Job");
        dialogItems.add("Generate Report");
        sharedPreferences = this.getSharedPreferences("skipp", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(PreDataEntry.this)
                        .title("Select")
                        .items(dialogItems)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                selected = ""+text;
                                return true;
                            }
                        })
                        .positiveText("Next")
                        .negativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new MaterialDialog.Builder(PreDataEntry.this)
                                        .title("Card Number")
                                        .content("Enter your NREGA card number")
                                        .inputType(InputType.TYPE_CLASS_NUMBER)
                                        .negativeText("Cancel")
                                        .positiveText("Done")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .input("Enter card number","", new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                String cardNumber = ""+input;
                                                editor.putString("cardnumber",cardNumber);
                                                editor.apply();
                                                Intent intent = new Intent(PreDataEntry.this,Details.class);
                                                startActivity(intent);
                                            }
                                        }).show();
                            }
                        })
                        .show();
            }
        });


        stateArray = new ArrayList<>();
        yearArray = new ArrayList<>();
        districtArray = new ArrayList<>();
        blockArray = new ArrayList<>();
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stateArray);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearArray);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtArray);
        ArrayAdapter<String> blockAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, blockArray);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state = (Spinner) findViewById(R.id.state);
        year = (Spinner) findViewById(R.id.year);
        district = (Spinner) findViewById(R.id.district);
        block = (Spinner) findViewById(R.id.block);
        stateArray.add("State");
        stateArray.add("Maharashtra");
        stateArray.add("Madhya Pradesh");
        stateArray.add("Andhra Pradesh");
        stateArray.add("Uttar Pradesh");

        yearArray.add("Financial Year");
        yearArray.add("2013-2014");
        yearArray.add("2014-2015");
        yearArray.add("2016-2017");

        districtArray.add("District");
        districtArray.add("Nashik");
        districtArray.add("Jalgaon");
        districtArray.add("Chnadrapur");

        blockArray.add("Block");
        blockArray.add("Chnadvad");
        blockArray.add("Deola");
        blockArray.add("Dindori");


        state.setAdapter(stateAdapter);
        year.setAdapter(yearAdapter);
        district.setAdapter(districtAdapter);
        block.setAdapter(blockAdapter);

        state.setOnItemSelectedListener(this);


       // StringRequest stringRequest = new StringRequest(Request.Method.GET,"forceclose.org/gz/readappliances.php",,new Response.Listener<String>())
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

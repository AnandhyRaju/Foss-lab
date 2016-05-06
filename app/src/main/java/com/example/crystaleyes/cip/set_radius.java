package com.example.crystaleyes.cip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class set_radius extends AppCompatActivity {

    SharedPreferences general;
    SharedPreferences.Editor edit;
    EditText rad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_radius);
        general=getApplicationContext().getSharedPreferences("General", Context.MODE_MULTI_PROCESS);
        edit=general.edit();
        rad=(EditText)findViewById(R.id.editText4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.remove("radius").commit();
                edit.putString("radius",rad.getText().toString()).commit();
            }
        });
    }

}

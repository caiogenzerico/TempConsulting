package com.example.caiogenzerico.com.br.tempconsulting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected EditText locationEditText;
    protected ListView weatherListView;
    protected WeatherArrayAdapter weatherAdapter;
    protected List<Weather> weatherList;
    protected ArrayList<String>historylist;
    protected String cidade = "";
    protected ListView cityListView;
    protected static String endereco = "";
    private AlertDialog aviso;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationEditText = findViewById(R.id.locationEditText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        cityListView = findViewById(R.id.cityListView);
        setSupportActionBar(toolbar);
        historylist = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, historylist);
        runOnUiThread(()->{
            adapter.notifyDataSetChanged();
        });

        cityListView.setAdapter(adapter);

        cityListView.setOnItemLongClickListener((arg0, v, index, arg3) -> {
            show_confirmation(index);
            cityListView.setAdapter(adapter);
            return true;
        });

        cityListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("User clicked ", historylist.get(position));
            endereco = getString(
                    R.string.web_service_url,
                    getString(R.string.desc_language),
                    historylist.get(position),
                    getString(R.string.api_key),
                    getString(R.string.measurement_unit));
                    cityListView.setAdapter(adapter);
            startActivity(new Intent(MainActivity.this, Activity_two.class));
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((v) -> {
            cidade = locationEditText.
                    getEditableText().toString();
            if (historylist.indexOf(cidade) == -1) {
                historylist.add(cidade);
            }
            endereco = getString(
                    R.string.web_service_url,
                    getString(R.string.desc_language),
                    cidade,
                    getString(R.string.api_key),
                    getString(R.string.measurement_unit));
            cityListView.setAdapter(adapter);
            //startActivity(new Intent(MainActivity.this, Activity_two.class));
        });
    }

    private void show_confirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention");
        builder.setMessage(getString(R.string.confirmation,
                historylist.get(position)));
        builder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                historylist.remove(position);
                cityListView.setAdapter(adapter);

            }
        });

        builder.setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        aviso = builder.create();
        aviso.show();
    }
}
package com.example.antoine.deadornotdead;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.antoine.deadornotdead.wiki.WikiAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Spinner spinner;
    private WikiAPI wikiApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        tvResult = findViewById(R.id.tvResult);
        wikiApi = new WikiAPI();
        editText.setText("Sean connery");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WikiAPI wikiApi = new WikiAPI();
                if (editText.getText().length()>0) {
                    wikiApi.getListTitle(5,editText.getText().toString());
                } else {
                    wikiApi.getListTitle(5,"madonna");
                }
            }
        });
        
        spinner = findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position<spinner.getCount()) {
                    String s= spinner.getSelectedItem().toString();
                    wikiApi.getPage(s);
                } else {
                    tvResult.setText("erreur de spinner");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventListTitle event) {

        String[] results = new String[event.getListTitle().size()];
        for (int i=0;i<event.getListTitle().size();i++) {
            results[i] = (String) event.getListTitle().get(i);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        results); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // Et on envoie la recherche de la premiere page
        wikiApi.getPage(event.getListTitle().get(0));

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventPage event) {
        tvResult.setText(event.getPersonne().toString());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventErreur event) {
        tvResult.setText(event.getErreur());
    }
}

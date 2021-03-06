package com.example.antoine.deadornotdead;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antoine.deadornotdead.recyclingReponse.RecyclerTouchListener;
import com.example.antoine.deadornotdead.recyclingReponse.Reponse;
import com.example.antoine.deadornotdead.recyclingReponse.ReponseAdapter;
import com.example.antoine.deadornotdead.wiki.WikiAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mondebug;
    private WikiAPI wikiApi;
    private RecyclerView recyclerView;
    private ReponseAdapter repAdapter;
    private List<Reponse> repList = new ArrayList<>();
    private int posRepClic;

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

        final SearchView searchView = findViewById(R.id.editText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>0) {
                    wikiApi.getListTitle(Parametre.nombreResultats,query);
                } else {
                    wikiApi.getListTitle(Parametre.nombreResultats,"madonna");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        Button button = findViewById(R.id.button);
        mondebug = findViewById(R.id.tvResult);
        wikiApi = new WikiAPI();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchView.getQuery().length()>0) {
                    wikiApi.getListTitle(Parametre.nombreResultats,searchView.getQuery().toString());
                } else {
                    wikiApi.getListTitle(Parametre.nombreResultats,"madonna");
                }
            }
        });



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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (repAdapter!=null) repAdapter.clear();
        repAdapter = new ReponseAdapter(repList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Reponse reponseClick = repList.get(position);
                posRepClic=position;
                wikiApi.getPage(reponseClick.getValeurReponse());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerView.setAdapter(repAdapter);

        for (int i=0;i<event.getListTitle().size();i++) {
            repList.add(new Reponse(event.getListTitle().get(i))) ;
        }

        repAdapter.notifyDataSetChanged();

        // Et on envoie la recherche de la premiere page
        // wikiApi.getPage(event.getListTitle().get(0));

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventPage event) {

       // Toast.makeText(getApplicationContext(), "OK "+event.getPersonne().getNom(), Toast.LENGTH_SHORT).show();

        TextView tmp = findViewById(R.id.nom);
        if (event.getPersonne().getNom()!=null) tmp.setText(event.getPersonne().getNom());
            else tmp.setText("");
        tmp = findViewById(R.id.datenaissance);
        if (event.getPersonne().getDateNaissance()!=null) tmp.setText("Date de naissance : "+event.getPersonne().getDateNaissance());
            else tmp.setText("");

        tmp = findViewById(R.id.age);
        event.getPersonne().calculAge();
        if (event.getPersonne().getAge()>0) tmp.setText("Age : "+event.getPersonne().getAge()+"");
            else tmp.setText("");

        tmp = findViewById(R.id.datedeces);
        if (event.getPersonne().getDateDeces()!=null) tmp.setText("Date de decès : "+event.getPersonne().getDateDeces());
            else tmp.setText("");

        ImageView tmpImage = findViewById(R.id.imageResult);
        if (event.getPersonne().isMort()) tmpImage.setImageResource(R.drawable.mort);
            else tmpImage.setImageResource(R.drawable.pasmort);
        mondebug.setText(event.getPersonne().toString());

       //Picasso.with(holder.photo.getContext()).load(movie.getPhoto()).into(holder.photo);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventErreur event) {
        //Toast.makeText(getApplicationContext(), "KO "+event.getErreur(), Toast.LENGTH_SHORT).show();

        ImageView tmpImage = findViewById(R.id.imageResult);
        tmpImage.setImageResource(R.drawable.intero);
        mondebug.setText(event.getErreur());

        TextView tmp = findViewById(R.id.nom);
        tmp.setText("");
        tmp = findViewById(R.id.datenaissance);
        tmp.setText("");
        tmp = findViewById(R.id.age);
        tmp.setText("");
        tmp = findViewById(R.id.datedeces);
        tmp.setText("");


    }
}

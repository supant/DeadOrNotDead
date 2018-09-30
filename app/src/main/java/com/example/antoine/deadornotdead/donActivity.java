package com.example.antoine.deadornotdead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class donActivity extends AppCompatActivity {

    private TextView mondebug;
    private WikiAPI wikiApi;
    private RecyclerView recyclerView;
    private LinearLayout personneView;
    private ReponseAdapter repAdapter;
    private List<Reponse> repList = new ArrayList<>();
    private int posRepClic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mondebug = findViewById(R.id.debut_txt);
        personneView =findViewById(R.id.personne);
        recyclerView = (RecyclerView) findViewById(R.id.rv_resultat);
        wikiApi = new WikiAPI();

        final SearchView searchView = findViewById(R.id.recherche_txt);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>0) {
                    wikiApi.getListTitle(Parametre.nombreResultats,query);
                } else {
                    wikiApi.getListTitle(Parametre.nombreResultats,"madonna");
                }
                if(recyclerView!=null) recyclerView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        ImageButton imageButton_back = findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remplir_rv_populaire();
            }

        });
        remplir_rv_populaire();
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

    public void init_RecyclerView() {
        if(recyclerView!=null)  recyclerView.setVisibility(View.VISIBLE);
        if(personneView!=null)  personneView.setVisibility(View.GONE);

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
                if (posRepClic>0 || !reponseClick.getValeurReponse().equals("Les + populaires")) wikiApi.getPage(reponseClick.getValeurReponse());

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(repAdapter);
    }

    public void remplir_rv_populaire() {
        init_RecyclerView();
        repList.add(new Reponse("Les + populaires")) ;
        repList.add(new Reponse("Madonna")) ;
        repList.add(new Reponse("Corbier")) ;
        repList.add(new Reponse("Jean Rochefort")) ;
        repList.add(new Reponse("Jennifer Aniston")) ;

        repAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventListTitle event) {
        init_RecyclerView();

        for (int i=0;i<event.getListTitle().size();i++) {
            repList.add(new Reponse(event.getListTitle().get(i))) ;
        }

        repAdapter.notifyDataSetChanged();

        // Et on envoie la recherche de la premiere page
        // wikiApi.getPage(event.getListTitle().get(0));

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventPage event) {


        if(recyclerView!=null)  recyclerView.setVisibility(View.GONE);
        if(personneView!=null)  personneView.setVisibility(View.VISIBLE);

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
        Toast.makeText(getApplicationContext(), "KO "+event.getErreur(), Toast.LENGTH_SHORT).show();

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

package com.example.antoine.deadornotdead.wiki;

import com.example.antoine.deadornotdead.MessageEventErreur;
import com.example.antoine.deadornotdead.MessageEventListTitle;
import com.example.antoine.deadornotdead.MessageEventPage;
import com.example.antoine.deadornotdead.myJsonListeTitle.ListeTitle;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by antoine.gagneux on 12/10/2017.
 */

public class WikiAPI {
    private WikiService service;

    public WikiAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WikiService.class);
    }

    public void getListTitle(int limit, String texte) {
        texte = texte.replace(" ","+");
        Call<ListeTitle>  req=service.getListTitle(limit+"",texte);
        req.enqueue(new Callback<ListeTitle>() {
            @Override
            public void onResponse(Call<ListeTitle> call,Response<ListeTitle> response) {
                // handle success
                MessageEventListTitle me = new MessageEventListTitle();
                for(int i=0;i<response.body().getQuery().getSearch().size();i++) {
                    me.addtListTitle(response.body().getQuery().getSearch().get(i).getTitle());
                }
                EventBus.getDefault().post(me);
            }

            @Override
            public void onFailure(Call<ListeTitle> call,Throwable t) {
                MessageEventErreur me = new MessageEventErreur();
                me.setErreur("erreur "+call.toString());
                EventBus.getDefault().post(me);
            }
        });
    }

    public void getPage(String texte) {
        //texte = texte.replace(" ","+");
        Call<ResponseBody>  req=service.getPage(texte);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                // handle success

                int retour=0;
                ParseWiki pw=null;
                try {
                    pw = new ParseWiki(new String(response.body().bytes()),1);
                    retour = pw.parse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (pw==null) {
                    MessageEventErreur me = new MessageEventErreur();
                    me.setErreur("erreur inconnue pw==null !");
                    EventBus.getDefault().post(me);
                } else {
                    if (retour==0) {
                        MessageEventErreur me = new MessageEventErreur();
                        me.setErreur(pw.getErreur());
                        EventBus.getDefault().post(me);
                    } else {
                        EventBus.getDefault().post(pw.getMep());
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                MessageEventErreur me = new MessageEventErreur();
                me.setErreur("erreur "+call.request().toString());
                EventBus.getDefault().post(me);
            }
        });
    }

}

package com.example.antoine.deadornotdead.wiki;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.GET;
import  com.example.antoine.deadornotdead.myJsonListeTitle.*;

/**
 * Created by antoine.gagneux on 12/10/2017.
 */

public interface WikiService {
    @GET("api.php?action=query&format=json&list=search&srnamespace=0")
    Call<ListeTitle> getListTitle(@Query("srlimit") String limit,@Query("srsearch") String texte);
    @GET("api.php?action=query&format=json&prop=&export=1")
    Call<ResponseBody> getPage(@Query("titles") String texte);
}


//https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srnamespace=0&srlimit= 2 &srsearch= toto
// remplacer espace par + --> NON

// https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&export=1&titles=toto


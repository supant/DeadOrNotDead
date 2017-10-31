
package com.example.antoine.deadornotdead.myJsonPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Export {

    @SerializedName("toto")
    @Expose
    private String toto;

    public String getToto() {
        return toto;
    }

    public void setToto(String toto) {
        this.toto = toto;
    }

}

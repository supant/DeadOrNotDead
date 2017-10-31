
package com.example.antoine.deadornotdead.myJsonListeTitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Searchinfo {

    @SerializedName("totalhits")
    @Expose
    private int totalhits;

    public int getTotalhits() {
        return totalhits;
    }

    public void setTotalhits(int totalhits) {
        this.totalhits = totalhits;
    }

}


package com.example.antoine.deadornotdead.myJsonListeTitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Continue {

    @SerializedName("sroffset")
    @Expose
    private int sroffset;
    @SerializedName("continue")
    @Expose
    private String _continue;

    public int getSroffset() {
        return sroffset;
    }

    public void setSroffset(int sroffset) {
        this.sroffset = sroffset;
    }

    public String getContinue() {
        return _continue;
    }

    public void setContinue(String _continue) {
        this._continue = _continue;
    }

}

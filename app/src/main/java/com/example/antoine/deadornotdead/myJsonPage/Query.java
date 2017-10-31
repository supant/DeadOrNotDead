
package com.example.antoine.deadornotdead.myJsonPage;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("normalized")
    @Expose
    private List<Normalized> normalized = null;
    @SerializedName("pages")
    @Expose
    private String pages;
    @SerializedName("export")
    @Expose
    private String export;

    public List<Normalized> getNormalized() {
        return normalized;
    }

    public void setNormalized(List<Normalized> normalized) {
        this.normalized = normalized;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

}

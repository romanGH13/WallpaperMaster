package com.trehub.wallpapermaster.model.pojo.secondaryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.trehub.wallpapermaster.model.pojo.primaryList.Result;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SecondaryList extends RealmObject {

    @SerializedName("results")
    @Expose
    private RealmList<Result> results = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("dates")
    @Expose
    private Dates dates;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public RealmList<Result> getResults() {
        return results;
    }

    public void setResults(RealmList<Result> results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}

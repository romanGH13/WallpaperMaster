package com.trehub.wallpapermaster.model.pojo.primaryList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Cache extends RealmObject {

    private Integer listId;
    private RealmList<Result> results;
    private Integer page;
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public RealmList<Result> getResults() {
        return results;
    }

    public void setResults(RealmList<Result> results) {
        this.results = results;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

}

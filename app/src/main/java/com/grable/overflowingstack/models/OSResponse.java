package com.grable.overflowingstack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by timgrable on 10/31/17.
 */

public class OSResponse {

    @SerializedName("items")
    @Expose
    private List<Question> items = null;

    @SerializedName("quota_remaining")
    @Expose
    private Integer quotaRemaining;

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(Integer quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }
}

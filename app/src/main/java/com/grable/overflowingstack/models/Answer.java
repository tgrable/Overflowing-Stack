package com.grable.overflowingstack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by timgrable on 11/1/17.
 */

public class Answer {

    @SerializedName("answer_id")
    @Expose
    private long answer_id;

    @SerializedName("question_id")
    @Expose
    private long question_id;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("body_markdown")
    @Expose
    private String body_markdown;

    @SerializedName("creation_date")
    @Expose
    private String creation_date;

    @SerializedName("is_accepted")
    @Expose
    private Boolean is_accepted;

    /**
     * Empty Constructor
     */
    public Answer() {
    }

    public Answer(long answer_id, long question_id, String body, String body_markdown, String creation_date, Boolean is_accepted) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.body = body;
        this.body_markdown = body_markdown;
        this.creation_date = creation_date;
        this.is_accepted = is_accepted;
    }

    public long getAnswer_id() {
        return this.answer_id;
    }

    public void setAnswer_id(long value) {
        this.answer_id = value;
    }

    public long getQuestion_id() {
        return this.question_id;
    }

    public void setQuestion_id(long value) {
        this.question_id = value;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public String getBody_markdown() {
        return this.body_markdown;
    }

    public void setBody_markdown(String value) {
        this.body_markdown = value;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(String value) {
        this.creation_date = value;
    }

    public Boolean getIs_accepted() {
        return this.is_accepted;
    }

    public void setIs_accepted(Boolean value) {
        this.is_accepted = value;
    }

}

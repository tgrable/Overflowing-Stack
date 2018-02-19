package com.grable.overflowingstack.vone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by timgrable on 10/31/17.
 */

public class Question {

    @SerializedName("question_id")
    @Expose
    private long question_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("body_markdown")
    @Expose
    private String body_markdown;

    @SerializedName("creation_date")
    @Expose
    private String creation_date;

    @SerializedName("isGuessed")
    @Expose
    private Boolean isGuessed;

    @SerializedName("answers")
    @Expose
    private List<Answer> answers;

    // Empty constructor
    public Question() {

    }

    public Question(long question_id, String title, String body, String body_markdown, String creation_date, Boolean isGuessed, List<Answer> answers) {
        this.question_id = question_id;
        this.title = title;
        this.body = body;
        this.body_markdown = body_markdown;
        this.creation_date = creation_date;
        this.isGuessed = (isGuessed == null) ? false : isGuessed;
        this.answers = answers;
    }

    public long getQuestion_id() { return this.question_id; }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String postTitle) {
        title = postTitle;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody_markdown() {
        return this.body_markdown;
    }

    public void setBody_markdown(String body_markdown) {
        this.body_markdown = body_markdown;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Boolean getIsGuessed() {
        return this.isGuessed;
    }

    public void setIsGuessed(Boolean isGuessed) {
        this.isGuessed = isGuessed;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}

package com.grable.overflowingstack.vone.models;

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

    @SerializedName("score")
    @Expose
    private int score;

    @SerializedName("down_vote_count")
    @Expose
    private int down_vote_count;

    @SerializedName("up_vote_count")
    @Expose
    private int up_vote_count;

    /**
     * Empty Constructor
     */
    public Answer() {
    }

    public Answer(long answer_id, long question_id, String body, String body_markdown, String creation_date, Boolean is_accepted, int score, int down_vote_count, int up_vote_count) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.body = body;
        this.body_markdown = body_markdown;
        this.creation_date = creation_date;
        this.is_accepted = is_accepted;
        this.score = score;
        this.down_vote_count = down_vote_count;
        this.up_vote_count = up_vote_count;
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

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDown_vote_count() {
        return this.down_vote_count;
    }

    public void setDown_vote_count(int value) {
        this.down_vote_count = value;
    }

    public int getUp_vote_count() {
        return this.up_vote_count;
    }

    public void setUp_vote_count(int value) {
        this.up_vote_count = value;
    }

}

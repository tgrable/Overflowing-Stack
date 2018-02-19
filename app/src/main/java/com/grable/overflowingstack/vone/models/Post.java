package com.grable.overflowingstack.vone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by timgrable on 11/1/17.
 */

public class Post {

    @SerializedName("answer_id")
    @Expose
    private String title;

    @SerializedName("answer_id")
    @Expose
    private String question;

    @SerializedName("answer_id")
    @Expose
    private List<String > answers;

    /**
     * Empty Constructor
     */
    public Post() {
    }

    public Post(String title, String question, List<String> answers) {
        this.title = title;
        this.question = question;
        this.answers = answers;
    }

    /**
     * Gets the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the question
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Sets the question
     */
    public void setQuestion(String value) {
        this.question = value;
    }

    /**
     * Gets the answers
     */
    public List<String> getAnswers() {
        return this.answers;
    }

    /**
     * Sets the answers
     */
    public void setAnswers(List<String> value) {
        this.answers = value;
    }
}

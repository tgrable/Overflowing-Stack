package com.grable.overflowingstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.grable.overflowingstack.adapters.AnswersAdapter;
import com.grable.overflowingstack.adapters.QuestionsAdapter;
import com.grable.overflowingstack.models.Post;
import com.grable.overflowingstack.models.Question;
import com.grable.overflowingstack.sqlite.DatabaseHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity {

    private AnswersAdapter mAdapter;
    private ArrayList<String> mAnswersArrayList;
    private DatabaseHandler mDb;

    @BindView(R.id.question_title) TextView questionTitle;
    @BindView(R.id.question_body) TextView questionBody;
    @BindView(R.id.answerRecyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ButterKnife.bind(this);

        mDb = new DatabaseHandler(this);

        Intent i = getIntent();
        long id = i.getLongExtra("question_id", 0);
        getQuestionFromLocalDB(id);
    }

    private void getQuestionFromLocalDB(long id) {
        Post p = mDb.getFullPost(id);
        questionTitle.setText(p.getTitle());
        questionBody.setText(Html.fromHtml(p.getQuestion()));
        mAnswersArrayList = (ArrayList<String>)p.getAnswers();

        Log.d(App.TAG, String.valueOf(mAnswersArrayList.size()));

        setupAdapter();
    }

    private void setupAdapter() {
        mAdapter = new AnswersAdapter(mAnswersArrayList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(QuestionActivity.this, 1));
        mRecyclerView.setAdapter(mAdapter);
    }
}

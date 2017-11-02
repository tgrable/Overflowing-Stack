package com.grable.overflowingstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.grable.overflowingstack.adapters.QuestionsAdapter;
import com.grable.overflowingstack.interfaces.ObserverListener;
import com.grable.overflowingstack.interfaces.QuestionSelectListener;
import com.grable.overflowingstack.interfaces.RequestInterface;
import com.grable.overflowingstack.factories.ObserverFactory;
import com.grable.overflowingstack.models.Answer;
import com.grable.overflowingstack.models.OSResponse;
import com.grable.overflowingstack.models.Question;
import com.grable.overflowingstack.sqlite.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity implements ObserverListener, QuestionSelectListener {

    @BindView(R.id.titleRecyclerView) RecyclerView mRecyclerView;

    private QuestionsAdapter mAdapter;
    private ArrayList<Question> mQuestionsArrayList;
    private DatabaseHandler mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mDb = new DatabaseHandler(this);

        getQuestions();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_email:
                getAllQuestionsFromWebServer();
                break;
            default:
                break;
        }
        return true;
    }

    /*
     * Utility methods
     */

    private void getQuestions() {

        if (mDb.getQuestionsCount() > 0) {
            getAllQuestionsFromLocalDB();
        }
        else {
            getAllQuestionsFromWebServer();
        }
    }

    private void setupAdapter() {
        mAdapter = new QuestionsAdapter(mQuestionsArrayList, this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(BaseActivity.this, 1));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAllQuestionsFromLocalDB() {
        mQuestionsArrayList = (ArrayList<Question>) mDb.getAllQuestions();
        setupAdapter();
    }

    private void getAllQuestionsFromWebServer() {
        RequestInterface ri = App.retrofit.create(RequestInterface.class);
        Observable<OSResponse> observable = ri.getQuestions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        ObserverFactory.createObserverObj(observable, this);
    }

    /*
     * Interface methods
     */

    @Override
    public void observerOnSubscribe() {
        Log.i(App.TAG, "observerOnSubscribe");
    }

    @Override
    public void observerOnNext(List<?> list) {
        List<Question> tempList = (List<Question>)list;
        mQuestionsArrayList = new ArrayList<>();

        for (int i = 0; i < tempList.size(); i++) {
            Question q = tempList.get(i);
            mDb.addQuestionsOrAnswers(q, App.QUESTION_DB);
            List<Answer> answers = q.getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                mDb.addQuestionsOrAnswers(answer, App.ANSWER_DB);
            }
            mQuestionsArrayList.add(q);
        }

        setupAdapter();
    }

    @Override
    public void observerOnError(Throwable e) {
        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void observerOnComplete() {
        Log.i(App.TAG, "observerOnComplete");
    }

    @Override
    public void onQuestionCardSelected(long id) {
        Intent i = new Intent(this, QuestionActivity.class);
        i.putExtra("question_id", id);
        startActivity(i);
    }
}

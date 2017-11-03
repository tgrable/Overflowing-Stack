package com.grable.overflowingstack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grable.overflowingstack.adapters.QuestionsAdapter;
import com.grable.overflowingstack.factories.ObserverFactory;
import com.grable.overflowingstack.interfaces.ObserverListener;
import com.grable.overflowingstack.interfaces.QuestionSelectListener;
import com.grable.overflowingstack.interfaces.RequestInterface;
import com.grable.overflowingstack.models.Answer;
import com.grable.overflowingstack.models.OSResponse;
import com.grable.overflowingstack.models.Post;
import com.grable.overflowingstack.models.Question;
import com.grable.overflowingstack.sqlite.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements ObserverListener, QuestionSelectListener, SearchView.OnQueryTextListener {

    @BindView(R.id.home_search_view)
    SearchView mSearchView;

    @BindView(R.id.home_score_view)
    TextView mTextView;

    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;

    public static ArrayList<Question> mQuestionsArrayList;

    private QuestionsAdapter mAdapter;
    private DatabaseHandler mDb;
    private ProgressDialog mRingProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

        mQuestionsArrayList = new ArrayList<>();
        setupAdapter();

        mDb = new DatabaseHandler(this);

        mSearchView.setOnQueryTextListener(this);

        getQuestions();
        getuserScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getuserScore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guessed) {
            mAdapter.FilterForGuessedQuestions();

            return true;
        }
        else if (id == R.id.action_reload) {
            getAllQuestionsFromWebServer();

            return true;
        }
        else if (id == R.id.action_all) {
            mAdapter.ReloadAllQuestions();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Utility methods
     **/
    private void getQuestions() {

        if (mDb.getQuestionsCount() > 0) {
            getAllQuestionsFromLocalDB();
        }
        else {
            getAllQuestionsFromWebServer();
        }
    }

    private void setupAdapter() {
        mAdapter = new QuestionsAdapter(mQuestionsArrayList, this, this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getAllQuestionsFromLocalDB() {
        mQuestionsArrayList = (ArrayList<Question>) mDb.getAllQuestions();
        mAdapter.AddList(mQuestionsArrayList);
    }

    private void getAllQuestionsFromWebServer() {
        mRingProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name),	"Getting Questions...", true);
        mRingProgressDialog.setCancelable(true);

        RequestInterface ri = App.retrofit.create(RequestInterface.class);
        Observable<OSResponse> observable = ri.getQuestions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        ObserverFactory.createObserverObj(observable, this);
    }

    public void getuserScore() {
        mTextView.setText("Score: " + App.getUserScore());
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.FilterList(query);

        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.FilterList(newText);

        return false;
    }

    @Override
    public void onQuestionCardSelected(long id) {
        Intent i = new Intent(this, PostActivity.class);
        i.putExtra("question_id", id);
        startActivity(i);
    }

    @Override
    public void observerOnSubscribe() {
        Log.i(App.TAG, "observerOnSubscribe");
    }

    @Override
    public void observerOnNext(List<?> list) {
        mAdapter.ClearList();

        mQuestionsArrayList.clear();
        List<Question> tempList = (List<Question>)list;

        for (int i = 0; i < tempList.size(); i++) {
            Question q = tempList.get(i);
            q.setIsGuessed(false);

            mDb.addQuestionsOrAnswers(q, App.QUESTION_DB);
            List<Answer> answers = q.getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                mDb.addQuestionsOrAnswers(answer, App.ANSWER_DB);
            }
            mQuestionsArrayList.add(q);
        }

        mAdapter.AddList(mQuestionsArrayList);
        if (mRingProgressDialog != null && mRingProgressDialog.isShowing()) {
            mRingProgressDialog.dismiss();
        }
    }

    @Override
    public void observerOnError(Throwable e) {
        if (mRingProgressDialog != null && mRingProgressDialog.isShowing()) {
            mRingProgressDialog.dismiss();
        }
        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void observerOnComplete() {
        Log.i(App.TAG, "observerOnComplete");
    }
}

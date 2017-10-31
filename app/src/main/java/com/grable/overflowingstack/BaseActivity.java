package com.grable.overflowingstack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.grable.overflowingstack.adapters.DataAdapter;
import com.grable.overflowingstack.interfaces.ObserverListener;
import com.grable.overflowingstack.interfaces.RequestInterface;
import com.grable.overflowingstack.factories.ObserverFactory;
import com.grable.overflowingstack.models.OSResponse;
import com.grable.overflowingstack.models.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseActivity extends AppCompatActivity implements ObserverListener {

    @BindView(R.id.titleRecyclerView) RecyclerView mRecyclerView;
    private DataAdapter mAdapter;

    private ArrayList<Question> mAndroidArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ButterKnife.bind(this);

        getQuestions();
    }

    private void getQuestions() {
        RequestInterface ri = App.retrofit.create(RequestInterface.class);
        Observable<OSResponse> observable = ri.getQuestions()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        ObserverFactory.createObserverObj(observable, this);
    }

    @Override
    public void observerOnSubscribe() {
        Log.i(App.TAG, "observerOnSubscribe");
    }

    @Override
    public void observerOnNext(List<?> list) {
        List<Question> tempList = (List<Question>)list;
        mAndroidArrayList = new ArrayList<>();
        Log.d(App.TAG, "tempList.size(): " + tempList.size());

        for (int i = 0; i < tempList.size(); i++) {
            Question q = new Question();
            q.setTitle(tempList.get(i).getTitle());
            Log.d(App.TAG, "q.getTitle(): " + q.getTitle());
            mAndroidArrayList.add(q);
        }

        mAdapter = new DataAdapter(mAndroidArrayList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(BaseActivity.this, 1));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void observerOnError(Throwable e) {
        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void observerOnComplete() {
        Log.i(App.TAG, "observerOnComplete");
    }
}

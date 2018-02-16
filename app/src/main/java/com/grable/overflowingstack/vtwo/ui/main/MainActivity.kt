package com.grable.overflowingstack.vtwo.ui.main

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.grable.overflowingstack.R
import com.grable.overflowingstack.vtwo.application.App
import com.grable.overflowingstack.vtwo.database.AppDatabase
import com.grable.overflowingstack.vtwo.models.QuestionRaw
import com.grable.overflowingstack.vtwo.models.Question
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_home.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.MainView {
    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        (application as App).getNetworkComponent()?.inject(this)
        presenter.setView(this)
        presenter.subscribe()
    }

    override fun setPresenter() { }

    override fun addItemsToAdapter(questions: List<Question>) {
        title = ""

        val layoutManager = LinearLayoutManager(this)
        home_recycler_view.setHasFixedSize(true)
        home_recycler_view.layoutManager = layoutManager

        val fastAdapter: FastItemAdapter<Question> = FastItemAdapter()
        fastAdapter.withSelectable(true)
        fastAdapter.withOnClickListener { _, _, item, _ ->
//            TODO: click through to the the post activity
            true
        }
        home_recycler_view.adapter = fastAdapter

        fastAdapter.add(questions)
    }
}

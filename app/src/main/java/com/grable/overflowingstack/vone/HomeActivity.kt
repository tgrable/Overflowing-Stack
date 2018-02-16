package com.grable.overflowingstack.vone

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.grable.overflowingstack.vone.adapters.QuestionsAdapter
import com.grable.overflowingstack.vone.factories.ObserverFactory
import com.grable.overflowingstack.vone.interfaces.ObserverListener
import com.grable.overflowingstack.vone.interfaces.QuestionSelectListener
import com.grable.overflowingstack.vone.interfaces.RequestInterface
import com.grable.overflowingstack.vone.models.Question
import com.grable.overflowingstack.vone.sqlite.DatabaseHandler

import java.util.ArrayList

import butterknife.ButterKnife
import com.grable.overflowingstack.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity(), ObserverListener, QuestionSelectListener, SearchView.OnQueryTextListener {

    private var mAdapter: QuestionsAdapter? = null
    private var mDb: DatabaseHandler? = null
    private var mRingProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        ButterKnife.bind(this)

        mQuestionsArrayList = ArrayList()
        setupAdapter()

        mDb = DatabaseHandler(this)

        home_search_view.setOnQueryTextListener(this)

        getQuestions()
        getuserScore()
    }

    override fun onResume() {
        super.onResume()
        getuserScore()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        when (id) {
            R.id.action_guessed -> {
                mQuestionsArrayList.clear()
                getAllQuestionsFromLocalDB()
                mAdapter!!.FilterForGuessedQuestions()

                return true
            }
            R.id.action_reload -> {
                getAllQuestionsFromWebServer()

                return true
            }
            R.id.action_all -> {
                mAdapter!!.ReloadAllQuestions()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    /**
     * Utility methods
     */
    private fun getQuestions() {

        if (mDb!!.questionsCount > 0) {
            getAllQuestionsFromLocalDB()
        } else {
            getAllQuestionsFromWebServer()
        }
    }

    private fun setupAdapter() {
        mAdapter = QuestionsAdapter(mQuestionsArrayList, this, this)
        home_recycler_view?.layoutManager = GridLayoutManager(this@HomeActivity, 1)
        home_recycler_view?.adapter = mAdapter
    }

    private fun getAllQuestionsFromLocalDB() {
        mQuestionsArrayList = mDb!!.allQuestions as ArrayList<Question>
        mAdapter!!.AddList(mQuestionsArrayList)
    }

    private fun getAllQuestionsFromWebServer() {
        mRingProgressDialog = ProgressDialog.show(this, resources.getString(R.string.app_name), "Getting Questions...", true)
        mRingProgressDialog!!.setCancelable(true)

        val ri = App.retrofit.create(RequestInterface::class.java)
        val observable = ri.questions
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

        ObserverFactory.createObserverObj(observable, this)
    }

    private fun getuserScore() {
        home_score_view.text = App.getUserScore().toString()
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
    override fun onQueryTextSubmit(query: String): Boolean {
        mAdapter!!.FilterList(query)

        return false
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    override fun onQueryTextChange(newText: String): Boolean {
        mAdapter!!.FilterList(newText)

        return false
    }

    override fun onQuestionCardSelected(id: Long) {
        val i = Intent(this, PostActivity::class.java)
        i.putExtra("question_id", id)
        startActivity(i)
    }

    override fun observerOnSubscribe() {
        Log.i(App.TAG, "observerOnSubscribe")
    }

    override fun observerOnNext(list: List<*>) {
        mAdapter!!.ClearList()

        mQuestionsArrayList.clear()
        val tempList = list as List<Question>

        for (i in tempList.indices) {
            val q = tempList[i]
            q.isGuessed = false

            mDb!!.addQuestionsOrAnswers(q, App.QUESTION_DB)
            val answers = q.answers
            for (j in answers.indices) {
                val answer = answers[j]
                mDb!!.addQuestionsOrAnswers(answer, App.ANSWER_DB)
            }
            mQuestionsArrayList.add(q)
        }

        mAdapter!!.AddList(mQuestionsArrayList)
        if (mRingProgressDialog != null && mRingProgressDialog!!.isShowing) {
            mRingProgressDialog!!.dismiss()
        }
    }

    override fun observerOnError(e: Throwable) {
        if (mRingProgressDialog != null && mRingProgressDialog!!.isShowing) {
            mRingProgressDialog!!.dismiss()
        }
        Toast.makeText(applicationContext, "Error: " + e.message, Toast.LENGTH_LONG).show()
    }

    override fun observerOnComplete() {
        Log.i(App.TAG, "observerOnComplete")
    }

    companion object {
        lateinit var mQuestionsArrayList: ArrayList<Question>
    }
}

package com.grable.overflowingstack.vtwo.ui.main

import android.util.Log
import com.grable.overflowingstack.vtwo.dao.QuestionDao
import com.grable.overflowingstack.vtwo.database.AppDatabase
import com.grable.overflowingstack.vtwo.models.OSResponse
import com.grable.overflowingstack.vtwo.models.Question
import com.grable.overflowingstack.vtwo.models.QuestionRaw
import com.grable.overflowingstack.vtwo.network.RestApi
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * Created by timgrable on 2/13/18.
 */
class MainPresenter @Inject constructor(private val retrofit: Retrofit, private val database: AppDatabase) : MainContract.MainPresenter {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var mainView: MainContract.MainView

    override fun setView(mainView: MainContract.MainView) {
        this.mainView = mainView
    }

    override fun subscribe() {
        loadQuestionsFromDatabase()
        loadResponse()
    }

    private fun loadResponse() {
        val post = retrofit.create(RestApi::class.java)
        post.getResponse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<OSResponse> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(osResponse: OSResponse) {
                    compositeDisposable.add(Observable.fromCallable { addQuestionsToDatabase(osResponse.items) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                loadQuestionsFromDatabase()
                            }))
                }

                override fun onError(e: Throwable) {
                    Log.e("tgrable", "Error: ${e.message}")
                }
            })
    }

    private fun addQuestionsToDatabase(questions: List<QuestionRaw>) {
        for (question in questions) {
            database.questionItemDao().insertQuestion(question)
        }
    }

    private fun loadQuestionsFromDatabase() {
        var questions: List<Question> = arrayListOf()
        compositeDisposable.add(
                Observable.fromCallable {
                    questions = database.questionItemDao().getAll().map { Question(it.question_id, it.title, it.body, it.body_markdown, it.creation_date, it.isGuessed, null) } }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ mainView.addItemsToAdapter(questions)},
                                { Log.d("tgrable", "Error loading content") })
        )
    }

    override fun dispose() {
        compositeDisposable.clear()
    }
}
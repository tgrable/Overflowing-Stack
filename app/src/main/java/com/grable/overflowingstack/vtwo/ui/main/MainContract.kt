package com.grable.overflowingstack.vtwo.ui.main

import com.grable.overflowingstack.vtwo.BasePresenter
import com.grable.overflowingstack.vtwo.BaseView
import com.grable.overflowingstack.vtwo.models.Question

/**
 * Created by timgrable on 2/13/18.
 */
interface MainContract {
    interface MainView : BaseView<MainPresenter> {
        fun addItemsToAdapter(questions: List<Question>)
    }

    interface MainPresenter : BasePresenter {
        fun setView(mainView: MainView)
    }
}
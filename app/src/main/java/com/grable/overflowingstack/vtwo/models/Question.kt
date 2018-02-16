package com.grable.overflowingstack.vtwo.models

import android.view.View
import com.mikepenz.fastadapter.items.AbstractItem
import android.support.v7.widget.RecyclerView
import com.grable.overflowingstack.R
import kotlinx.android.synthetic.main.question_title.view.*
import com.grable.overflowingstack.vone.models.Answer

/**
 * Created by timgrable on 2/13/18.
 */

class Question(private var question_id: Long = 0,
               private var title: String? = null,
               private var body: String? = null,
               private var body_markdown: String? = null,
               private var creation_date: String? = null,
               private var isGuessed: Boolean? = null,
               private var answers: List<Answer>? = null) : AbstractItem<Question, Question.ViewHolder>() {

    override fun getType(): Int = R.id.response_holder_id
    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)
    override fun getLayoutRes(): Int = R.layout.question_title

    override fun bindView(holder: ViewHolder, payloads: List<Any>?) {
        super.bindView(holder, payloads)

        holder.itemView.post_title.text = title
        holder.itemView.post_body.text = body
    }

    override fun unbindView(holder: ViewHolder?) {
        super.unbindView(holder)

        holder?.itemView?.post_title?.text = null
        holder?.itemView?.post_body?.text = null
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
package com.grable.overflowingstack.vone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.grable.overflowingstack.vone.HomeActivity;
import com.grable.overflowingstack.R;
import com.grable.overflowingstack.vone.interfaces.QuestionSelectListener;
import com.grable.overflowingstack.vone.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timgrable on 10/30/17.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private ArrayList<Question> questionList;
    private QuestionSelectListener listener;
    private Context context;

    public QuestionsAdapter(ArrayList<Question> questionList, QuestionSelectListener listener, Context context) {
        this.questionList = questionList;
        this.listener = listener;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_title, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(QuestionsAdapter.ViewHolder holder, int position) {
        holder.mPostTitle.setText(this.questionList.get(position).getTitle());
        holder.itemView.setTag(questionList.get(position).getQuestion_id());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onQuestionCardSelected((long)view.getTag());
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.questionList.size();
    }

    /**
     * Updates the adapter with the recently downloaded list.
     *
     * @param questions List of question for the Stack Exchange API or local sql database.
     */
    public void AddList(List<Question> questions) {
        questionList.addAll(questions);
        notifyDataSetChanged();
    }

    /**
     * Filters the List using the passed in query string
     *
     * @param queryStrng String to filter the list based on question title
     */
    public void FilterList(String queryStrng) {
        ArrayList<Question> masterList = HomeActivity.Companion.getMQuestionsArrayList();
        ArrayList<Question> listToFilter = new ArrayList<>();
        for (Question q: masterList) {
            if (q.getTitle().toLowerCase().contains(queryStrng.toLowerCase())) {
                listToFilter.add(q);
            }
        }

        questionList.clear();
        questionList.addAll(listToFilter);
        notifyDataSetChanged();
    }

    /**
     * Filters the List based on question.is_guessed being true
     * then updates the list
     */
    public void FilterForGuessedQuestions() {
        ArrayList<Question> masterList = HomeActivity.Companion.getMQuestionsArrayList();
        ArrayList<Question> listToFilter = new ArrayList<>();
        for (Question q: masterList) {
            if (q.getIsGuessed()) {
                listToFilter.add(q);
            }
        }

        questionList.clear();
        questionList.addAll(listToFilter);
        notifyDataSetChanged();
    }

    /**
     * Clears the list
     */
    public void ClearList() {
        questionList.clear();
        notifyDataSetChanged();
    }

    /**
     * Reloads all the questions
     */
    public void ReloadAllQuestions() {
        questionList.clear();
        questionList.addAll(HomeActivity.Companion.getMQuestionsArrayList());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPostTitle;

        public ViewHolder(View view) {
            super(view);
            mPostTitle = (TextView)view.findViewById(R.id.post_title);
        }
    }
}

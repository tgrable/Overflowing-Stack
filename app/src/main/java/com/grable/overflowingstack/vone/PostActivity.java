package com.grable.overflowingstack.vone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grable.overflowingstack.R;
import com.grable.overflowingstack.vone.models.Answer;
import com.grable.overflowingstack.vone.models.Question;
import com.grable.overflowingstack.vone.sqlite.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity {

    @BindView(R.id.question_linear_layout)
    LinearLayout mQuestionLinearLayout;

    @BindView(R.id.question_title)
    TextView mQuestionTitle;

    @BindView(R.id.question_body)
    TextView mQuestionBody;

    private Question question;
    private DatabaseHandler mDb;
    private int score;
    private ArrayList<TextView> textViews;
    private int correctAnswerIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);

        mDb = new DatabaseHandler(this);

        textViews = new ArrayList<>();

        Intent i = getIntent();
        long id = i.getLongExtra("question_id", 0);

        score = App.getUserScore();

        getQuestionFromLocalDB(id);
        getAnswersFromLocalDB(mDb.getAnswersForQuestion(id));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getQuestionFromLocalDB(long id) {
        question = mDb.getQuestion(id);
        Log.d(App.TAG, question.getTitle());

        mQuestionTitle.setText(question.getTitle());
        mQuestionBody.setText(Html.fromHtml(question.getBody()));
    }

    private void getAnswersFromLocalDB(final List<Answer> answerList) {
        int count = 0;

        for (Answer a: answerList) {

            if (a.getIs_accepted()) {
                correctAnswerIndex = count;
            }

            View v = new View(this);
            v.setMinimumHeight(10);
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            mQuestionLinearLayout.addView(v);
            App.setMargins(v, 50, 25, 25, 10);

            TextView textView = new TextView(this);
            textView.setText(Html.fromHtml(a.getBody()));
            textView.setTag(count);
            mQuestionLinearLayout.addView(textView);
            App.setMargins(textView, 50, 25, 25, 25);

            textViews.add(textView);

            count++;

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!question.getIsGuessed()) {
                        question.setIsGuessed(true);
                        mDb.updateQuestion(question);
                    }

                    TextView tv = textViews.get(correctAnswerIndex);
                    tv.setBackgroundColor(getResources().getColor(R.color.colorCorrectLight));

                    Answer a = answerList.get((Integer) view.getTag());
                    if (a.getIs_accepted()) {
                        score = score + (a.getUp_vote_count() - a.getDown_vote_count());
                        App.setUserScore(score);
                        displayAlertDialog("Nice work! \n\n Current Score: "  + score);
                    }
                    else {
                        score = score - (a.getUp_vote_count() - a.getDown_vote_count());
                        App.setUserScore(score);
                        displayAlertDialog("Keep Trying! \n\n Current Score: " + score);
                    }
                }
            });
        }
    }

    private void displayAlertDialog(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Overflowing Stack");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private String getStringMessage(int score, boolean correct) {
        String msg;

        if (score < 0) {
            String p = (score == -1) ? "point" : "points";
            msg = "You lost " + score + " " + p + " on that one.";
        }
        else if (score == 0) {
            msg = (correct == true) ? "Unfortunately that answer wasn't worth any points." : "The good news is that answer wasn't worth any points.";
        }
        else if (score > 0) {
            String p = (score == 1) ? "point" : "points";
            msg = "You earned " + score + " " + p + " on that one.";
        }
        else {
            msg = "";
        }

        return msg;
    }
}

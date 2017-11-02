package com.grable.overflowingstack.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.grable.overflowingstack.App;
import com.grable.overflowingstack.models.Answer;
import com.grable.overflowingstack.models.Post;
import com.grable.overflowingstack.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timgrable on 10/31/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "questionsManager";

    // Contacts table name
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ANSWERS = "answers";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_BODY_MARKDOWN = "body_markdown";
    private static final String KEY_CREATION_DATE = "creation_date";
    private static final String KEY_IS_GUESSED = "isGuessed";

    private static final String KEY_ANSWER_ID = "id";
    private static final String KEY_ANSWER_QUESTION_ID = "question_id";
    private static final String KEY_ANSWER_BODY = "body";
    private static final String KEY_ANSWER_BODY_MARKDOWN = "body_markdown";
    private static final String KEY_ANSWER_CREATION_DATE = "creation_date";
    private static final String KEY_ANSWER_IS_ACCEPTED = "is_accepted";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_BODY +
                " TEXT," + KEY_BODY_MARKDOWN + " TEXT," + KEY_CREATION_DATE +
                " TEXT," + KEY_IS_GUESSED + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "(" +
                KEY_ANSWER_ID + " INTEGER PRIMARY KEY," + KEY_ANSWER_QUESTION_ID + " INTEGER," + KEY_ANSWER_BODY +
                " TEXT," + KEY_ANSWER_BODY_MARKDOWN + " TEXT," + KEY_ANSWER_CREATION_DATE +
                " TEXT," + KEY_ANSWER_IS_ACCEPTED + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_ANSWERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new contact
    public <T> void addQuestionsOrAnswers(T T, String dbType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (dbType == App.QUESTION_DB) {
            Question question = (Question)T;

            long id = (Long.valueOf(question.getQuestion_id()) == null) ? 0 : question.getQuestion_id();
            values.put(KEY_ID, id); // Question Id

            String title = (question.getTitle() == null) ? "" : question.getTitle();
            values.put(KEY_TITLE, title); // Question Title

            String body = (question.getBody() == null) ? "" : question.getBody();
            values.put(KEY_BODY, body); // Question Body

            String bodyMarkdown = (question.getBody_markdown() == null) ? "" : question.getBody_markdown();
            values.put(KEY_BODY_MARKDOWN, bodyMarkdown); // Question Body Markdown

            String creationDate = (question.getCreation_date() == null) ? "" : question.getCreation_date();
            values.put(KEY_CREATION_DATE, creationDate); // Question Creation Date

            Boolean isGuessed = (question.getIsGuessed() == null) ? false : question.getIsGuessed();
            values.put(KEY_IS_GUESSED, isGuessed.toString()); // Question Is Guessed

            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
        }
        else {
            Answer answer = (Answer)T;

            long id = (Long.valueOf(answer.getAnswer_id()) == null) ? 0 : answer.getAnswer_id();
            values.put(KEY_ANSWER_ID, id); // Answer Id

            long questionId = (Long.valueOf(answer.getQuestion_id()) == null) ? 0 : answer.getQuestion_id();
            values.put(KEY_ANSWER_QUESTION_ID, questionId); // Answer Question Id

            String body = (answer.getBody() == null) ? "" : answer.getBody();
            values.put(KEY_ANSWER_BODY, body); // Answer Body

            String bodyMarkdown = (answer.getBody_markdown() == null) ? "" : answer.getBody_markdown();
            values.put(KEY_ANSWER_BODY_MARKDOWN, bodyMarkdown); // Answer Body Markdown

            String creationDate = (answer.getCreation_date() == null) ? "" : answer.getCreation_date();
            values.put(KEY_ANSWER_CREATION_DATE, creationDate); // Answer Creation Date

            Boolean isAccepted = (answer.getIs_accepted() == null) ? false : answer.getIs_accepted();
            values.put(KEY_ANSWER_IS_ACCEPTED, isAccepted.toString()); // Answer Is Accepted

            // Inserting Row
            db.insert(TABLE_ANSWERS, null, values);
        }

        db.close(); // Closing database connection
    }

    public Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { KEY_ID, KEY_TITLE, KEY_BODY, KEY_BODY_MARKDOWN, KEY_CREATION_DATE, KEY_IS_GUESSED}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Question question = new Question(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Boolean.parseBoolean(cursor.getString(5)),
                null
        );

        db.close(); // Closing database connection

        return question;
    }

    public Post getFullPost(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String pq = "SELECT " + TABLE_QUESTIONS + "." + KEY_TITLE + ", " +
                TABLE_QUESTIONS + "." + KEY_BODY + ", " +
                TABLE_ANSWERS + "." + KEY_ANSWER_BODY + " FROM " +
                TABLE_QUESTIONS + " INNER JOIN " +
                TABLE_ANSWERS + " ON " + TABLE_QUESTIONS + ".id = " +
                TABLE_ANSWERS + ".question_id WHERE " + TABLE_ANSWERS + ".question_id=?";

        Log.d(App.TAG, "pq: " + pq);

        Cursor cursor = db.rawQuery(pq, new String[]{String.valueOf(id)});

        Post p = new Post();

        if (cursor.moveToFirst()) {

            ArrayList<String> answers = new ArrayList<>();

            p.setTitle(cursor.getString(0));
            p.setQuestion(cursor.getString(1));

            do {
                answers.add(cursor.getString(2));

            } while (cursor.moveToNext());

            p.setAnswers(answers);
        }

        db.close(); // Closing database connection

        return p;
    }

    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<Question>();

        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Question q = new Question();
                q.setQuestion_id(Integer.parseInt(cursor.getString(0)));
                q.setTitle(cursor.getString(1));
                q.setBody(cursor.getString(2));
                q.setBody_markdown(cursor.getString(3));
                q.setCreation_date(cursor.getString(4));
                q.setIsGuessed(Boolean.parseBoolean(cursor.getString(5)));

                questionList.add(q);

            } while (cursor.moveToNext());
        }

        return questionList;
    }

    public int getQuestionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, question.getQuestion_id()); // Question Id
        values.put(KEY_TITLE, question.getTitle()); // Question Title
        values.put(KEY_BODY, question.getBody()); // Question Body
        values.put(KEY_BODY_MARKDOWN, question.getBody_markdown()); // Question Body Markdown
        values.put(KEY_CREATION_DATE, question.getCreation_date()); // Question Creation Date
        values.put(KEY_IS_GUESSED, question.getIsGuessed().toString()); // Question Is Quessed

        // updating row
        return db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getQuestion_id()) });
    }

    public void deleteQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getQuestion_id()) });
        db.close();
    }
}

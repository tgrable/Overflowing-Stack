package com.grable.overflowingstack.vone.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grable.overflowingstack.vone.models.Answer;
import com.grable.overflowingstack.vone.models.Post;
import com.grable.overflowingstack.vone.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timgrable on 10/31/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String QUESTION_DB = "question";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "questionsManager";

    // table name
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_ANSWERS = "answers";

    // Table Columns names
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
    private static final String KEY_ANSWER_SCORE = "score";
    private static final String KEY_ANSWER_DOWN_VOTE = "down_vote_count";
    private static final String KEY_ANSWER_UP_VOTE= "up_vote_count";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the sqlite database
     *
     * @param sqLiteDatabase SQLiteDatabase object.
     */
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
                " TEXT," + KEY_ANSWER_IS_ACCEPTED + " TEXT," + KEY_ANSWER_SCORE + " INTEGER,"
                + KEY_ANSWER_DOWN_VOTE + " INTEGER," + KEY_ANSWER_UP_VOTE + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_ANSWERS_TABLE);
    }

    /**
     * Upgrades the sqlite database
     *
     * @param int i
     * @param int i1
     */
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

    /**
     * Add a queston or answer the sqlite database
     *
     * @param T T Generic class to hold either question or answer
     * @param String dbType
     */
    public <T> void addQuestionsOrAnswers(T T, String dbType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (dbType.equals(QUESTION_DB)) {
            Question question = (Question)T;

            long id = (Long.valueOf(question.getQuestion_id()) == null) ? 0 : question.getQuestion_id();
            values.put(KEY_ID, id); // QuestionRaw Id

            String title = (question.getTitle() == null) ? "" : question.getTitle();
            values.put(KEY_TITLE, title); // QuestionRaw Title

            String body = (question.getBody() == null) ? "" : question.getBody();
            values.put(KEY_BODY, body); // QuestionRaw Body

            String bodyMarkdown = (question.getBody_markdown() == null) ? "" : question.getBody_markdown();
            values.put(KEY_BODY_MARKDOWN, bodyMarkdown); // QuestionRaw Body Markdown

            String creationDate = (question.getCreation_date() == null) ? "" : question.getCreation_date();
            values.put(KEY_CREATION_DATE, creationDate); // QuestionRaw Creation Date

            Boolean isGuessed = (question.getIsGuessed() == null) ? false : question.getIsGuessed();
            values.put(KEY_IS_GUESSED, isGuessed.toString()); // QuestionRaw Is Guessed

            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
        }
        else {
            Answer answer = (Answer)T;

            long id = (Long.valueOf(answer.getAnswer_id()) == null) ? 0 : answer.getAnswer_id();
            values.put(KEY_ANSWER_ID, id); // Answer Id

            long questionId = (Long.valueOf(answer.getQuestion_id()) == null) ? 0 : answer.getQuestion_id();
            values.put(KEY_ANSWER_QUESTION_ID, questionId); // Answer QuestionRaw Id

            String body = (answer.getBody() == null) ? "" : answer.getBody();
            values.put(KEY_ANSWER_BODY, body); // Answer Body

            String bodyMarkdown = (answer.getBody_markdown() == null) ? "" : answer.getBody_markdown();
            values.put(KEY_ANSWER_BODY_MARKDOWN, bodyMarkdown); // Answer Body Markdown

            String creationDate = (answer.getCreation_date() == null) ? "" : answer.getCreation_date();
            values.put(KEY_ANSWER_CREATION_DATE, creationDate); // Answer Creation Date

            Boolean isAccepted = (answer.getIs_accepted() == null) ? false : answer.getIs_accepted();
            values.put(KEY_ANSWER_IS_ACCEPTED, isAccepted.toString()); // Answer Is Accepted

            int score = (Integer.valueOf(answer.getScore()) == null) ? 0 : answer.getScore();
            values.put(KEY_ANSWER_SCORE, score); // Answer Score

            int downVote = (Integer.valueOf(answer.getDown_vote_count()) == null) ? 0 : answer.getDown_vote_count();
            values.put(KEY_ANSWER_DOWN_VOTE, downVote); // Answer downVote

            int upVote = (Integer.valueOf(answer.getUp_vote_count()) == null) ? 0 : answer.getUp_vote_count();
            values.put(KEY_ANSWER_UP_VOTE, upVote); // Answer upVote

            // Inserting Row
            db.insert(TABLE_ANSWERS, null, values);
        }

        db.close(); // Closing database connection
    }

    /**
     * Get a queston or answer the sqlite database
     *
     * @param long id of the question to return
     *
     * @return QuestionRaw object from local database
     */
    public Question getQuestion(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { KEY_ID, KEY_TITLE, KEY_BODY, KEY_BODY_MARKDOWN, KEY_CREATION_DATE, KEY_IS_GUESSED}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null , null);

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

    /**
     * Get a list questons or answers the sqlite database
     *
     * @param long id of the answers to return
     *
     * @return List of Answer objects from local database
     */
    public List<Answer> getAnswersForQuestion(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Answer> answerList = new ArrayList<Answer>();

        String selectQuery = "SELECT * FROM " + TABLE_ANSWERS + " WHERE " + KEY_ANSWER_QUESTION_ID + "=? ORDER BY " + KEY_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                Answer a = new Answer();
                a.setAnswer_id(Integer.parseInt(cursor.getString(0)));
                a.setQuestion_id(Integer.parseInt(cursor.getString(1)));
                a.setBody(cursor.getString(2));
                a.setBody_markdown(cursor.getString(3));
                a.setCreation_date(cursor.getString(4));
                a.setIs_accepted(Boolean.parseBoolean(cursor.getString(5)));
                a.setScore(Integer.parseInt(cursor.getString(6)));
                a.setDown_vote_count(Integer.parseInt(cursor.getString(7)));
                a.setUp_vote_count(Integer.parseInt(cursor.getString(8)));
                answerList.add(a);

            } while (cursor.moveToNext());
        }

        return answerList;
    }

    /**
     * Get a post sqlite database
     *
     * This was a failed attempt at using a sqlite JOIN call to save on
     * database calls
     *
     * @param long id of the question to return
     *
     * @return Post object from local database
     */
    public Post getFullPost(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String pq = "SELECT " + TABLE_QUESTIONS + "." + KEY_TITLE + ", " +
                TABLE_QUESTIONS + "." + KEY_BODY + ", " +
                TABLE_ANSWERS + "." + KEY_ANSWER_BODY + " FROM " +
                TABLE_QUESTIONS + " INNER JOIN " +
                TABLE_ANSWERS + " ON " + TABLE_QUESTIONS + ".id = " +
                TABLE_ANSWERS + ".question_id WHERE " + TABLE_ANSWERS + ".question_id=?";

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

    /**
     * Get a questions from the sqlite database
     *
     * @return List of QuestionRaw objects from local database
     */
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<Question>();

        String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS + " ORDER BY " + KEY_ID + " DESC";
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

    /**
     * Get a count of questions from the sqlite database
     *
     * @return int number of questions
     */
    public int getQuestionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

    /**
     * Get a count of questions from the sqlite database
     *
     * @param Question object to update
     */
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, question.getQuestion_id()); // QuestionRaw Id
        values.put(KEY_TITLE, question.getTitle()); // QuestionRaw Title
        values.put(KEY_BODY, question.getBody()); // QuestionRaw Body
        values.put(KEY_BODY_MARKDOWN, question.getBody_markdown()); // QuestionRaw Body Markdown
        values.put(KEY_CREATION_DATE, question.getCreation_date()); // QuestionRaw Creation Date
        values.put(KEY_IS_GUESSED, question.getIsGuessed().toString()); // QuestionRaw Is Quessed

        // updating row
        return db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getQuestion_id()) });
    }

    /**
     * Delete a questions from the sqlite database
     *
     * @param Question object to update
     */
    public void deleteQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getQuestion_id()) });
        db.close();
    }
}

package com.wiss.thom.wiredmobpro.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;


import com.wiss.thom.wiredmobpro.adapter.BitmapArrayHelper;
import com.wiss.thom.wiredmobpro.adapter.DatabaseWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 15.04.2015.
 */
public class PostORM {

    private static final String TAG = "PostORM";

    public static final String TABLE_NAME = "post";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY";
    public static final String COLUMN_ID = "_id";

    private static final String COLUMN_TIMESTAMP_TYPE = "TEXT";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String COLUMN_POSTEDDATE_TYPE = "TEXT";
    private static final String COLUMN_POSTEDDATE = "posteddate";

    private static final String COLUMN_PHOTOGRAPHER_TYPE = "TEXT";
    private static final String COLUMN_PHOTOGRAPHER = "photographer";

    private static final String COLUMN_MAIN_TYPE = "TEXT";
    private static final String COLUMN_MAIN = "main";

    private static final String COLUMN_AUTHOR_TYPE = "TEXT";
    private static final String COLUMN_AUTHOR = "author";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_PREVIEW_TYPE = "TEXT";
    private static final String COLUMN_PREVIEW = "preview";

    private static final String COLUMN_BODY_TYPE = "TEXT";
    private static final String COLUMN_BODY = "body";

    private static final String COLUMN_URL_TYPE = "TEXT";
    private static final String COLUMN_URL = "url";

    private static final String COLUMN_IMAGE_TYPE = "IMAGE";
    private static final String COLUMN_IMAGE = "image";




    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEP +
                    COLUMN_POSTEDDATE + " " + COLUMN_POSTEDDATE_TYPE + COMMA_SEP +
                    COLUMN_TIMESTAMP + " " + COLUMN_TIMESTAMP_TYPE + COMMA_SEP +
                    COLUMN_TITLE  + " " + COLUMN_TITLE_TYPE + COMMA_SEP +
                    COLUMN_PREVIEW + " " + COLUMN_PREVIEW_TYPE + COMMA_SEP +
                    COLUMN_BODY + " " + COLUMN_BODY_TYPE + COMMA_SEP +
                    COLUMN_PHOTOGRAPHER + " " + COLUMN_PHOTOGRAPHER_TYPE + COMMA_SEP +
                    COLUMN_URL + " " + COLUMN_URL_TYPE + COMMA_SEP +
                    COLUMN_AUTHOR + " " + COLUMN_AUTHOR_TYPE + COMMA_SEP +
                    COLUMN_MAIN + " " + COLUMN_MAIN_TYPE + COMMA_SEP +
                    COLUMN_IMAGE + " " + COLUMN_IMAGE_TYPE + ")";



    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    /**
     * Fetches a single Post identified by the specified ID
     * @param context
     * @param link
     * @return
     */
    public static synchronized Post findPostByLink(Context context, String link) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Post post = null;
        if(database != null) {
            Log.i(TAG, "Loading Post in 'findPostByLink' [" + link + "] ...");
            Cursor cursor = database.rawQuery("SELECT * FROM " + PostORM.TABLE_NAME + " WHERE " + PostORM.COLUMN_URL + " = "
                    + "'" + link + "'", null);

            if(cursor.getCount() > 0 ) {
                cursor.moveToFirst();
                post = cursorToPost(cursor);
                Log.i(TAG, "Post in 'findPostByLink' successfully loaded!");
            }
        }

        return post;
    }


    /**
     * Inserts a Post object into the local database
     * @param context
     * @param post
     * @return
     */
    public static boolean insertPost(Context context, Post post) {

        boolean success;
        if(findPostByLink(context, post.getUrl()) != null) {
            Log.i(TAG, "Post already exists in database, not inserting!");
            success = false;
        }else {
            ContentValues values = postToContentValues(post);

            DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
            SQLiteDatabase database = databaseWrapper.getWritableDatabase();

            success = false;
            try {
                if (database != null) {
                    long postId = database.insert(PostORM.TABLE_NAME, "null", values);
                    Log.i(TAG, "Inserted new Post with ID: " + postId);
                    success = true;
                }
            } catch (NullPointerException ex) {
                Log.e(TAG, "Failed to insert Post[" + post.getTitle() + "] due to: " + ex);
            }
        }
        return success;
    }


    /**
     * Inserts a Post object into the local database
     * @param context
     * @param post
     * @return
     */
    public static boolean insertUpdateOfPost(Context context, Post post) {

        boolean success;
        if(findPostByLink(context, post.getUrl()) != null) {
            Log.i(TAG, "Post already exists in database, not inserting!");
            success = false;
        }else {
            ContentValues values = postToContentValues(post);

            DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
            SQLiteDatabase database = databaseWrapper.getWritableDatabase();

            success = false;
            try {
                if (database != null) {
                    long postId = database.insert(PostORM.TABLE_NAME, "null", values);
                    Log.i(TAG, "Inserted new Post with ID: " + postId);
                    success = true;
                }
            } catch (NullPointerException ex) {
                Log.e(TAG, "Failed to insert Post[" + post.getTitle() + "] due to: " + ex);
            }
        }
        return success;
    }

    public static boolean updatePost(Context context, Post post) {
        ContentValues values = postToContentValues(post);
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        boolean success = false;
        try {
            if (database != null) {
                Log.i(TAG, "Updating Post[" + post.getTitle() + "]...");
                database.update(PostORM.TABLE_NAME, values, PostORM.COLUMN_URL + " = " + "'" + post.getUrl() + "'", null);
                success = true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Failed to update Post[" + post.getTitle() + "] due to: " + ex);
        }

        return success;
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostORM.COLUMN_TIMESTAMP, dateFormat.format(post.getTimestamp()));
        values.put(PostORM.COLUMN_TITLE, post.getTitle());
        values.put(PostORM.COLUMN_PREVIEW, post.getPreview());
        values.put(PostORM.COLUMN_BODY, post.getBody());
        values.put(PostORM.COLUMN_URL, post.getUrl());
        values.put(PostORM.COLUMN_MAIN, post.getMain());
        values.put(PostORM.COLUMN_PHOTOGRAPHER, post.getPhotographer());
        values.put(PostORM.COLUMN_AUTHOR, post.getAuthor());
        values.put(PostORM.COLUMN_POSTEDDATE, post.getPostedDate());
        values.put(PostORM.COLUMN_IMAGE, BitmapArrayHelper.getBytesFromBitmap(post.getImage()));
        Log.d(TAG, "Title of post in postToContentValues: " + post.getTitle());
        return values;
    }

    public static void deleteDatabase(Context context){
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    public synchronized static List<Post> getAllPosts(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + PostORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<Post> postList = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Post post = cursorToPost(cursor);
                postList.add(post);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }
        return postList;
    }

    public synchronized static List<Post> getAllPostsByCategory(Context context,String category) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + PostORM.TABLE_NAME + " WHERE " + PostORM.COLUMN_BODY + " = "
                + "'" + category + "'", null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        List<Post> postList = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Post post = cursorToPost(cursor);
                postList.add(post);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts of category " + category + " loaded successfully.");
        }


        return postList;
    }


    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Post cursorToPost(Cursor cursor) {
        Post post = new Post();
        post.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        post.setPreview(cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW)));
        post.setBody(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
        post.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
        post.setPhotographer(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTOGRAPHER)));
        post.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
        post.setPostedDate(cursor.getString(cursor.getColumnIndex(COLUMN_POSTEDDATE)));
        post.setMain(cursor.getString(cursor.getColumnIndex(COLUMN_MAIN)));
        Bitmap bitmap = BitmapArrayHelper.BytesToBitmap(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
        post.setImage(bitmap);
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
        try {
            post.setTimestamp(dateFormat.parse(date));
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse date " + date + " for Post " + post.getTitle());
            post.setTimestamp(null);
        }

        return post;
    }



}

package com.wiss.thom.wiredmobpro.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wiss.thom.wiredmobpro.R;
import com.wiss.thom.wiredmobpro.model.Categories;
import com.wiss.thom.wiredmobpro.model.Post;
import com.wiss.thom.wiredmobpro.model.PostORM;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

public class SplashScreenActivity extends Activity {


    private static final String TAG = "SplashScreenActivity";
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load_activity);

        URL businessUrl = null;
        URL seUrl = null;

        try {
            businessUrl = new URL(Categories.businessURL);
            seUrl = new URL(Categories.securityURL);
        } catch (MalformedURLException e) {
            Log.i(TAG, "Failure in URL: " + e.getMessage());
        }

        new PreFetchTask(this, businessUrl, Categories.business).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new PreFetchTask(this, seUrl, Categories.security).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void startMainActivity(){
        if(counter <= 1){
            Log.i(TAG, "Wait for all PreFetchTasks to end.. ");
        }else{
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }
    }


    private class PreFetchTask extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "ConnectionTask";
        private Activity context;
        private URL url;
        private String category;


        public PreFetchTask(Activity context, URL url, String category) {
            this.context = context;
            this.url = url;
            this.category = category;
        }


        @Override
        protected Void doInBackground(Void... urls) {


            String container = "main#grid ul li";
            Log.d(TAG, "loading list in doInBackground: ");

            try {

                Elements allElements = getListOfElement(url, container);
                for (Element element : allElements) {
                    if (element.attr("role").equals("listitem")) {
                        break;
                    }
                    Post post = new Post();
                    Elements linkElmt = element.getElementsByTag("a");
                    String detailedURL = linkElmt.attr("href");
                    post.setUrl(detailedURL);  // linkToArticle
                    post.setTitle(element.getElementsByTag("h2").text());  // title
                    post.setPreview(element.getElementsByTag("p").text());   // preview text
                    Elements imageElement = element.getElementsByTag("img");
                    String relSrc = imageElement.attr("data-lazy-src");
                    InputStream inputStream = openHttpConnection(new URL(relSrc));
                    post.setImage(BitmapFactory.decodeStream(inputStream));   // image
                    post.setBody(category);
                    post.setTimestamp(new Date());
                    PostORM.insertPost(context, post);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            counter++;
            startMainActivity();

        }


        //   ------------------------ Helper Methods  --------------------------------------------


        private Elements getListOfElement(URL url, String selectedItem) throws IOException, URISyntaxException {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url.toURI());
            HttpResponse resp = client.execute(get);
            String content = EntityUtils.toString(resp.getEntity());
            Document doc = Jsoup.parse(content);
            Elements masthead = doc.select(selectedItem);
            return masthead;
        }

        private InputStream openHttpConnection(URL url) {
            InputStream in = null;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                in = httpURLConnection.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return in;
        }

    }
}

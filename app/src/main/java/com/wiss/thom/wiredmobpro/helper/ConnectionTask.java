package com.wiss.thom.wiredmobpro.helper;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Thomas on 18.04.2015.
 */
public class ConnectionTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "ConnectionTask";
    private Activity context;
    private URL url;
    private String category;



    public ConnectionTask(Activity context, URL url, String category){
        this.context = context;
        this.url = url;
        this.category = category;
    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected Void doInBackground(Void... urls) {


            String container = "main#grid ul li";
            Log.d(TAG, "loading list in doInBackground: ");

            try {

                Elements allElements = getListOfElement(url,container);
                for(Element element : allElements){
                    if(element.attr("role").equals("listitem") || element.attr("itemtype").equals("http://schema.org/Article")){
                        break;
                    }

                    Elements linkElmt = element.getElementsByTag("a");
                    String detailedURL = linkElmt.attr("href");

                    try {
                        if (PostORM.findPostByLink(context, detailedURL).getUrl() == null ||
                                detailedURL.equals(PostORM.findPostByLink(context, detailedURL).getUrl())) {
                            Log.d(TAG, "Post is already in database");
                            continue;
                        }
                    }catch(NullPointerException e){
                        Log.d(TAG, "Post is empty -> continue");
                    }

                    Post post = new Post();
                    post.setUrl(detailedURL);  // linkToArticle
                    post.setTitle(element.getElementsByTag("h2").text());  // title
                    post.setPreview(element.getElementsByTag("p").text());   // preview text
                    Elements timeElements = (element.getElementsByTag("time"));
                    post.setPostedDate(timeElements.attr("pubdate"));  // posted date
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
    protected void onPostExecute(Void result){


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

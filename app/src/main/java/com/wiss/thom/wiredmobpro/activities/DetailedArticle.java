package com.wiss.thom.wiredmobpro.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiss.thom.wiredmobpro.R;
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


public class DetailedArticle extends Activity {

    private static final String TAG = "DetailedArticle";

    private String incomingLink;
    private Post post;
    private Post toUpdatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toUpdatePost = new Post();

        incomingLink = getIntent().getStringExtra("link");
        Log.i(TAG, "Link: " + incomingLink);

        post = PostORM.findPostByLink(getBaseContext(), incomingLink);

        if(post.getMain() == null){     // Fetch missing data
            try {
                new ConnectionDetailedTask(new URL(incomingLink)).execute();
            } catch (MalformedURLException e) {
                Log.e(TAG, "broken URL");
                e.printStackTrace();
            }
        }else{
            showContentOnScreen();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickLinkToWebsite(View v){
        // Link zur Website => incomingLink
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.setData(Uri.parse(incomingLink));
        startActivity(in);
    }


    private void showContentOnScreen(){

        setContentView(R.layout.activity_detailed_article);

        TextView mainTextView = (TextView) findViewById(R.id.detailed_textViewMain);
        TextView photographerTextView = (TextView) findViewById(R.id.detailed_textViewPhotographer);
        TextView authorTextView = (TextView) findViewById(R.id.detailed_textViewAuthor);
        TextView titleTextView = (TextView) findViewById(R.id.detailed_textViewTitle);
        TextView dateTextView = (TextView) findViewById(R.id.detailed_textViewPostedDate);
        TextView categoryTextView = (TextView) findViewById(R.id.detailed_textViewCategory);
        ImageView imageView = (ImageView) findViewById(R.id.detailed_imageView);

        Post postUI = PostORM.findPostByLink(getBaseContext(), incomingLink);

        if(postUI.getPhotographer() == null || postUI.getPhotographer().length() > 70){
            photographerTextView.setText("© by Condé Nast");
        }else{
            photographerTextView.setText("© by Condé Nast and " + postUI.getPhotographer());
        }
        categoryTextView.setText(postUI.getBody().toUpperCase());
        titleTextView.setText(postUI.getTitle().toUpperCase());
        imageView.setImageBitmap(postUI.getImage());
        mainTextView.setText(postUI.getMain());
        dateTextView.setText(postUI.getPostedDate());
        authorTextView.setText(postUI.getAuthor());
    }


    // -------------------------- Innere Klasse --------------------------------

    private class ConnectionDetailedTask extends AsyncTask<Void, Void, Void> {

        private static final String TAG = "ConnectionDetailedTask";
        private URL url;
        private Post postInside;

        public ConnectionDetailedTask(URL url){
            this.url = url;
        }

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... urls) {


            String containerDetailed = "header#post-header";
            String containerMain = "article#start-of-content";
            StringBuilder stringBuilder = new StringBuilder();

            try {

                postInside = new Post();
                //----- Fetch header and Main in article website ------
                Element detailedElement = getFirstHtmlElement(url,containerDetailed);
                postInside.setAuthor(detailedElement.getElementsByTag("span").first().text());  // author
                postInside.setPhotographer(detailedElement.getElementsByTag("span").toggleClass("credit").text());  //photographer
                Element mainElement = getFirstHtmlElement(url,containerMain); //main
                Elements linkDetailedElmt = detailedElement.getElementsByTag("time");
                postInside.setPostedDate(linkDetailedElmt.attr("pubdate"));  // posted date

                if(mainElement.text() == null){
                    postInside.setMain(post.getPreview() + "...");  // no useful text, show preview
                }else {
                    Elements pElements = mainElement.getElementsByTag("p");
                    for(Element p : pElements){
                        stringBuilder.append("\n");
                        stringBuilder.append(p.text());
                        stringBuilder.append("\n");
                    }
                    postInside.setMain(stringBuilder.toString());
                }
                //-----------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void non){

            if(post.getPostedDate() == null){
                toUpdatePost.setPostedDate(postInside.getPostedDate());
            }else{
                toUpdatePost.setPostedDate(post.getPostedDate());
            }
            toUpdatePost.setTimestamp(post.getTimestamp());
            toUpdatePost.setImage(post.getImage());
            toUpdatePost.setTitle(post.getTitle());
            toUpdatePost.setBody(post.getBody());
            toUpdatePost.setPreview(post.getPreview());
            toUpdatePost.setUrl(post.getUrl());
            toUpdatePost.setAuthor(postInside.getAuthor());
            toUpdatePost.setPhotographer(postInside.getPhotographer());
            toUpdatePost.setMain(postInside.getMain());

            PostORM.updatePost(getBaseContext(), toUpdatePost);


             showContentOnScreen();


        }



        //   ------------------------ Helper Methods  --------------------------------------------


        private Element getFirstHtmlElement(URL url, String selectedItem) throws IOException, URISyntaxException {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url.toURI());
            HttpResponse resp = client.execute(get);
            String content = EntityUtils.toString(resp.getEntity());
            Document doc = Jsoup.parse(content);
            org.jsoup.nodes.Element masthead = doc.select(selectedItem).first();
            return masthead;
        }

    }




}

package com.wiss.thom.wiredmobpro.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Thomas on 15.04.2015.
 */
public class Post implements Serializable, Comparable<Post>{



        private String title;
        private String preview;
        private String body;
        private Bitmap image;
        private String url;
        private Date timestamp;
        private String postedDate;
        private String photographer;
        private String main;
        private String author;

        public Post(){
        }


        @Override
        public int compareTo(Post post) {
            if(this == post)
                return 0;
            if (getPostedDate() == null || post.getPostedDate() == null)
                return 0;
            return post.getPostedDate().compareTo(getPostedDate());
        }

        public String getPostedDate() {
            return postedDate;
        }

        public void setPostedDate(String postedDate) {
            this.postedDate = postedDate;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getTimestamp() {
                return timestamp;
            }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }
}

package com.example.toptop.Models;

public class MediaObject {

    public MediaObject(){

    }
    private  String title,description,datePost,post_categories,post_id,view,media_url,thumbnail,user_id;
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MediaObject(String title, String description, String datePost, String post_categories, String post_id, String view, String media_url, String thumbnail) {
        this.title = title;
        this.description = description;
        this.datePost = datePost;
        this.post_categories = post_categories;
        this.post_id = post_id;
        this.view = view;
        this.media_url = media_url;
        this.thumbnail = thumbnail;
    }



    public MediaObject(String title, String description, String datePost, String post_categories, String post_id, String media_url) {
        this.title = title;
        this.description = description;
        this.datePost = datePost;
        this.post_categories = post_categories;
        this.post_id = post_id;
        this.media_url = media_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getPost_categories() {
        return post_categories;
    }

    public void setPost_categories(String post_categories) {
        this.post_categories = post_categories;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

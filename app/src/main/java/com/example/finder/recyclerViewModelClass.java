package com.example.finder;

public class recyclerViewModelClass {

    String title,picture,description,postKey;

    public recyclerViewModelClass(){

    }

    public recyclerViewModelClass(String title, String picture, String description,String postKey) {
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.postKey = postKey;


    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String tittle) {
        this.title = tittle;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

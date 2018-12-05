package com.example.shangsheingoh.scaneat.Model;


public class PostDetails {

    public PostDetails() {
    }

    private String title, desc, imageUrl;

    public PostDetails(String title, String desc, String imageUrl) {
        this.title = title;
        this.desc = desc;
        this.imageUrl=imageUrl;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }


}

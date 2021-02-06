package com.example.doodle_war.ui.home;

public class DataModel {
    int propic,post;
    String name;

    public DataModel(int propic, int post, String name) {
        this.propic = propic;
        this.post = post;
        this.name = name;
    }

    public int getPropic() {
        return propic;
    }

    public void setPropic(int propic) {
        this.propic = propic;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

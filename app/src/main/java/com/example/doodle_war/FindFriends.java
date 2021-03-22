package com.example.doodle_war;

public class FindFriends
{
    public String profileimage,fullname,bio;

    public FindFriends()
    {

    }


    public FindFriends(String profileimage, String fullname, String bio) {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.bio = bio;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

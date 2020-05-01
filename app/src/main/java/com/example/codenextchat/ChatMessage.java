package com.example.codenextchat;

import android.net.Uri;

public class ChatMessage {
    String username;
    String message;
    String profilePic;

    public ChatMessage() {

    }

    public ChatMessage(String username, String message, String profilePic) {
        this.username = username;
        this.message = message;
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}

package com.Demoe2e.e2edemo.Models;

public class Users {
    private String profilePic, userName, email, password, userId, lasMessage, status;

    public Users() {

    }

    public Users(String profilePic, String userName, String email, String password, String userId, String lasMessage, String status) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lasMessage = lasMessage;
        this.status = status;
    }

    public Users(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLasMessage() {
        return lasMessage;
    }

    public void setLasMessage(String lasMessage) {
        this.lasMessage = lasMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

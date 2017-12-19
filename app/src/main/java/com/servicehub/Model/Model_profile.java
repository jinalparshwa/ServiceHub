package com.servicehub.Model;

/**
 * Created by admin on 1/21/2017.
 */

public class Model_profile {

    String usertype_id;
    String name;
    String email;
    String mobile;
    String image;
    String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUsertype_id() {
        return usertype_id;
    }

    public void setUsertype_id(String usertype_id) {
        this.usertype_id = usertype_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.servicehub.Model;

import java.io.Serializable;

/**
 * Created by admin on 1/19/2017.
 */

public class Model_service_list implements Serializable {
    String service_id;
    String service_name;
    String service_image;
    String service_rate;
    String selected_image;
    int img;
    int  status;

    public String getSelected_image() {
        return selected_image;
    }

    public void setSelected_image(String selected_image) {
        this.selected_image = selected_image;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getService_rate() {
        return service_rate;
    }

    public void setService_rate(String service_rate) {
        this.service_rate = service_rate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

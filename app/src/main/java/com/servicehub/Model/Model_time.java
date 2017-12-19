package com.servicehub.Model;

import java.io.Serializable;

/**
 * Created by admin on 1/30/2017.
 */

public class Model_time implements Serializable{

    String start_time;
    String end_time;
    String hours;
    String inq_id;

    public String getInq_id() {
        return inq_id;
    }

    public void setInq_id(String inq_id) {
        this.inq_id = inq_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}

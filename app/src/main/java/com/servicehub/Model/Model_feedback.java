package com.servicehub.Model;

/**
 * Created by admin on 1/24/2017.
 */

public class Model_feedback {
    String emp_id;
    String ratings;
    String comments;
    String inq_id;

    public String getInq_id() {
        return inq_id;
    }

    public void setInq_id(String inq_id) {
        this.inq_id = inq_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

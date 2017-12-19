package com.servicehub.Model;

/**
 * Created by admin on 1/24/2017.
 */

public class Model_employee {

    String emp_id;
    String emp_name;
    String emp_image;
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

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_image() {
        return emp_image;
    }

    public void setEmp_image(String emp_image) {
        this.emp_image = emp_image;
    }
}

package com.servicehub.Model;

/**
 * Created by admin on 1/21/2017.
 */

public class Model_Order {

    String inquiry_id;
    String inquiry_no;
    String s_time;
    String e_time;
    String amount;
    String payment;
    String status;
    String no_of_job;
    String total;
    String empid;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getNo_of_job() {
        return no_of_job;
    }

    public void setNo_of_job(String no_of_job) {
        this.no_of_job = no_of_job;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getInquiry_id() {
        return inquiry_id;
    }

    public void setInquiry_id(String inquiry_id) {
        this.inquiry_id = inquiry_id;
    }

    public String getInquiry_no() {
        return inquiry_no;
    }

    public void setInquiry_no(String inquiry_no) {
        this.inquiry_no = inquiry_no;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getE_time() {
        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

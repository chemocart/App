package com.chemocart.chemocartseller;

public class DetailModel {
    String phonenumber;
    String email;


    String password;

    public DetailModel() {
    }

    public DetailModel(String phonenumber, String email, String password) {
        this.phonenumber = phonenumber;
        this.email = email;
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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


}

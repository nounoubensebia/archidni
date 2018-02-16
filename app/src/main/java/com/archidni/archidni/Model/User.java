package com.archidni.archidni.Model;

import com.google.gson.Gson;

/**
 * Created by nouno on 16/02/2018.
 */

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;

    public User(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String toJson()
    {
        return new Gson().toJson(this);
    }

    public static User fromJson (String json)
    {
        return new Gson().fromJson(json,User.class);
    }
}

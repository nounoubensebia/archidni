package com.archidni.archidni.Model.Reports;

import com.archidni.archidni.Model.User;

public class Report {
    private User user;
    private String description;

    public Report(User user, String description) {
        this.user = user;
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }
}

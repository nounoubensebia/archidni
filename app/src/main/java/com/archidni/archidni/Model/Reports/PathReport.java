package com.archidni.archidni.Model.Reports;

import com.archidni.archidni.Model.Path.Path;
import com.archidni.archidni.Model.Path.PathSettings;
import com.archidni.archidni.Model.User;

public class PathReport extends Report {
    private Path path;
    private boolean isGood;

    public PathReport(User user, String description, Path path, boolean isGood) {
        super(user, description);
        this.path = path;
        this.isGood = isGood;
    }

    public Path getPath() {
        return path;
    }

    public boolean isGood() {
        return isGood;
    }
}

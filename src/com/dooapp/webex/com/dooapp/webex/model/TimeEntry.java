package com.dooapp.webex.com.dooapp.webex.model;

import javafx.util.Duration;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 18:50
 */
public class TimeEntry {

    private final Project project;

    private final Duration duration;

    public TimeEntry(Duration duration, Project project) {
        this.duration = duration;
        this.project = project;
    }
}

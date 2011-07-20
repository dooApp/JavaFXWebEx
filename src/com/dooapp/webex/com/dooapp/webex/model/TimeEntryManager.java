package com.dooapp.webex.com.dooapp.webex.model;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 18:51
 */
public class TimeEntryManager {

    private ObjectProperty<ObservableList<TimeEntry>> timeEntries = new ObjectProperty<ObservableList<TimeEntry>>(FXCollections.observableList(new ArrayList<TimeEntry>()));

    public final ObservableList<TimeEntry> getTimeEntries() {
        return timeEntries.get();
    }

    public ObjectProperty<ObservableList<TimeEntry>> timeEntriesProperty() {
        return timeEntries;
    }

}

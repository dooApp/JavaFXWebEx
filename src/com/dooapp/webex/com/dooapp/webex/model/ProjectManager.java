package com.dooapp.webex.com.dooapp.webex.model;

import com.google.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 15:58
 */
public class ProjectManager {

    private ObjectProperty<ObservableList<Project>> projects = new ObjectProperty<ObservableList<Project>>(FXCollections.observableList(new ArrayList<Project>()));

    public final ObservableList<Project> getProjects() {
        return projects.get();
    }

    public ObjectProperty<ObservableList<Project>> projectsProperty() {
        return projects;
    }

    @Inject
    public ProjectManager(Project... projects) {
        getProjects().addAll(projects);
    }

}

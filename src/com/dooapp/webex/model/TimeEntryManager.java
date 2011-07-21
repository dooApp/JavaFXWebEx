package com.dooapp.webex.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.PieChart;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 18:51
 */
public class TimeEntryManager {

    private ObservableMap<Project, ObservableList<Duration>> timeEntries = FXCollections.observableMap(new HashMap<Project, ObservableList<Duration>>());

    public void addDuration(Duration duration, final Project project) {
        ObservableList<Duration> durationList = timeEntries.get(project);
        if (durationList == null) {
            durationList = FXCollections.observableList(new ArrayList<Duration>());
            timeEntries.put(project, durationList);
            final PieChart.Data data = new PieChart.Data(project.getName(), 0);
            durationList.addListener(new ListChangeListener() {

                public void onChanged(Change change) {
                    Duration total = Duration.ZERO;
                    for (Duration d : timeEntries.get(project)) {
                        total = total.add(d);
                    }
                    data.pieValueProperty().set(total.toMillis());
                    data.nameProperty().bind(project.nameProperty());
                }
            });
            pieChartData.add(data);
        }
        durationList.add(duration);
    }

    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableList(new ArrayList<PieChart.Data>());

    public ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

}

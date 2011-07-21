package com.dooapp.webex.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.webex.model.Project;
import com.dooapp.webex.model.ProjectManager;
import com.dooapp.webex.model.TimeEntryManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.builders.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 15:50
 */
public class SceneModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public SceneBuilder createSceneBuilder(ProjectManager projectManager, TimeEntryManager timeEntryManager) {
        this.timeEntryManager = timeEntryManager;
        return SceneBuilder.create()
                .root(createRoot(projectManager))
                .width(800)
                .height(480)
                .stylesheets(SceneModule.class.getResource("style.css").toString());
    }

    private Parent createRoot(ProjectManager projectManager) {
        return BorderPaneBuilder.create()
                .top(createTitleNode())
                .left(createProjectNode(projectManager))
                .center(HBoxBuilder.create()
                        .children(createTimerNode(), createPieNode())
                        .spacing(50)
                        .build())
                .build();
    }

    private TimeEntryManager timeEntryManager;

    private ObjectProperty<Project> selectedProject = new ObjectProperty<Project>();

    /**
     * Creates the project selection/edition Node
     *
     * @param projectManager
     * @return
     */
    private javafx.scene.Node createProjectNode(final ProjectManager projectManager) {
        final ChoiceBox<Project> projectChoiceBox = ChoiceBoxBuilder.<Project>create()
                .items(projectManager.getProjects())
                .build();
        final Group formRoot = GroupBuilder.create().
                build();
        selectedProject.bind(projectChoiceBox.getSelectionModel().selectedItemProperty());
        selectedProject.addListener(new ChangeListener<Project>() {
            public void changed(ObservableValue<? extends Project> observableValue, Project project, Project project1) {
                formRoot.getChildren().clear();
                formRoot.getChildren().add(createForm(selectedProject.get()));
                startDate = null;
                stopTimer();
            }
        });
        projectChoiceBox.getSelectionModel().selectFirst();
        Label subTitle = LabelBuilder.create()
                .text("Project")
                .build();
        subTitle.getStyleClass().add("sub-title");
        return VBoxBuilder.create()
                .children(
                        HBoxBuilder.create()
                                .children(
                                        subTitle,
                                        ButtonBuilder.create()
                                                .onAction(new EventHandler<ActionEvent>() {
                                                    public void handle(ActionEvent actionEvent) {
                                                        projectManager.getProjects().add(new Project("New project"));
                                                        projectChoiceBox.getSelectionModel().selectLast();
                                                    }
                                                }).text("+")
                                                .id("sub-title")
                                                .build())
                                .build(),
                        projectChoiceBox,
                        formRoot)
                .build();
    }

    private Node createForm(Project project) {
        return new FXForm(project);
    }

    /**
     * Creates the title Node
     *
     * @return
     */
    private Node createTitleNode() {
        Label title = LabelBuilder.create()
                .text("JavaFX 2.0 WebEx by dooApp")
                .build();
        title.getStyleClass().add("title");
        return title;
    }

    private StringProperty startButtonText = new StringProperty("Start");

    private RotateTransition rotateTransition;

    private final static DateFormat TIME_FORMAT = new SimpleDateFormat("mm:ss");

    private Date startDate;

    /**
     * Creates the timer Node
     *
     * @return
     */
    private Node createTimerNode() {
        int radius = 75;
        Button startButton = ButtonBuilder.create()
                .onAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent actionEvent) {
                        if (rotateTransition.getStatus() == Animation.Status.RUNNING) {
                            stopTimer();
                        } else {
                            startTimer();
                        }
                    }
                })
                .id("big-button")
                .build();
        startButton.textProperty().bind(startButtonText);
        Line line = LineBuilder.create()
                .stroke(Color.BLACK)
                .startX(0.0)
                .startY(radius)
                .endX(0.0)
                .endY(-radius)
                .strokeWidth(3.0)
                .build();
        rotateTransition = RotateTransitionBuilder.create()
                .duration(Duration.valueOf(30000))
                .node(line)
                .fromAngle(0.0)
                .toAngle(360)
                .cycleCount(RotateTransition.INDEFINITE)
                .interpolator(Interpolator.LINEAR)
                .build();
        Label elapsedTimeLabel = LabelBuilder.create()
                .id("time-label")
                .build();
        // Low-level binding
        elapsedTimeLabel.textProperty().bind(new StringBinding() {
            {
                bind(rotateTransition.currentTimeProperty(), selectedProject);
            }

            @Override
            protected String computeValue() {
                rotateTransition.currentTimeProperty().get();
                if (startDate != null) {
                    return TIME_FORMAT.format(new Date(new Date().getTime() - startDate.getTime()));
                } else {
                    return "";
                }
            }
        });
        Group clock = GroupBuilder.create()
                .children(
                        CircleBuilder.create()
                                .radius(radius)
                                .fill(Color.WHITE)
                                .stroke(Color.GREY)
                                .strokeWidth(10.0)
                                .build(),
                        line

                ).build();
        return VBoxBuilder.create()
                .children(
                        clock,
                        elapsedTimeLabel,
                        startButton
                )
                .fillWidth(true)
                .alignment(Pos.CENTER)
                .build();
    }

    protected void startTimer() {
        startDate = new Date();
        rotateTransition.play();
        startButtonText.set("Stop");
    }

    protected void stopTimer() {
        if (startDate != null) {
            recordDuration();
            rotateTransition.stop();
            startButtonText.set("Start");
        }
    }

    private void recordDuration() {
        Duration duration = Duration.valueOf((new Date().getTime() - startDate.getTime()));
        System.out.println("Recording " + duration + "@" + selectedProject.get());
        timeEntryManager.addDuration(duration, selectedProject.get());
    }

    private Node createPieNode() {
        PieChart pieChart = PieChartBuilder.create()
                .data(timeEntryManager.getPieChartData())
                .build();
        return pieChart;
    }

}

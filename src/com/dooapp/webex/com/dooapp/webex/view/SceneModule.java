package com.dooapp.webex.com.dooapp.webex.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.webex.com.dooapp.webex.model.Project;
import com.dooapp.webex.com.dooapp.webex.model.ProjectManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.builders.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

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
    public SceneBuilder createSceneBuilder(ProjectManager projectManager) {
        return SceneBuilder.create().root(createRoot(projectManager));
    }

    private Parent createRoot(ProjectManager projectManager) {
        return BorderPaneBuilder.create()
                .top(createTitleNode())
                .left(createProjectNode(projectManager))
                .center(createTimerNode())
                .build();
    }

    private ObjectProperty<Project> selectedProject = new ObjectProperty<Project>();

    private javafx.scene.Node createProjectNode(final ProjectManager projectManager) {
        final ChoiceBox<Project> projectChoiceBox = ChoiceBoxBuilder.<Project>create()
                .items(projectManager.getProjects()).build();
        projectChoiceBox.getSelectionModel().selectFirst();
        final Group formRoot = GroupBuilder.create().build();
        selectedProject.bind(projectChoiceBox.getSelectionModel().selectedItemProperty());
        selectedProject.addListener(new ChangeListener<Project>() {
            public void changed(ObservableValue<? extends Project> observableValue, Project project, Project project1) {
                formRoot.getChildren().clear();
                formRoot.getChildren().add(createForm(selectedProject.get()));
            }
        });
        return VBoxBuilder.create().children(
                HBoxBuilder.create().children(
                        new Label("Project"),
                        ButtonBuilder.create().onAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent actionEvent) {
                                projectManager.getProjects().add(new Project("New project"));
                                projectChoiceBox.getSelectionModel().selectLast();
                            }
                        }).text("+").build()).build(),
                projectChoiceBox,
                formRoot)
                .build();
    }

    private Node createForm(Project project) {
        return new FXForm(project);
    }

    private Node createTitleNode() {
        return new Label("JavaFX 2.0 WebEx by dooApp");
    }

    private StringProperty startButtonText = new StringProperty("Start");

    private RotateTransition rotateTransition;

    private Node createTimerNode() {
        Button startButton = ButtonBuilder.create()
                .onAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent actionEvent) {
                        if (rotateTransition.getStatus() == Animation.Status.RUNNING) {
                            rotateTransition.stop();
                        } else {
                            rotateTransition.play();
                        }
                    }
                }).build();
        Line line = LineBuilder.create()
                .stroke(Color.GREEN)
                .startX(0.0)
                .startY(0)
                .endX(0.0)
                .endY(-25)
                .build();
        rotateTransition = RotateTransitionBuilder.create().duration(Duration.valueOf(60000)).node(line).fromAngle(0.0).toAngle(360).cycleCount(RotateTransition.INDEFINITE).build();
        Group clock = GroupBuilder.create()
                .children(
                        CircleBuilder.create()
                                .radius(25)
                                .fill(Color.WHITE)
                                .stroke(Color.BLACK)
                                .strokeWidth(2.0)
                                .build(),
                        line

                ).build();
        return VBoxBuilder.create()
                .children(
                        clock,
                        startButton
                )
                .fillWidth(true)
                .build();
    }

}

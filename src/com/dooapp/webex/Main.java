package com.dooapp.webex;

import com.dooapp.webex.com.dooapp.webex.model.ModelModule;
import com.dooapp.webex.com.dooapp.webex.view.SceneModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.builders.SceneBuilder;
import javafx.stage.Stage;

/**
 * User: dooApp
 * Date: 20/07/11
 * Time: 15:37
 */
public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = Guice.createInjector(new SceneModule(), new DemoModule(), new ModelModule());
        // Get our scene builder and create the scene
        stage.setScene(injector.getInstance(SceneBuilder.class).build());
        // Show the stage
        stage.setVisible(true);
    }
}

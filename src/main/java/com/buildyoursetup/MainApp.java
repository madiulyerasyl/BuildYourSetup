package com.buildyoursetup;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("BUILD YOUR SETUP");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Create and customize your own PC setup"
        );

        Button startButton = new Button("Create New Build");

        startButton.setPrefWidth(200);

        startButton.setOnAction(event -> {
            subtitle.setText(
                    "PC Builder is coming soon!"
            );
        });

        VBox layout = new VBox(
                20,
                title,
                subtitle,
                startButton
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Scene scene = new Scene(layout, 800, 500);

        stage.setTitle("Build Your Setup");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

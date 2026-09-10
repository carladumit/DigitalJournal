package org.carladumit.digitaljournal.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Objects.requireNonNull(
                        getClass().getResource("/LoginView.fxml"),
                        "Error: '/LoginView.fxml' could not be found. Check your file location."
                )
        );

        Scene scene = new Scene(fxmlLoader.load(), 500, 600);

        stage.setTitle("Digital Journal - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
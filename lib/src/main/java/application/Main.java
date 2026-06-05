package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/Menu.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/View/style.css").toExternalForm()
            );

            primaryStage.setTitle("Menu");
            primaryStage.setScene(scene);
            primaryStage.show();

            root.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
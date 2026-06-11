package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            URL fxml = getClass().getResource("/View/Menu.fxml");
            System.out.println("FXML = " + fxml);

            URL css = getClass().getResource("/View/style.css");
            System.out.println("CSS = " + css);

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            Scene scene = new Scene(root, 420, 550);

            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            }

            primaryStage.setTitle("Terminal Clicker");

            primaryStage.setScene(scene);

            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (Exception e) {

            System.out.println("===== ERROR =====");
            System.out.println("Tipo: " + e.getClass().getName());
            System.out.println("Mensaje: " + e.getMessage());

            Throwable causa = e.getCause();

            while (causa != null) {
                System.out.println("Causa: " + causa.getClass().getName());
                System.out.println("Mensaje causa: " + causa.getMessage());
                causa = causa.getCause();
            }

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
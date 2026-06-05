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

            URL fxml = getClass().getResource("/View/Game.fxml");
            System.out.println("FXML = " + fxml);

			URL fxml = getClass().getResource("/View/Menu.fxml");
			System.out.println("FXML = " + fxml);


			URL css = getClass().getResource("/View/style.css");
			System.out.println("CSS = " + css);

			FXMLLoader loader = new FXMLLoader(fxml);
			Parent root = loader.load();

			Scene scene = new Scene(root);

			if (css != null) {
				scene.getStylesheets().add(css.toExternalForm());
			}

<<<<<<< HEAD
            primaryStage.setTitle("Juego");
            primaryStage.setScene(scene);
            primaryStage.show();
=======
			primaryStage.setTitle("Menu");
			primaryStage.setScene(scene);
			primaryStage.show();
>>>>>>> 8c0ee165021d914dfc1ee27733bbb7e01f018ec5

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
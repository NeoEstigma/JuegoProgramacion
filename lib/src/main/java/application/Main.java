package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación Terminal Clicker.
 * Extiende {@link Application} de JavaFX y actúa como punto de entrada
 * de la interfaz gráfica. Se encarga de cargar la vista inicial del menú,
 * aplicar el CSS y configurar la ventana principal.
 *
 * @author Mateo, Diego, Laura
 */


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
            
            primaryStage.getIcons().add(
            	    new Image(getClass().getResourceAsStream("../imagenes/mascaraVerde.jpg")));

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
    
    /**
     * Punto de entrada principal de la aplicación.
     * Llama a {@link Application#launch(String...)} para iniciar JavaFX.
     *
     * @param args argumentos de línea de comandos 
     */

    public static void main(String[] args) {
        launch(args);
    }
}
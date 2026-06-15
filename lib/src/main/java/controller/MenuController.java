package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Partida;
import model.PartidaDao;

/**
 * Controlador del menú principal de Terminal Clicker.
 * Gestiona la navegación hacia las distintas pantallas del juego:
 * nueva partida, continuar partida guardada, ranking y salir.
 * Utiliza {@link PartidaDao} para comprobar si existe una partida guardada
 * y {@link Partida} para establecer el singleton antes de cargar el juego.
 *
 * @author Mateo, Diego, Laura
 */


public class MenuController {

	@FXML
	private Button btnContinuar;
	/** DAO para acceder a la partida guardada en MongoDB. */
	private PartidaDao partidaDao = new PartidaDao();

	/**
     * Método de inicialización llamado automáticamente por JavaFX al cargar la vista.
     * Comprueba si existe una partida guardada en MongoDB. Si la partida está
     * terminada la elimina. Desactiva el botón continuar si no hay partida disponible.
     */
	
	@FXML
	public void initialize() {
		SoundController.playInicio();
		Partida guardada = partidaDao.cargarUnica();

		if (guardada != null && guardada.estaTerminado()) {
			partidaDao.eliminarUnica();
			guardada = null;
		}

		btnContinuar.setDisable(guardada == null);
	}
	
	/**
     * Carga la partida guardada desde MongoDB y navega a la pantalla de juego.
     * Recalcula los precios de las mejoras según las compras realizadas antes
     * de establecer la partida como instancia activa del singleton.
     */

	@FXML
	private void continuarPartida() {
		SoundController.playClick();
		SoundController.stopInicio();
		
		Partida guardada = partidaDao.cargarUnica();

		if (guardada != null) {
			guardada.recalcularPrecios();
			Partida.setInstancia(guardada);
			cargarVista("/View/Game.fxml");
		}
	}
	
	/**
     * Muestra un diálogo para introducir el nombre del operador e inicia
     * una nueva partida. Si ya existe una partida guardada, la elimina antes
     * de crear la nueva. Navega a la pantalla de juego al confirmar.
     */

	@FXML
	private void nuevaPartida() {
		SoundController.playClick();
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Nueva partida");
		dialog.setHeaderText(null);
		dialog.setContentText("Introduce tu nombre:");

		dialog.setGraphic(null);

		dialog.getDialogPane().getStylesheets().add(getClass().getResource("/View/style.css").toExternalForm());

		dialog.getDialogPane().getStyleClass().add("terminal-dialog");

		Optional<String> resultado = dialog.showAndWait();

		resultado.ifPresent(nombre -> {
			String nombreLimpio = nombre.trim();

			if (!nombreLimpio.isEmpty()) {
				partidaDao.eliminarUnica();
				Partida.nuevaPartida(nombreLimpio);
				cargarVista("/View/Game.fxml");
				SoundController.stopInicio();
			}
		});
		
	}
	
	/**
     * Navega a la pantalla de ranking.
     */

	@FXML
	private void mostrarRanking() {
		cargarVista("/View/Ranking.fxml");
	}
	
	/**
     * Cierra la aplicación.
     */

	@FXML
	private void salir() {
		SoundController.playClick();
		Stage stage = (Stage) btnContinuar.getScene().getWindow();
		stage.close();
	}
	
	/**
     * Carga una vista FXML y la establece como escena principal.
     * Aplica el CSS externo y maximiza la ventana.
     *
     * @param ruta ruta del fichero FXML a cargar, relativa a los recursos.
     */

	private void cargarVista(String ruta) {
	    try {
	        URL fxml = getClass().getResource(ruta);

	        if (fxml == null) {
	            throw new IOException("No se encontró el FXML: " + ruta);
	        }

	        FXMLLoader loader = new FXMLLoader(fxml);
	        Parent root = loader.load();

	        Stage stage = (Stage) btnContinuar.getScene().getWindow();

	        Scene escenaActual = stage.getScene();

	        if (escenaActual == null) {
	            Scene nuevaEscena = new Scene(root);
	            nuevaEscena.getStylesheets().add(
	                getClass().getResource("/View/style.css").toExternalForm()
	            );
	            stage.setScene(nuevaEscena);
	        } else {
	            escenaActual.setRoot(root);
	        }

	        stage.setMaximized(true);

	    } catch (IOException e) {
	        System.out.println("Error al cargar vista: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}
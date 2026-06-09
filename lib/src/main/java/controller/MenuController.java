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

public class MenuController {

	@FXML
	private Button btnContinuar;

	private PartidaDao partidaDao = new PartidaDao();

	@FXML
	public void initialize() {
		// Activa continuar solo si hay una partida guardada
		Partida guardada = partidaDao.cargarUnica();
		btnContinuar.setDisable(guardada == null);
	}

	@FXML
	private void continuarPartida() {
		// Carga directamente la partida guardada
		cargarVista("/View/Game.fxml");
	}

	@FXML
	private void nuevaPartida() {
		// Popup para pedir el nombre
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Nueva partida");
		dialog.setHeaderText(null);
		dialog.setContentText("Introduce tu nombre:");

		Optional<String> resultado = dialog.showAndWait();
		resultado.ifPresent(nombre -> {
			if (!nombre.trim().isEmpty()) {
				// Borra la partida anterior si existe
				partidaDao.eliminarUnica();
				// When the player enters their name and starts the game:
				Partida.nuevaPartida(nombre);
				// Carga la vista del juego
				cargarVista("/View/Game.fxml");
			}
		});
	}

	@FXML
	private void mostrarRanking() {
		cargarVista("/View/Ranking.fxml");
	}

	@FXML
	private void salir() {
		Stage stage = (Stage) btnContinuar.getScene().getWindow();
		stage.close();
	}

	private void cargarVista(String ruta) {
		try {
			URL fxml = getClass().getResource(ruta);
			FXMLLoader loader = new FXMLLoader(fxml);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) btnContinuar.getScene().getWindow();
			stage.setScene(scene);
		} catch (IOException e) {
			System.out.println("Error al cargar vista: " + e.getMessage());
		}
	}

}
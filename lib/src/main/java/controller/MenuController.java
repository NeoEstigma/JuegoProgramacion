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
        Partida guardada = partidaDao.cargarUnica();
        btnContinuar.setDisable(guardada == null);
    }

    @FXML
    private void continuarPartida() {
        Partida guardada = partidaDao.cargarUnica();

        if (guardada != null) {
            Partida.setInstancia(guardada);
            cargarVista("/View/Game.fxml");
        }
    }

    @FXML
    private void nuevaPartida() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva partida");
        dialog.setHeaderText(null);
        dialog.setContentText("Introduce tu nombre:");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(nombre -> {
            String nombreLimpio = nombre.trim();

            if (!nombreLimpio.isEmpty()) {
                partidaDao.eliminarUnica();
                Partida.nuevaPartida(nombreLimpio);
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

            if (fxml == null) {
                throw new IOException("No se encontró el FXML: " + ruta);
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            Stage stage = (Stage) btnContinuar.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error al cargar vista: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
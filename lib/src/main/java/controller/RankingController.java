package controller;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Ranking;

public class RankingController implements Initializable {

    @FXML private TableView<Ranking> tablaRanking;

    @FXML private TableColumn<Ranking, Number> colPosicion;
    @FXML private TableColumn<Ranking, String> colJugador;
    @FXML private TableColumn<Ranking, Number> colDp;
    @FXML private TableColumn<Ranking, Number> colNivel;

    @FXML private TableColumn<Ranking, Number> colRaspberry;
    @FXML private TableColumn<Ranking, Number> colPC;
    @FXML private TableColumn<Ranking, Number> colJunior;
    @FXML private TableColumn<Ranking, Number> colSenior;
    @FXML private TableColumn<Ranking, Number> colMaqCafe;
    @FXML private TableColumn<Ranking, Number> colRGBS;

    @FXML private TableColumn<Ranking, String> colTiempo;
    @FXML private TableColumn<Ranking, String> colFecha;

    @FXML private Button btnVolver;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    

    private ObservableList<Ranking> listaRanking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaRanking = FXCollections.observableArrayList();

        colPosicion.setCellValueFactory(cell ->
                new SimpleIntegerProperty(tablaRanking.getItems().indexOf(cell.getValue()) + 1)
        );

        colJugador.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getJugador())
        );

        colDp.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getDp())
        );

        colNivel.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getNivel())
        );

        colRaspberry.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumRaspberry())
        );

        colPC.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumPC())
        );

        colJunior.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumJunior())
        );

        colSenior.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumJunior())
        );

        colMaqCafe.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumMaqCafe())
        );

        colRGBS.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMejoras().getNumRGBS())
        );

        colTiempo.setCellValueFactory(cell ->
                new SimpleStringProperty(formatearTiempo(cell.getValue().getTiempoPartida()))
        );

        colFecha.setCellValueFactory(cell ->
                new SimpleStringProperty(formatearFecha(cell.getValue().getFechaInicio()))
        );

        tablaRanking.setItems(listaRanking);

        actualizarRanking();
    }


    @FXML
    private void actualizarRanking() {
        listaRanking.clear();

        /*
         * Aquí deberías cargar los datos reales desde JSON,
         * MongoDB local o donde estés guardando el ranking.
         */

        // Ejemplo temporal
        listaRanking.add(new Ranking("Neo", 1500, 4, null, 320, new Date()));
        listaRanking.add(new Ranking("Admin", 900, 3, null, 250, new Date()));
        listaRanking.add(new Ranking("Hacker", 500, 2, null, 180, new Date()));
    }

    @FXML
    private void eliminarRanking() {
        listaRanking.clear();
        tablaRanking.refresh();
    }

    private String formatearTiempo(long segundos) {
        long minutos = segundos / 60;
        long seg = segundos % 60;

        return minutos + " min " + seg + " s";
    }

    private String formatearFecha(Date fecha) {
        if (fecha == null) {
            return "Sin fecha";
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formato.format(fecha);
    }
    @FXML
    private void volver() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/View/Menu.fxml")
            );

            Stage stage = (Stage) btnVolver.getScene().getWindow();

            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    getClass().getResource("/View/style.css").toExternalForm()
            );

            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Mejoras;
import model.Ranking;
import model.RankingDao;

public class RankingController implements Initializable {

	@FXML
	private TableView<Ranking> tablaRanking;
	@FXML
	private TableColumn<Ranking, Number> colPosicion;
	@FXML
	private TableColumn<Ranking, String> colJugador;
	@FXML
	private TableColumn<Ranking, Number> colDp;
	@FXML
	private TableColumn<Ranking, Number> colNivel;
	@FXML
	private TableColumn<Ranking, Number> colRaspberry;
	@FXML
	private TableColumn<Ranking, Number> colPC;
	@FXML
	private TableColumn<Ranking, Number> colJunior;
	@FXML
	private TableColumn<Ranking, Number> colSenior;
	@FXML
	private TableColumn<Ranking, Number> colMaqCafe;
	@FXML
	private TableColumn<Ranking, Number> colRGBS;
	@FXML
	private TableColumn<Ranking, String> colTiempo;
	@FXML
	private TableColumn<Ranking, String> colFecha;
	@FXML
	private Button btnVolver;
	@FXML
	private Button btnActualizar;
	@FXML
	private Button btnEliminar;

	private ObservableList<Ranking> listaRanking;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    listaRanking = FXCollections.observableArrayList();

	    tablaRanking.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	    colPosicion.setCellValueFactory(
	            cell -> new SimpleIntegerProperty(tablaRanking.getItems().indexOf(cell.getValue()) + 1));

	    // resto de tu código igual...

		colJugador.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getJugador()));

		colDp.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getDp()));

		colNivel.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getNivel()));

		colRaspberry.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumRaspberry() : 0);
		});

		colPC.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumPC() : 0);
		});

		colJunior.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumJunior() : 0);
		});

		colSenior.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumSenior() : 0);
		});

		colMaqCafe.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumMaqCafe() : 0);
		});

		colRGBS.setCellValueFactory(cell -> {
			Mejoras m = cell.getValue().getMejoras();
			return new SimpleIntegerProperty(m != null ? m.getNumRGBS() : 0);
		});

		colTiempo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTiempoFormateado()));

		colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaFormateada()));

		tablaRanking.setItems(listaRanking);
		actualizarRanking();
	}

	@FXML
	private void actualizarRanking() {
		listaRanking.clear();
		RankingDao rankingDao = new RankingDao();
		List<Ranking> datos = rankingDao.obtenerTodos();
		listaRanking.addAll(datos);
	}

	@FXML
	private void eliminarRanking() {
		Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
		alerta.setTitle("Eliminar ranking");
		alerta.setHeaderText(null);
		alerta.setContentText("¿Seguro que quieres eliminar todo el ranking? Esta acción no se puede deshacer.");

		alerta.showAndWait().ifPresent(respuesta -> {
			if (respuesta == ButtonType.OK) {
				RankingDao rankingDao = new RankingDao();
				rankingDao.eliminarTodo();
				listaRanking.clear();
				tablaRanking.refresh();
			}
		});
	}

	@FXML
	private void volver() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/View/Menu.fxml"));
			Stage stage = (Stage) btnVolver.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/View/style.css").toExternalForm());
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
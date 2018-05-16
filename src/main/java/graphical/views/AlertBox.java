package graphical.views;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertBox {

	public AlertBox() {
	}

	public static Optional<ButtonType> showAndWait(AlertType alertType, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle("CryptoChat");
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		return alert.showAndWait();
	}
}

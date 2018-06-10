package graphical.views;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * class containing the method that displays the alert
 */
public class AlertBox {

	public AlertBox() {
	}

	/**
	 * method that displays an alert with specific content
	 * @author Marcin Lesniewski
	 * @param alertType type of alert displayed
	 * @param headerText main text of the alert displayed
	 * @param contentText text content of the displayed alert
	 * @return alert of the appropriate type and with the relevant texts
	 */
	public static Optional<ButtonType> showAndWait(AlertType alertType, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle("CryptoChat");
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		return alert.showAndWait();
	}
}

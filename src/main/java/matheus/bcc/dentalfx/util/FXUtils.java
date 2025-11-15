package matheus.bcc.dentalfx.util;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public class FXUtils {
    public static <T> void configurarComboBox(ComboBox<T> comboBox, List<T> items, String texto, Function<T, String> funcao) {
        comboBox.setItems(FXCollections.observableList(items));
        comboBox.setPromptText(texto);

        comboBox.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText(funcao.apply(item));
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText(funcao.apply(item));
            }
        });
    }

    public static void configurarDatePicker(DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now()))
                    setDisable(true);
            }
        });
        datePicker.setValue(LocalDate.now());
    }
}

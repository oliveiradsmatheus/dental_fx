package matheus.bcc.dentalfx.util;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

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
}

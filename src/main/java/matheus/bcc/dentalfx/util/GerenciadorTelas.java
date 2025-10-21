package matheus.bcc.dentalfx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.DentalFXApplication;

public class GerenciadorTelas {
    public static void carregar(Stage stage, String nomeFXML, String titulo, String icone, boolean modal) {
        try {
            FXMLLoader loader = new FXMLLoader(DentalFXApplication.class.getResource(nomeFXML));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.getIcons().add(new Image(GerenciadorTelas.class.getResourceAsStream("/icones/" + icone + ".png")));
            if (modal) {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
            } else {
                stage.setMaximized(true);
                stage.show();
            }
        } catch (Exception e) {
            Erro.exibir("Erro ao carregar tela: " + e.getMessage());
        }
    }
}

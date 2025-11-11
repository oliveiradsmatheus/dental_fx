package matheus.bcc.dentalfx.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.DentalFXApplication;

import java.util.function.Consumer;

public class GerenciadorTelas {
    public static <T> void carregar(Stage stage, String nomeFXML, String titulo, String icone, boolean modal) {
        carregar(stage, nomeFXML, titulo, icone, modal, null);
    }

    public static <T> void carregar(Stage stage, String nomeFXML, String titulo, String icone, boolean modal, Consumer<T> injector) {
        try {
            FXMLLoader loader = new FXMLLoader(DentalFXApplication.class.getResource(nomeFXML));
            Parent root = loader.load();

            T controller = loader.getController();

            if (injector != null)
                injector.accept(controller);

            Scene scene = new Scene(root);
            GerenciadorTemas.registrar(scene);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.getIcons().add(new Image(GerenciadorTelas.class.getResourceAsStream("/icones/" + icone + ".png")));
            if (modal) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                stage.setMaximized(true);
                stage.show();
            }
        } catch (Exception e) {
            Alerta.exibirErro("Erro de carregamento", "Erro ao carregar tela: " + e.getMessage());
        }
    }
}

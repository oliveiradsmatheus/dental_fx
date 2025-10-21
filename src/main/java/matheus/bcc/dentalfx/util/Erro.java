package matheus.bcc.dentalfx.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Erro extends Stage {
    public Erro(String mensagem) {
        setTitle("Erro");

        Label lb_erro = new Label(mensagem);
        lb_erro.setWrapText(true);
        lb_erro.setAlignment(Pos.CENTER);

        Button bt_voltar = new Button("Voltar");
        bt_voltar.setOnAction(e -> close());
        bt_voltar.setDefaultButton(true);

        VBox layout = new VBox(36, lb_erro, bt_voltar);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.getStylesheets().add(getClass().getResource("/matheus/bcc/dentalfx/style.css").toExternalForm());

        Scene scene = new Scene(layout, 640, 224);

        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
        getIcons().add(new Image(Erro.class.getResourceAsStream("/icones/erro.png")));
    }

    public static void exibir(String mensagem) {
        new Erro(mensagem).showAndWait();
    }
}

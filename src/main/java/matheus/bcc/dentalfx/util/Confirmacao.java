package matheus.bcc.dentalfx.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Confirmacao extends Stage {
    private boolean confirmado;

    public Confirmacao(String mensagem) {
        setTitle("Confirmar");

        Label lb_texto = new Label(mensagem);
        lb_texto.setWrapText(true);
        lb_texto.setAlignment(Pos.CENTER);


        Button bt_confirmar = new Button("Confirmar");
        bt_confirmar.setOnAction(e -> {
            confirmado = true;
            close();
        });
        Button bt_cancelar = new Button("Cancelar");

        bt_cancelar.setOnAction(e -> {
            confirmado = false;
            close();
        });

        HBox hb_botoes = new HBox(32, bt_confirmar, bt_cancelar);
        hb_botoes.setAlignment(Pos.CENTER);

        VBox layout = new VBox(36, lb_texto, hb_botoes);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(640, 224);

        layout.getStylesheets().add(getClass().getResource("/matheus/bcc/dentalfx/style.css").toExternalForm());

        Scene scene = new Scene(layout);
        setScene(scene);

        initModality(Modality.APPLICATION_MODAL);
        getIcons().add(new Image(Erro.class.getResourceAsStream("/icones/aviso.png")));
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public static boolean exibir(String mensagem) {
        Confirmacao conf = new Confirmacao(mensagem);
        conf.showAndWait();
        return conf.isConfirmado();
    }
}

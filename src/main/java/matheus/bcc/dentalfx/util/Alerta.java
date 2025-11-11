package matheus.bcc.dentalfx.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alerta extends VBox {
    private boolean confirmado;
    public enum Tipo {
        ALERTA,
        CONFIRMACAO,
        ERRO
    }

    public Alerta(String mensagem, Tipo tipo) {
        Label lb_mensagem = new Label(mensagem);
        lb_mensagem.setWrapText(true);
        lb_mensagem.setAlignment(Pos.CENTER);

        HBox hb_botoes = new HBox(32);
        hb_botoes.setAlignment(Pos.CENTER);

        if (tipo == Tipo.CONFIRMACAO) {
            Button bt_confirmar = new Button("Confirmar");
            bt_confirmar.setOnAction(e -> {
                confirmado = true;
                this.getScene().getWindow().hide();
            });
            bt_confirmar.setDefaultButton(true);

            Button bt_cancelar = new Button("Cancelar");
            bt_cancelar.setOnAction(e -> {
                confirmado = false;
                this.getScene().getWindow().hide();
            });
            bt_cancelar.setCancelButton(true);
            hb_botoes.getChildren().addAll(bt_confirmar, bt_cancelar);
        } else {
            Button bt_voltar = new Button("Voltar");
            bt_voltar.setOnAction(e -> this.getScene().getWindow().hide());
            bt_voltar.setDefaultButton(true);
            hb_botoes.getChildren().addAll(bt_voltar);
        }

        setMinWidth(480);
        setMinHeight(226);
        setAlignment(Pos.CENTER);
        setSpacing(32);
        setPadding(new Insets(30));

        getChildren().addAll(lb_mensagem, hb_botoes);
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public static void exibirAlerta(String titulo, String mensagem) {
        exibir(titulo, mensagem, Tipo.ALERTA, "alerta.png");
    }

    public static boolean exibirConfirmacao(String titulo, String mensagem) {
        return exibir(titulo, mensagem, Tipo.CONFIRMACAO,"alerta.png");
    }

    public static void exibirErro(String titulo, String mensagem) {
        exibir(titulo, mensagem, Tipo.ERRO, "erro.png");
    }

    public static boolean exibir(String titulo, String mensagem, Tipo tipo, String icone) {
        Stage stage = new Stage();
        stage.setTitle(titulo);

        Alerta alerta = new Alerta(mensagem, tipo);
        stage.setScene(new Scene(alerta));
        GerenciadorTemas.registrar(stage.getScene());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Alerta.class.getResourceAsStream("/icones/" + icone)));
        stage.showAndWait();

        return alerta.isConfirmado();
    }
}

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

public class Sobre extends VBox {
    public Sobre() {
        Label lb_titulo = new Label("Atividade - Ferramentas Computacionais II");
        lb_titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label lb_dev_label = new Label("Desenvolvedor:");
        Label lb_dev_nome = new Label("Matheus Oliveira da Silva");
        lb_dev_nome.setStyle("-fx-font-weight: bold;");

        Label lb_professor = new Label("Prof. Me. Silvio Antonio Carro");
        lb_professor.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button bt_voltar = new Button("Voltar");
        bt_voltar.setOnAction(e -> this.getScene().getWindow().hide());
        bt_voltar.setDefaultButton(true);

        setMinWidth(480);
        setMinHeight(200);
        setAlignment(Pos.CENTER);
        setSpacing(36);
        setPadding(new Insets(25));

        getChildren().addAll(lb_titulo, lb_dev_label, lb_dev_nome, lb_professor, bt_voltar);
    }

    public static void exibir(String titulo) {
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(new Sobre()));
        GerenciadorTemas.registrar(stage.getScene());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Sobre.class.getResourceAsStream("/icones/icone.png")));

        stage.showAndWait();
    }
}

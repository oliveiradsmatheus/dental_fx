package matheus.bcc.dentalfx.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matheus.bcc.dentalfx.db.entidades.Usuario;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;

public class LoginPanel extends VBox {
    private boolean sucesso = false;

    public LoginPanel() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(30, 30, 30, 30));

        setSpacing(5);

        Label login_label = new Label("Login:");
        TextField login_field = new TextField();
        login_label.setPadding(new Insets(0,0,0,0));

        Label password_label = new Label("Senha:");
        PasswordField password_field = new PasswordField();
        password_label.setPadding(new Insets(20, 0, 0, 0));

        Button bt_login = new Button("Entrar");
        bt_login.setDefaultButton(true);

        Button bt_cancelar = new Button("Cancelar");
        bt_cancelar.setCancelButton(true);

        HBox hb_botoes = new HBox(10, bt_login, bt_cancelar);
        hb_botoes.setAlignment(Pos.CENTER);

        VBox.setMargin(hb_botoes, new Insets(30, 0, 0, 0));

        bt_login.setOnAction(event -> {
            String login = login_field.getText();
            String password = password_field.getText();
            sucesso = verificarLogin(login, password);
            if (sucesso)
                getScene().getWindow().hide();
            else {
                login_field.setText("");
                password_field.setText("");
            }
        });

        bt_cancelar.setOnAction(event -> {
            Sessao.getInstancia().limparSessao();
            sucesso = false;
            getScene().getWindow().hide();
        });

        getChildren().add(login_label);
        getChildren().add(login_field);
        getChildren().add(password_label);
        getChildren().add(password_field);
        getChildren().add(hb_botoes);
    }

    private boolean verificarLogin(String login, String password) {
        if (!login.isEmpty() &&  !password.isEmpty()) {
            PessoaDAL dal = new PessoaDAL();
            Usuario usuario = dal.getUsuario(login);

            if (usuario != null)
                if (usuario.getSenha().equals(password)) {
                    Sessao.getInstancia().criarSessao(usuario);
                    return true;
                }
            Alerta.exibirErro("Erro", "Usuário ou senha incorretos!");
        } else
            Alerta.exibirErro("Erro", "Preencha todos os campos para realizar Login");
        return false;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public static boolean exibir() {
        Stage stage = new Stage();
        LoginPanel pLogin = new LoginPanel();

        stage.initStyle(StageStyle.UTILITY);
        pLogin.setPrefWidth(512);
        stage.setTitle("Login");
        stage.setScene(new Scene(pLogin));
        GerenciadorTemas.registrar(stage.getScene());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Alerta.class.getResourceAsStream("/icones/icone.png")));
        stage.showAndWait();

        return pLogin.isSucesso();
    }
}

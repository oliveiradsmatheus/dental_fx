package matheus.bcc.dentalfx.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginPanel extends VBox {
    private boolean loginValido;
    private int nivelAcesso = 0;

    public LoginPanel() {
        super();
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20, 20, 20, 20));
        Label loginLabel = new Label("Login:");
        loginLabel.setPadding(new Insets(0,0,10,0));
        TextField loginField = new TextField();
        Label passwordLabel = new Label("Senha:");
        passwordLabel.setPadding(new Insets(20, 0, 10, 0));
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Entrar");
        VBox.setMargin(loginButton, new Insets(30, 0, 0, 0));

        loginButton.setOnAction(event -> {
            String login = loginField.getText();
            String password = passwordField.getText();
            loginValido = verifyLogin(login, password);
            getScene().getWindow().hide();
        });

        getChildren().add(loginLabel);
        getChildren().add(loginField);
        getChildren().add(passwordLabel);
        getChildren().add(passwordField);
        getChildren().add(loginButton);

    }

    private boolean verifyLogin(String login, String password) {
        // coloque aqui a lógica para verificar se o login é válido
        // aqui estamos apenas verificando se A SENHA É O LOGIN invertido
        String reversedLogin = new StringBuilder(login).reverse().toString();
        //verifique também o nível de acesso do usuário, caso pertinente
        nivelAcesso = 1;
        return reversedLogin.equals(password);
    }

    public boolean isLoginValido() {
        return loginValido;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }
}

//coloque o seguinte código no método start do código de lançamento
//crie um novo stage, não use o que vem como parâmetro do start
//Stage stageLogin = new Stage();
//            stageLogin.initStyle(StageStyle.UTILITY);
//
//LoginPanel pLogin = new LoginPanel();
//            pLogin.setPrefWidth(380);
//            stageLogin.setScene(new Scene(pLogin));
//        stageLogin.showAndWait();
//            if (pLogin.isLoginValido()) {
//        // escolha o fxml de acordo com o nível de usuário, caso pertinente
//        // execute o lançamento do stage
//        }

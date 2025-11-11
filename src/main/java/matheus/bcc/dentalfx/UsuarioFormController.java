package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.entidades.NivelUsuario;
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.entidades.Usuario;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.FXUtils;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class UsuarioFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_nome;
    public ComboBox<NivelUsuario> cb_nivel;
    public VBox vb_dentista;
    public ComboBox<Pessoa> cb_dentista;
    public PasswordField pf_senha;

    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarFormulario();
        configurarCB();
    }

    private void configurarFormulario() {
        Platform.runLater(() -> tf_nome.requestFocus());
    }

    public void carregarUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (this.usuario != null)
            preencherFormulario();
    }

    private void preencherFormulario() {
        tf_id.setText("" + usuario.getId());
        tf_nome.setText(usuario.getNome());
        cb_nivel.setValue(NivelUsuario.fromId(usuario.getNivel()));
        pf_senha.setText(usuario.getSenha());
        if (usuario.getDenId() != null)
            cb_dentista.getItems()
                    .stream()
                    .filter(pessoa -> pessoa.getId() == usuario.getDenId())
                    .findFirst()
                    .ifPresent(dentistaEncontrado -> cb_dentista.setValue(dentistaEncontrado)
                    );
    }

    private void configurarCB() {
        cb_nivel.setItems(FXCollections.observableArrayList(NivelUsuario.values()));
        PessoaDAL pessoaDAL = new PessoaDAL();
        List<Pessoa> dentistas = pessoaDAL.get("", new Dentista());
        dentistas.sort(Comparator.comparing(Pessoa::getNome));
        FXUtils.configurarComboBox(cb_dentista, dentistas, "Selecione um dentista", Pessoa::getNome);

        cb_nivel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == NivelUsuario.DENTISTA) {
                vb_dentista.setVisible(true);
                vb_dentista.setManaged(true);
            } else {
                vb_dentista.setVisible(false);
                vb_dentista.setManaged(false);
                cb_dentista.getSelectionModel().clearSelection();
            }
        });
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            Usuario usuario = construirUsuario();
            if (salvarUsuario(usuario)) {
                Alerta.exibirAlerta("Sucesso", "Usuário salvo com sucesso.");
                tf_id.getScene().getWindow().hide();
            } else
                Alerta.exibirErro("Erro", "Não foi possível salvar o usuário.");
        }
    }

    private Usuario construirUsuario() {
        String nome = tf_nome.getText().trim();
        String senha = pf_senha.getText().trim();
        NivelUsuario nivelSelecionado = cb_nivel.getValue();

        Integer denId = null;

        if (nivelSelecionado == NivelUsuario.DENTISTA && cb_dentista.getValue() != null)
            denId = cb_dentista.getValue().getId();

        Usuario usuario = new Usuario(nome, nivelSelecionado.getId(), senha, denId);
        if (!tf_id.getText().isEmpty())
            usuario.setId(Integer.parseInt(tf_id.getText()));

        return usuario;
    }

    private boolean usuarioExistente(String nome, Integer id) {
        PessoaDAL pessoaDAL = new PessoaDAL();
        Usuario existente = pessoaDAL.getUsuario(nome);

        if (existente != null)
            return id == null || !id.equals(existente.getId());
        return false;
    }

    private boolean salvarUsuario(Usuario usuario) {
        PessoaDAL dal = new PessoaDAL();
        boolean sucesso = false;

        if (!usuarioExistente(usuario.getNome(), usuario.getId())) {
            if (usuario.getId() > 0) {
                sucesso = dal.alterar(usuario);
                if (!sucesso)
                    Alerta.exibirErro("Erro ao alterar", "Erro ao alterar o usuário!");
            } else {
                sucesso = dal.gravar(usuario);
                if (!sucesso)
                    Alerta.exibirErro("Erro ao gravar", "Erro ao gravar o usuário!");
            }
        } else
            Alerta.exibirErro("Erro", "Nome de usuário já está em uso. Por favor escolha outro.");

        return sucesso;
    }

    private boolean validarCampos() {
        String nome = tf_nome.getText().trim();
        String senha = pf_senha.getText().trim();
        NivelUsuario nivelSelecionado = cb_nivel.getValue();

        if (!nome.isEmpty() && !senha.isEmpty() && nivelSelecionado != null)
            if (nome.matches("^[a-zA-Z0-9]{4,20}$"))
                if (nivelSelecionado == NivelUsuario.DENTISTA)
                    if (cb_dentista.getValue() != null)
                        return true;
                    else
                        Alerta.exibirErro("Erro", "É necessário vincular um dentista!");
                else
                    return true;
            else
                Alerta.exibirErro("Erro", "O nome de usuário deve conter apenas letras e números (4 a 20 caracteres).");
        else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos.");
        return false;
    }

    private void limparCampos() {
        tf_nome.setText("");
        pf_senha.setText("");
        cb_nivel.getSelectionModel().clearSelection();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}

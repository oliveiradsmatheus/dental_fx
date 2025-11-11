package matheus.bcc.dentalfx.util;

import matheus.bcc.dentalfx.db.entidades.Usuario;

public class Sessao {
    private static Sessao instancia;
    private Usuario usuario;

    private Sessao() {}

    public static Sessao getInstancia() {
        if (instancia == null)
            instancia = new Sessao();
        return instancia;
    }

    public void criarSessao(Usuario usuario) {
        this.usuario = usuario;
    }

    public void limparSessao() {
        usuario = null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isLogado() {
        return usuario != null;
    }
}

package matheus.bcc.dentalfx.db.entidades;

public class Usuario extends Pessoa {
    private int nivel;
    private String senha;

    public Usuario(int id, String nome, int nivel, String senha) {
        super(id, nome);
        this.nivel = nivel;
        this.senha = senha;
    }

    public Usuario (String nome, int nivel, String senha) {
        this(0, nome, nivel, senha);
    }

    public Usuario () {
        this(0, "", 0, "" );
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

package matheus.bcc.dentalfx.db.entidades;

public class Usuario extends Pessoa {
    private int nivel;
    private Integer denId;
    private String senha;

    public Usuario(int id, String nome, int nivel, String senha, Integer denId) {
        super(id, nome);
        this.nivel = nivel;
        this.senha = senha;
        this.denId = denId;
    }

    public Usuario (String nome, int nivel, String senha, Integer denId) {
        this(0, nome, nivel, senha, denId);
    }

    public Usuario () {
        this(0, "", 0, "" , null);
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

    public Integer getDenId() {
        return denId;
    }

    public void setDenId(Integer denId) {
        this.denId = denId;
    }
}

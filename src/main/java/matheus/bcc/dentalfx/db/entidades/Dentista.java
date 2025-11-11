package matheus.bcc.dentalfx.db.entidades;

public class Dentista extends Pessoa {
    int cro;
    String fone;
    String email;

    public Dentista(int id, String nome, int cro, String fone, String email) {
        super(id, nome);
        this.cro = cro;
        this.fone = fone;
        this.email = email;
    }

    public Dentista(String nome, int cro, String fone, String email) {
        this(0, nome, cro, fone, email);
    }

    public Dentista() {
        this(0, "", 0, "", "");
    }

    public int getCro() {
        return cro;
    }

    public void setCro(int cro) {
        this.cro = cro;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.getNome();
    }
}

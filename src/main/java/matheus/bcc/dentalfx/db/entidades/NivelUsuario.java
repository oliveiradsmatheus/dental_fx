package matheus.bcc.dentalfx.db.entidades;

public enum NivelUsuario {
    ADMINISTRADOR(1, "Administrador"),
    SECRETARIA(2, "Secretária"),
    DENTISTA(3, "Dentista");

    private final int id;
    private final String descricao;

    NivelUsuario(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }

    public static NivelUsuario fromId(int id) {
        for (NivelUsuario nivel : values()) {
            if (nivel.id == id) {
                return nivel;
            }
        }
        return null;
    }
}

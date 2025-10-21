package matheus.bcc.dentalfx.db.entidades;

public class Material {
    private int id;
    private String descricao;
    private double preco;

    public Material(int id, String descricao, double preco) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
    }

    public Material(String descricao, double preco) {
        this(0, descricao, preco);
    }

    public Material() {
        this(0, "", 0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}

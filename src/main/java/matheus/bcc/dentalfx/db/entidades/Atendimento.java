package matheus.bcc.dentalfx.db.entidades;

import java.util.ArrayList;
import java.util.List;

public class Atendimento {
    // Inner record
    public static record MatItem(Material material, int quant) {}
    public static record ProcItem(Procedimento procedimento, int quant) {}

    private String relato;
    private List<MatItem> materialList;
    private List<ProcItem> procedimentoList;

    public Atendimento(String relato) {
        this.relato = relato;
        materialList = new ArrayList<>();
        procedimentoList = new ArrayList<>();
    }

    public boolean addMaterial(int quantidade, Material material) {
        return materialList.add(new MatItem(material,quantidade));
    }

    public boolean addProcedimento(int quantidade, Procedimento procedimento) {
        return procedimentoList.add(new ProcItem(procedimento,quantidade));
    }

    public String getRelato() {
        return relato;
    }

    public void setRelato(String relato) {
        this.relato = relato;
    }
}

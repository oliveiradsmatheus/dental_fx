package matheus.bcc.dentalfx.db.dto;

import java.time.LocalDate;

public class ConsultaDTO {
    private LocalDate data;
    private int horario;
    private String relato;
    private String nomeDentista;

    public ConsultaDTO(LocalDate data, int horario, String relato, String nomeDentista) {
        this.data = data;
        this.horario = horario;
        this.relato = relato;
        this.nomeDentista = nomeDentista;
    }

    public LocalDate getData() {
        return data;
    }

    public int getHorario() {
        return horario;
    }

    public String getRelato() {
        return relato;
    }

    public String getNomeDentista() {
        return nomeDentista;
    }
}

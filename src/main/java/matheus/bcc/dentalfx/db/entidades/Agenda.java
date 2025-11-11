package matheus.bcc.dentalfx.db.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private Pessoa dentista;
    private LocalDate data;
    private List<Horario> horarioList;

    public Agenda(Pessoa dentista, LocalDate data) {
        this.dentista = dentista;
        this.data = data;
        horarioList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            horarioList.add(new Horario(i)); // Criando os 10 horários de atendimento diário
    }

    public void setHorario(int sequencia, Horario horario) {
        horarioList.set(sequencia, horario);
    }

    public Horario getHorario(int sequencia) {
        return horarioList.get(sequencia - 1); // Assumindo que o primeiro horário é o horario 1
    }

    public Pessoa getDentista() {
        return dentista;
    }

    public void setDentista(Pessoa dentista) {
        this.dentista = dentista;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Horario> getHorarioList() {
        return horarioList;
    }
}

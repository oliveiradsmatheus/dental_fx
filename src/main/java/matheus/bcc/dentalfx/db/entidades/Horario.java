package matheus.bcc.dentalfx.db.entidades;

public class Horario {
    private int sequencia;
    private Paciente paciente;
    private Atendimento atendimento;

    public Horario(int sequencia, Paciente paciente) {
        this.sequencia = sequencia;
        this.paciente = paciente;
        atendimento = null;
    }

    public Horario(int sequencia) {
        this(sequencia, null);
    }

    public Horario() {
        this(0,null);
    }

    public int getSequencia() {
        return sequencia;
    }

    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }
}

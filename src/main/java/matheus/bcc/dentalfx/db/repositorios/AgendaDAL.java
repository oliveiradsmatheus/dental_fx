package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Alerta;

import java.sql.ResultSet;
import java.time.LocalDate;

public class AgendaDAL {
    public Agenda carregarAgenda(Dentista dentista, LocalDate data) {
        int id, sequencia;
        Agenda agenda = new Agenda(dentista, data);
        String sql = "SELECT * FROM consulta WHERE con_data = '#1' and den_id = #2";
        sql = sql.replace("#1", data.toString());
        sql = sql.replace("#2", "" + dentista.getId());
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                id = rs.getInt("con_id");
                sequencia = rs.getInt("con_horario");
                Horario horario = new Horario( rs.getInt("con_horario"), (Paciente) new PessoaDAL().get(rs.getInt("pac_id"), new Paciente()));
                if (rs.getBoolean("con_efetivado")) {
                    Atendimento atendimento = new Atendimento(rs.getString("con_relato"));
                    ResultSet rsMat = SingletonDB.getConexao().consultar("SELECT * FROM cons_mat WHERE con_id = " + id);
                    while (rsMat.next())
                        atendimento.addMaterial(rsMat.getInt("cm_quant"), new MaterialDAL().get(rsMat.getInt("mat_id")));
                    ResultSet rsProc = SingletonDB.getConexao().consultar("SELECT * FROM cons_proc WHERE con_id = " + id);
                    while (rsProc.next())
                        atendimento.addProcedimento(rsProc.getInt("cp_quant"), new ProcedimentoDAL().get(rsProc.getInt("pro_id")));
                    horario.setAtendimento(atendimento);
                }
                agenda.setHorario(sequencia, horario);
            }
        } catch (Exception e) {
            Alerta.exibirErro("Erro", "Erro: " + e.getMessage());
        }
        return agenda;
    }

    public boolean salvarAgenda(Agenda agenda) {
        boolean resultado = true;
        String sql = "SELECT con_id FROM consulta WHERE con_data = '#1' AND den_id = #2 AND con_horario = #3";
        sql = sql.replace("#1", agenda.getData().toString());
        sql = sql.replace("#2", "" + agenda.getDentista().getId());
        try {
            for (Horario horario : agenda.getHorarioList()) {
                int id = -1; // Atribui id inexistente para consulta no banco
                sql = sql.replace("#3", "" + horario.getSequencia());
                ResultSet rs = SingletonDB.getConexao().consultar(sql);
                if (rs.next()) // Já está no banco
                    id = rs.getInt("con_id"); // Se existe no banco, resgata o id
                if (horario.getPaciente() != null) { // Existe no banco e no objeto, mas pode ter havido alguma alteração.
                    Atendimento atendimento = horario.getAtendimento();
                    boolean efetivado = false;
                    if (id == -1) {
                        // Não existe no banco, dou insert e marco como efetivado true;
                    } else {
                        // Se  existe, altero
                    }

                    if (efetivado) { // se tudo correu bem, adicionar materiais
                        // deletar materiais e procedimentos antigos
                        // inserir materiais e procedimentos novos
                    }

                } else { // Se existe no banco mas não existe no objeto, foi um cancelamento
                    /*if (id != -1) {
                        resultado &= SingletonDB.getConexao().manipular("DELETE FROM cons_mat WHERE con_id = " + id);
                        resultado &= SingletonDB.getConexao().manipular("DELETE FROM cons_proc WHERE con_id = " + id);
                        resultado &= SingletonDB.getConexao().manipular("DELETE FROM consulta WHERE con_id = " + id);
                    }*/
                }
            }
        } catch (Exception e) {
            Alerta.exibirErro("Erro", "Erro: " + e.getMessage());
            resultado = false;
        }
        return resultado;
    }

    public boolean efetivarConsulta(Dentista dentista, LocalDate data) {
        return true;
    }
}

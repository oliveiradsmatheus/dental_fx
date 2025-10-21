package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Erro;

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
            Erro.exibir("Erro: " + e.getMessage());
        }
        return agenda;
    }

    boolean salvarAgenda(Agenda agenda) {
        // Apagar todos os procedimentos e materiais marcados nas consultas da agenda de um determinado dentista em uma determinada data.
        // Apagar todas as consultas marcadas na data da agenda "agenda.getData()" de um determinado dentista "agenda.getDentista()".
        // Criar as consultas, materiais utilizados (tabela cons_mat) e procedimentos (tabela cons_prod).
        return false;
    }
}

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
        String sqlConsulta = "SELECT * FROM consulta WHERE con_data = '#1' AND den_id = #2 AND con_horario = #3";
        sqlConsulta = sqlConsulta.replace("#1", agenda.getData().toString());
        sqlConsulta = sqlConsulta.replace("#2", "" + agenda.getDentista().getId());
        try {
            for (Horario horario : agenda.getHorarioList()) {
                int id = -1, seq = -1;

                String sql = sqlConsulta;
                sql = sql.replace("#3", "" + horario.getSequencia());
                ResultSet rs = SingletonDB.getConexao().consultar(sql);

                if (rs.next())
                    id = rs.getInt("con_id");

                if (horario.getPaciente() != null) {
                    Atendimento atendimento = horario.getAtendimento();
                    boolean efetivado = false;
                    if (id == -1) {
                        String insertSql = "INSERT INTO public.consulta(" +
                                "con_relato, con_data, con_horario, pac_id, den_id, con_efetivado)" +
                                "VALUES ('#1', '#2', #3, #4, #5, #6);";
                        insertSql = insertSql.replace("#1", "");
                        insertSql = insertSql.replace("#2", "" + agenda.getData());
                        insertSql = insertSql.replace("#3", "" + horario.getSequencia());
                        insertSql = insertSql.replace("#4", "" + horario.getPaciente().getId());
                        insertSql = insertSql.replace("#5", "" + agenda.getDentista().getId());
                        insertSql = insertSql.replace("#6", "FALSE");
                        return SingletonDB.getConexao().manipular(insertSql);
                    }
                } else { // Se existe no banco mas não existe no objeto, foi um cancelamento
                    if (id != -1) {
                        resultado = SingletonDB.getConexao().manipular("DELETE FROM cons_mat WHERE con_id = " + id);
                        resultado &= SingletonDB.getConexao().manipular("DELETE FROM cons_proc WHERE con_id = " + id);
                        resultado &= SingletonDB.getConexao().manipular("DELETE FROM consulta WHERE con_id = " + id);
                    }
                }
            }
        } catch (Exception e) {
            Alerta.exibirErro("Erro", "Erro: " + e.getMessage());
            resultado = false;
        }
        return resultado;
    }

    public boolean efetivarConsulta(Horario horario, LocalDate data, Dentista dentista) {
        int idConsulta = -1;
        String sqlConsulta = "SELECT * FROM consulta WHERE con_data = '#1' AND den_id = #2 AND con_horario = #3";
        sqlConsulta = sqlConsulta.replace("#1", data.toString());
        sqlConsulta = sqlConsulta.replace("#2", "" + dentista.getId());
        sqlConsulta = sqlConsulta.replace("#3", "" + horario.getSequencia());

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sqlConsulta);
            if (rs.next())
                idConsulta = rs.getInt("con_id");
            else {
                Alerta.exibirErro("Erro", "Consulta não encontrada no banco de dados.");
                return false;
            }
            String sqlUpdate = "UPDATE consulta SET con_relato = '#1', con_efetivado = TRUE WHERE con_id = #2";
            String relato = "";

            if (horario.getAtendimento().getRelato() != null)
                relato = horario.getAtendimento().getRelato();
            sqlUpdate = sqlUpdate.replace("#1", relato);
            sqlUpdate = sqlUpdate.replace("#2", "" + idConsulta);
            if (SingletonDB.getConexao().manipular(sqlUpdate)) {
                Atendimento atendimento = horario.getAtendimento();

                for (Atendimento.MatItem item : atendimento.getMaterialList()) {
                    String sqlInsertMat = "INSERT INTO cons_mat (con_id, mat_id, cm_quant) VALUES (#1, #2, #3)";
                    sqlInsertMat = sqlInsertMat.replace("#1", "" + idConsulta);
                    sqlInsertMat = sqlInsertMat.replace("#2", "" + item.material().getId());
                    sqlInsertMat = sqlInsertMat.replace("#3", "" + item.quant());

                    if (!SingletonDB.getConexao().manipular(sqlInsertMat))
                        Alerta.exibirErro("Erro", "Erro ao inserir materiais da consulta.");
                }

                for (Atendimento.ProcItem item : atendimento.getProcedimentoList()) {
                    String sqlInsertProc = "INSERT INTO cons_proc (con_id, pro_id, cp_quant) VALUES (#1, #2, #3)";
                    sqlInsertProc = sqlInsertProc.replace("#1", "" + idConsulta);
                    sqlInsertProc = sqlInsertProc.replace("#2", "" + item.procedimento().getId());
                    sqlInsertProc = sqlInsertProc.replace("#3", "" + item.quant());

                    if (!SingletonDB.getConexao().manipular(sqlInsertProc))
                        Alerta.exibirErro("Erro", "Erro ao inserir procedimentos da consulta.");
                }
            } else {
                Alerta.exibirErro("Erro", "Erro ao atualizar o status da consulta.");
                return false;
            }
            return true;
        } catch (Exception e) {
            Alerta.exibirErro("Erro ao realizar consulta", "Erro: " +  e.getMessage());
            return false;
        }
    }

    public boolean limparConsultas() {
        return SingletonDB.getConexao().manipular("DELETE FROM consulta WHERE con_data < CURRENT_DATE");
    }
}

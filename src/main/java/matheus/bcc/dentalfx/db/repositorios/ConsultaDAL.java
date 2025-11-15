package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.dto.ConsultaDTO;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Alerta;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAL {
    public List<ConsultaDTO> buscarConsultasPorPaciente(int pacienteId) {
        List<ConsultaDTO> lista = new ArrayList<>();
        String sql = "SELECT c.con_data, c.con_horario, c.con_relato, d.den_nome " +
                "FROM consulta c " +
                "JOIN dentista d ON c.den_id = d.den_id " +
                "WHERE c.pac_id = #1 AND c.con_efetivado = true " +
                "ORDER BY c.con_data DESC;";
        sql = sql.replace("#1", "" + pacienteId);
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                LocalDate data = rs.getDate("con_data").toLocalDate();
                int horario = rs.getInt("con_horario");
                String relato = rs.getString("con_relato");
                String dentista = rs.getString("den_nome");
                ConsultaDTO dto = new ConsultaDTO(data, horario, relato, dentista);
                lista.add(dto);
            }
            rs.close();
        } catch (Exception e) {
            Alerta.exibirErro("Erro ao buscar histórico de consultas", e.getMessage());
        }
        return lista;
    }
}

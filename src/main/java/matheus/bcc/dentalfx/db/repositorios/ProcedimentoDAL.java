package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.entidades.Procedimento;
import matheus.bcc.dentalfx.db.util.IDAL;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Erro;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProcedimentoDAL implements IDAL<Procedimento> {
    @Override
    public boolean gravar(Procedimento procedimento) {
        String sql = "INSERT INTO procedimento (pro_desc, pro_tempo, pro_valor) VALUES ('#1', #2, #3);";
        sql = sql.replace("#1", procedimento.getDescricao());
        sql = sql.replace("#2", "" + procedimento.getTempo());
        sql = sql.replace("#3", String.format(Locale.US, "%.2f", procedimento.getValor()));
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Procedimento procedimento) {
        String sql = "UPDATE procedimento SET pro_desc = '#1', pro_tempo = #2, pro_valor = #3 WHERE pro_id = #4;";
        sql = sql.replace("#1", procedimento.getDescricao());
        sql = sql.replace("#2", "" + procedimento.getTempo());
        sql = sql.replace("#3", String.format(Locale.US, "%.2f", procedimento.getValor()));
        sql = sql.replace("#4", "" + procedimento.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Procedimento procedimento) {
        return SingletonDB.getConexao().manipular("DELETE FROM procedimento WHERE pro_id = " + procedimento.getId());
    }

    @Override
    public Procedimento get(int id) {
        String sql = "SELECT * FROM procedimento WHERE pro_id = " + id;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Procedimento procedimento = null;
        try {
            if (rs.next())
                procedimento = new Procedimento(rs.getInt("pro_id"), rs.getString("pro_desc"), rs.getDouble("pro_tempo"), rs.getDouble("pro_valor"));
            rs.close();
        } catch(Exception e) {
            Erro.exibir("Erro: " + e.getMessage());
        }
        return procedimento;
    }

    @Override
    public List<Procedimento> get(String filtro) {
        List<Procedimento> procedimentoList = new ArrayList<>();
        String sql = "SELECT * FROM procedimento";
        if (!filtro.isEmpty())
            sql += " WHERE " + filtro;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next())
                procedimentoList.add(new Procedimento(rs.getInt("pro_id"), rs.getString("pro_desc"), rs.getDouble("pro_tempo"), rs.getDouble("pro_valor")));
            rs.close();
        } catch (Exception e) {
            Erro.exibir("Erro: " + e.getMessage());
        }
        return procedimentoList;
    }
}

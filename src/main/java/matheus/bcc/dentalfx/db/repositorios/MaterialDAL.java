package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.entidades.Material;
import matheus.bcc.dentalfx.db.util.IDAL;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Alerta;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaterialDAL implements IDAL<Material> {
    @Override
    public boolean gravar(Material material) {
        String sql = "INSERT INTO material (mat_desc, mat_preco) VALUES ('#1', #2);";
        sql = sql.replace("#1", material.getDescricao());
        sql = sql.replace("#2", "" + material.getPreco());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Material material) {
        String sql = "UPDATE material SET mat_desc = '#1', mat_preco = #2 WHERE mat_id = #3;";
        sql = sql.replace("#1", material.getDescricao());
        sql = sql.replace("#2", String.format(Locale.US, "%.2f", material.getPreco()));
        sql = sql.replace("#3", "" + material.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Material material) {
        return SingletonDB.getConexao().manipular("DELETE FROM material WHERE mat_id = " + material.getId());
    }

    @Override
    public Material get(int id) {
        String sql = "SELECT * FROM material WHERE mat_id = " + id;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Material material = null;
        try {
            if (rs.next())
                material = new Material(rs.getInt("mat_id"), rs.getString("mat_desc"), rs.getDouble("mat_preco"));
            rs.close();
        } catch(Exception e) {
            Alerta.exibirErro("Erro", e.getMessage());
        }
        return material;
    }

    @Override
    public List<Material> get(String filtro) {
        List<Material> materialList = new ArrayList<>();
        String sql = "SELECT * FROM material";
        if (!filtro.isEmpty())
            sql += " WHERE " + filtro;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next())
                materialList.add(new Material(rs.getInt("mat_id"), rs.getString("mat_desc"), rs.getDouble("mat_preco")));
            rs.close();
        } catch (Exception e) {
            Alerta.exibirErro("Erro", e.getMessage());
        }
        return materialList;
    }
}

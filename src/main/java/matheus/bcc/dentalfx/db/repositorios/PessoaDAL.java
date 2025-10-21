package matheus.bcc.dentalfx.db.repositorios;

import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.util.IDAL;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Erro;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAL implements IDAL<Pessoa> {
    @Override
    public boolean gravar(Pessoa pessoa) {
        String sql = "";
        if (pessoa instanceof Dentista) {
            sql = """
                INSERT INTO dentista (
                    den_nome,
                    den_cro,
                    den_fone,
                    den_email
                ) VALUES ('#1', #2, '#3', '#4');
            """;
            sql = sql.replace("#1", pessoa.getNome());
            sql = sql.replace("#2", "" + ((Dentista) pessoa).getCro());
            sql = sql.replace("#3", ((Dentista) pessoa).getFone().replace("(","").replace(")","").replace("-","").replace(" ",""));
            sql = sql.replace("#4", ((Dentista) pessoa).getEmail());
        } else if (pessoa instanceof Paciente) {
            sql = """
                INSERT INTO paciente (
                    pac_cpf,
                    pac_nome,
                    pac_cep,
                    pac_rua,
                    pac_numero,
                    pac_bairro,
                    pac_cidade,
                    pac_uf,
                    pac_fone,
                    pac_email,
                    pac_histo
                ) VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8', '#9', '#A', '#B');
            """;
            sql = sql.replace("#1", ((Paciente) pessoa).getCpf());
            sql = sql.replace("#2", pessoa.getNome());
            sql = sql.replace("#3", ((Paciente) pessoa).getCep());
            sql = sql.replace("#4", ((Paciente) pessoa).getRua());
            sql = sql.replace("#5", ((Paciente) pessoa).getNumero());
            sql = sql.replace("#6", ((Paciente) pessoa).getBairro());
            sql = sql.replace("#7", ((Paciente) pessoa).getCidade());
            sql = sql.replace("#8", ((Paciente) pessoa).getUf());
            sql = sql.replace("#9", ((Paciente) pessoa).getFone().replace("(","").replace(")","").replace("-","").replace(" ",""));
            sql = sql.replace("#A", ((Paciente) pessoa).getEmail());
            sql = sql.replace("#B", ((Paciente) pessoa).getHistorico());
        } else if (pessoa instanceof Usuario) {
            sql = """
                INSERT INTO usuario (
                    uso_nome,
                    uso_nivel,
                    uso_senha
                ) VALUES ('#1', #2, '#3');
            """;
            sql = sql.replace("#1", pessoa.getNome());
            sql = sql.replace("#2", "" + ((Usuario) pessoa).getNivel());
            sql = sql.replace("#3", ((Usuario) pessoa).getSenha());
        }
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean alterar(Pessoa pessoa) {
        String sql = "";
        if (pessoa instanceof Dentista) {
            sql = """
                UPDATE dentista SET
                    den_nome = '#1',
                    den_cro = '#2',
                    den_fone = '#3',
                    den_email = '#4'
                WHERE den_id = #5;
            """;
            sql = sql.replace("#1", pessoa.getNome());
            sql = sql.replace("#2", "" + ((Dentista) pessoa).getCro());
            sql = sql.replace("#3", ((Dentista) pessoa).getFone().replace("(","").replace(")","").replace("-","").replace(" ",""));
            sql = sql.replace("#4", ((Dentista) pessoa).getEmail());
            sql = sql.replace("#5", "" + pessoa.getId());
        } else if (pessoa instanceof Paciente) {
            sql = """
                UPDATE paciente SET
                    pac_cpf = '#1',
                    pac_nome = '#2',
                    pac_cep = '#3',
                    pac_rua = '#4',
                    pac_numero = '#5',
                    pac_bairro = '#6',
                    pac_cidade = '#7',
                    pac_uf = '#8',
                    pac_fone = '#9',
                    pac_email = '#A',
                    pac_histo = '#B'
                WHERE pac_id = #C;
            """;
            sql = sql.replace("#1", ((Paciente) pessoa).getCpf());
            sql = sql.replace("#2", pessoa.getNome());
            sql = sql.replace("#3", ((Paciente) pessoa).getCep());
            sql = sql.replace("#4", ((Paciente) pessoa).getRua());
            sql = sql.replace("#5", ((Paciente) pessoa).getNumero());
            sql = sql.replace("#6", ((Paciente) pessoa).getBairro());
            sql = sql.replace("#7", ((Paciente) pessoa).getCidade());
            sql = sql.replace("#8", ((Paciente) pessoa).getUf());
            sql = sql.replace("#9", ((Paciente) pessoa).getFone().replace("(","").replace(")","").replace("-","").replace(" ",""));
            sql = sql.replace("#A", ((Paciente) pessoa).getEmail());
            sql = sql.replace("#B", ((Paciente) pessoa).getHistorico());
            sql = sql.replace("#C", "" + pessoa.getId());
        } else if (pessoa instanceof Usuario) {
            sql = """
                UPDATE usuario SET
                    uso_nome = '#1',
                    uso_nivel = #2,
                    uso_senha = '#3'
                WHERE uso_id = #4;
            """;
            sql = sql.replace("#1", pessoa.getNome());
            sql = sql.replace("#2", "" + ((Usuario) pessoa).getNivel());
            sql = sql.replace("#3", ((Usuario) pessoa).getSenha());
            sql = sql.replace("#4", "" + pessoa.getId());

        }
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Pessoa pessoa) {
        String sql = "";
        if (pessoa instanceof Dentista)
            sql = "DELETE FROM dentista WHERE den_id = " + pessoa.getId();
        else if (pessoa instanceof Paciente)
            sql = "DELETE FROM paciente WHERE pac_id = " + pessoa.getId();
        else if (pessoa instanceof Usuario)
            sql = "DELETE FROM usuario WHERE uso_id = " + pessoa.getId();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public Pessoa get(int id) {
        return null;
    }

    public Pessoa get(int id, Pessoa p) {
        Pessoa pessoa = null;
        ResultSet rs;
        String sql = "SELECT * FROM ";
        if (p instanceof Dentista) {
            sql += "dentista WHERE den_id = " + id;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                if (rs.next())
                    pessoa = new Dentista(
                            rs.getInt("den_id"),
                            rs.getString("den_nome"),
                            rs.getInt("den_cro"),
                            rs.getString("den_fone"),
                            rs.getString("den_email")
                    );
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        } else if (p instanceof Paciente) {
            sql += "paciente WHERE pac_id = " + id;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                if (rs.next())
                    pessoa = new Paciente(
                            rs.getInt("pac_id"),
                            rs.getString("pac_nome"),
                            rs.getString("pac_cpf"),
                            rs.getString("pac_cep"),
                            rs.getString("pac_rua"),
                            rs.getString("pac_numero"),
                            rs.getString("pac_bairro"),
                            rs.getString("pac_cidade"),
                            rs.getString("pac_uf"),
                            rs.getString("pac_fone"),
                            rs.getString("pac_email"),
                            rs.getString("pac_histo")
                    );
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        } else if (p instanceof Usuario) {
            sql += "usuario WHERE uso_id = " + id;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                if (rs.next())
                    pessoa = new Usuario(
                            rs.getInt("uso_id"),
                            rs.getString("uso_nome"),
                            rs.getInt("uso_nivel"),
                            rs.getString("uso_senha")
                    );
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        }
        return pessoa;
    }

    @Override
    public List<Pessoa> get(String filtro) {
        return List.of();
    }

    public List<Pessoa> get(String filtro, Pessoa pessoa) {
        List<Pessoa> pessoaList = new ArrayList<>();
        ResultSet rs;
        String sql = "SELECT * FROM ";
        if (pessoa instanceof Dentista) {
            sql += "dentista";
            if (!filtro.isEmpty())
                sql += " WHERE " + filtro;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                while (rs.next())
                    pessoaList.add(new Dentista(
                            rs.getInt("den_id"),
                            rs.getString("den_nome"),
                            rs.getInt("den_cro"),
                            rs.getString("den_fone"),
                            rs.getString("den_email")
                    ));
                rs.close();
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        } else if (pessoa instanceof Paciente) {
            sql += "paciente";
            if (!filtro.isEmpty())
                sql += " WHERE " + filtro;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                while (rs.next())
                    pessoaList.add(new Paciente(
                            rs.getInt("pac_id"),
                            rs.getString("pac_nome"),
                            rs.getString("pac_cpf"),
                            rs.getString("pac_cep"),
                            rs.getString("pac_rua"),
                            rs.getString("pac_numero"),
                            rs.getString("pac_bairro"),
                            rs.getString("pac_cidade"),
                            rs.getString("pac_uf"),
                            rs.getString("pac_fone"),
                            rs.getString("pac_email"),
                            rs.getString("pac_histo")
                    ));
                rs.close();
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        } else if (pessoa instanceof Usuario) {
            sql += "usuario";
            if (!filtro.isEmpty())
                sql += " WHERE " + filtro;
            rs = SingletonDB.getConexao().consultar(sql);
            try {
                while (rs.next())
                    pessoaList.add(new Usuario(
                            rs.getInt("uso_id"),
                            rs.getString("uso_nome"),
                            rs.getInt("uso_nivel"),
                            rs.getString("uso_senha")
                    ));
                rs.close();
            } catch (Exception e) {
                Erro.exibir("Erro: " + e.getMessage());
            }
        }
        return pessoaList;
    }
}

package matheus.bcc.dentalfx.db.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class SingletonDB {
    private static Conexao conexao = null;

    public static boolean conectar() {
        conexao = new Conexao();
        return conexao.conectar("jdbc:postgresql://localhost:5432/", "sisdentaldb", "postgres", "postgres123");
    }

    public static Conexao getConexao() {
        return conexao;
    }

    public static boolean criarBD(String BD) {
        try {
            String url = "jdbc:postgresql://localhost/";
            Connection con = DriverManager.getConnection(url,"postgres","postgres123");

            Statement statement = con.createStatement();
            statement.execute("CREATE DATABASE " + BD);

            statement.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean criarTabelas(String script, String BD) {
        try {
            String url = "jdbc:postgresql://localhost/" + BD;
            Connection con = DriverManager.getConnection(url, "postgres","postgres123");

            Statement statement = con.createStatement();
            List<String> linhas = Files.readAllLines(Paths.get(script), StandardCharsets.UTF_8);
            for (String linha : linhas)
                if (!linha.trim().isEmpty() && !linha.trim().startsWith("--"))
                    statement.addBatch(linha);
            statement.executeBatch();

            statement.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean excluirBD(String BD) {
        try {
            String url = "jdbc:postgresql://localhost/";
            Connection con = DriverManager.getConnection(url, "postgres", "postgres123");

            Statement statement = con.createStatement();
            String disconnectSQL = "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '" + BD + "';";
            statement.execute(disconnectSQL);
            statement.execute("DROP DATABASE IF EXISTS " + BD);

            statement.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

package matheus.bcc.dentalfx.db.util;

import matheus.bcc.dentalfx.util.Alerta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Banco {
    private static String getComando(String baseCommand) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows)
            return "bdutil/" + baseCommand + ".exe";
        else
            return baseCommand;
    }

    public static boolean backup(String arquivo, String database) throws Exception {
        final ArrayList<String> comandos = new ArrayList();
        comandos.add(getComando("pg_dump"));

        comandos.add("--host");
        comandos.add("localhost");
        comandos.add("--port"); comandos.add("5432"); comandos.add("--username");
        comandos.add("postgres");comandos.add("--format");comandos.add("plain");
        comandos.add("--blobs");comandos.add("--verbose");comandos.add("--file");
        comandos.add(arquivo);
        comandos.add(database);

        ProcessBuilder pb = new ProcessBuilder(comandos);
        pb.environment().put("PGPASSWORD", "postgres123");
        try {
            final Process process = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line); line = r.readLine();
            }
            r.close();
            process.waitFor();
            process.destroy();
            Alerta.exibirAlerta("Sucesso", "Backup realizado com sucesso.");
            return true;
        } catch (Exception e) {
            Alerta.exibirAlerta("Erro", "Erro na realização do backup.");
            return false;
        }
    }

    public static boolean restaurar(String arquivo, String database) throws Exception {
        final ArrayList<String> comandos = new ArrayList();
        comandos.add(getComando("pg_restore"));

        comandos.add("-c");
        comandos.add("--host"); comandos.add("localhost");
        comandos.add("--port"); comandos.add("5432");
        comandos.add("--username"); comandos.add("postgres");
        comandos.add("--dbname"); comandos.add(database);
        comandos.add("--verbose");
        comandos.add(arquivo);

        ProcessBuilder pb = new ProcessBuilder(comandos);
        pb.environment().put("PGPASSWORD", "postgres123");
        try {
            final Process process = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();
            process.waitFor();
            process.destroy();
            Alerta.exibirAlerta("Sucesso", "Dados restaurados com sucesso.");
            return true;
        } catch (Exception e) {
            Alerta.exibirAlerta("Erro", "Erro na restauração dos dados.");
            return false;
        }
    }
}
package matheus.bcc.dentalfx.db.util;

import matheus.bcc.dentalfx.util.Alerta;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Backup {
    private static String getComando(String comandoBase) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        if (!isWindows)
            return comandoBase;

        String executavel = comandoBase + ".exe";
        File pastaRaiz = new File("C:\\Program Files\\PostgreSQL");

        if (pastaRaiz.exists() && pastaRaiz.isDirectory()) {
            File[] versoes = pastaRaiz.listFiles(File::isDirectory);

            if (versoes != null && versoes.length > 0) {
                Arrays.sort(versoes, Collections.reverseOrder());

                for (File versao : versoes) {
                    File caminhoBin = new File(versao, "bin\\" + executavel);
                    if (caminhoBin.exists())
                        return caminhoBin.getAbsolutePath();
                }
            }
        }
        return executavel;
    }

    public static boolean backup(String arquivo, String database) {
        final List<String> comandos = new ArrayList<>();

        comandos.add(getComando("pg_dump"));

        comandos.add("--host"); comandos.add("localhost");
        comandos.add("--port"); comandos.add("5432");
        comandos.add("--username"); comandos.add("postgres");
        comandos.add("--format"); comandos.add("custom");
        comandos.add("--blobs");
        comandos.add("--verbose");

        comandos.add("--file");
        comandos.add(new File(arquivo).getAbsolutePath());

        comandos.add(database);

        return executarProcesso(comandos, "Backup realizado com sucesso.", "Erro na realização do backup.");
    }

    public static boolean restaurar(String arquivo, String database) {
        final List<String> comandos = new ArrayList<>();

        comandos.add(getComando("pg_restore"));

        comandos.add("-c");
        comandos.add("--host"); comandos.add("localhost");
        comandos.add("--port"); comandos.add("5432");
        comandos.add("--username"); comandos.add("postgres");
        comandos.add("--dbname"); comandos.add(database);
        comandos.add("--verbose");

        comandos.add(new File(arquivo).getAbsolutePath());

        return executarProcesso(comandos, "Dados restaurados com sucesso.", "Erro na restauração dos dados.");
    }

    private static boolean executarProcesso(List<String> comandos, String msgSucesso, String msgErro) {
        ProcessBuilder pb = new ProcessBuilder(comandos);
        pb.environment().put("PGPASSWORD", "postgres123");

        try {
            final Process process = pb.start();
            final BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = r.readLine();

            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();

            int exitCode = process.waitFor();
            process.destroy();

            if (exitCode == 0) {
                Alerta.exibirAlerta("Sucesso", msgSucesso);
                return true;
            } else {
                Alerta.exibirAlerta("Erro", msgErro + " (Código: " + exitCode + ")");
                return false;
            }
        } catch (Exception e) {
            Alerta.exibirAlerta("Erro", msgErro + " " + e.getMessage());
            return false;
        }
    }
}
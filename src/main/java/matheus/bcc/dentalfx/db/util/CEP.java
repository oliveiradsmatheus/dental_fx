package matheus.bcc.dentalfx.db.util;

import matheus.bcc.dentalfx.util.Alerta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CEP {
    public static String consultar(String cep, String formato) {
        StringBuffer dados = new StringBuffer();
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/" + formato + "/");
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false);
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;
            while (null != (s = br.readLine()))
                dados.append(s);
            br.close();
        } catch (Exception ex) {
            Alerta.exibirErro("Erro ao consultar", ex.toString());
        }
        return dados.toString();
    }
}

package matheus.bcc.dentalfx.db.util;

import javafx.application.Platform;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.Constantes;

public class Inicializador {
    public static boolean verificarConexao() {
        if (!SingletonDB.conectar()) {
            if (Alerta.exibirConfirmacao("Erro na base de dados", "Erro ao conectar com o banco.\nDeseja criar a base de dados?"))
                if (SingletonDB.criarBD(Constantes.NOME_BANCO)) {
                    if (!SingletonDB.criarTabelas(Constantes.SCRIPT_TABELAS, Constantes.NOME_BANCO)) {
                        SingletonDB.excluirBD(Constantes.NOME_BANCO);
                        Alerta.exibirErro("Erro ao criar tabelas","Erro ao criar as tabelas. Base de dados excluída.");
                    }
                } else
                    Alerta.exibirErro("Erro ao criar DB", "Erro ao criar a base de dados\n" + SingletonDB.getConexao().getMensagemErro());
            Platform.exit();
            return false;
        }
        return true;
    }
}

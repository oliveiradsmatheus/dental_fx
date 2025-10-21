package matheus.bcc.dentalfx;

import matheus.bcc.dentalfx.db.entidades.Agenda;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.util.SingletonDB;

import java.time.LocalDate;

public class MainTeste {
    void main() {
        SingletonDB.conectar();
        AgendaDAL dal = new AgendaDAL();
        PessoaDAL pessoaDAL = new PessoaDAL();
        Agenda agenda = dal.carregarAgenda((Dentista) pessoaDAL.get(1, new Dentista()), LocalDate.parse("2023-10-09"));
        IO.println(agenda.getData().toString());
    }
}

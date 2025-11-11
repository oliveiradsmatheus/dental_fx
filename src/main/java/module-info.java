module matheus.bcc.dentalfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires org.json;
    requires java.prefs;
    requires kernel;
    requires io;
    requires layout;
    requires jasperreports;
    requires javafx.web;


    opens matheus.bcc.dentalfx to javafx.fxml;
    exports matheus.bcc.dentalfx;

    // Habilitando reflexão para acesso ao package entidades
    opens matheus.bcc.dentalfx.db.entidades to javafx.fxml;
    exports matheus.bcc.dentalfx.db.entidades;
}
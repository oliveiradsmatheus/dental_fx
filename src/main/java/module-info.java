module matheus.bcc.dentalfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens matheus.bcc.dentalfx to javafx.fxml;
    exports matheus.bcc.dentalfx;
}
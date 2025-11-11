package matheus.bcc.dentalfx;

import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    public WebView web_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = web_view.getEngine();
        webEngine.setJavaScriptEnabled(true);
        File file = new File("ajuda/main.html");
        webEngine.load(file.toURI().toString());
    }
}

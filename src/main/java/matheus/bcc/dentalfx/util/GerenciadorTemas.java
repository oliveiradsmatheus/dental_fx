package matheus.bcc.dentalfx.util;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class GerenciadorTemas {
    private static final List<Scene> cenas = new ArrayList<>();
    private static boolean modoEscuro;

    private static final String TEMA_ESCURO = GerenciadorTemas.class.getResource("/matheus/bcc/dentalfx/temas/tema-escuro.css").toExternalForm();
    private static final String TEMA_CLARO = GerenciadorTemas.class.getResource("/matheus/bcc/dentalfx/temas/tema-claro.css").toExternalForm();

    private static final Preferences prefs = Preferences.userRoot().node("DentalFX");
    private static final String PREF_KEY = "modoEscuro";

    static {
        modoEscuro = prefs.getBoolean(PREF_KEY, true);
    }

    public static void registrar(Scene cena) {
        if (!cenas.contains(cena))
            cenas.add(cena);
        aplicarTema(cena);
    }

    public static void aplicarTema(Scene cena) {
        cena.getStylesheets().clear();
        cena.getStylesheets().add(modoEscuro ? TEMA_ESCURO : TEMA_CLARO);
    }

    public static void trocarTema() {
        modoEscuro = !modoEscuro;
        prefs.putBoolean(PREF_KEY, modoEscuro);
        atualizarCenas();
    }

    private static void atualizarCenas() {
        for (Scene cena : cenas)
            aplicarTema(cena);
    }

    public static boolean isDarkMode() {
        return modoEscuro;
    }
}

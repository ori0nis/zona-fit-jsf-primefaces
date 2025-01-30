package gm.zona_fit;

import com.formdev.flatlaf.FlatDarculaLaf;
import gm.zona_fit.gui.ZonaFitForma;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import javax.swing.*;

// Desactivamos este componente para poder refactorizar a app web:
// @SpringBootApplication
public class ZonaFitSwing {
    // Levantamos la app desde aquí.
    public static void main(String[] args) {
        // Instancia de fábrica de Spring:
        FlatDarculaLaf.setup();
        ConfigurableApplicationContext contextoSpring =
                new SpringApplicationBuilder(ZonaFitSwing.class)
                        .headless(false) // La app es de escritorio
                        .web(WebApplicationType.NONE) // La app no es web
                        .run(args);

        // Creamos un objeto de Swing:
        // Una vez esté lista la fábrica, se ejecuta la app.
        SwingUtilities.invokeLater(() -> {
            ZonaFitForma zonaFitForma = contextoSpring.getBean(ZonaFitForma.class);
            zonaFitForma.setVisible(true);
        });
    }
}

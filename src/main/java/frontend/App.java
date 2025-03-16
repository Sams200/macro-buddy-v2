package frontend;

import com.example.sams.config.SpringContext;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import frontend.controller.PageController;
import frontend.view.LoginView;
import frontend.view.PageView;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.sql.Date;

import static com.example.sams.DemoApplication.context;

public class App {
    public static void startApp() {
        FlatMacDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            // Create and show the application
            new LoginView(context).setVisible(true);
        });
    }
}

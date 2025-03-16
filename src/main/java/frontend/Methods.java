package frontend;

import javax.swing.*;
import java.net.URL;

public class Methods {
    /*
    there used to be a bunch of static methods in here but now it's
    mostly empty
     */

    public static ImageIcon createImageIcon(String path,
                                            String description) {
        URL imgURL = Methods.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

package frontend.view;

import frontend.Methods;
import frontend.controller.DateController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;

public class DateView {
    /*
    the view for writing a date manually
     */
    JFrame frame = new JFrame("");
    ImageIcon ronnieC=Methods.createImageIcon(Resources.ronnie,"YEAH BUDDY");
    Image ronnie=ronnieC.getImage();

    ImageIcon checkmark= Methods.createImageIcon(Resources.checkmark,"a simple checkmark");
    JLabel label1=new JLabel("Set date (yyyy-mm-dd):");
    JTextField textField=new JTextField(10);
    JButton button=new JButton(checkmark);
    JPanel panel=new JPanel();

    DateController dateController;

    public DateView(){
        frame.setIconImage(ronnie);
        frame.setLocationRelativeTo(null);
        //frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(200, 100);
        frame.setMinimumSize(new Dimension(200,100));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dateController.clickedCheck("");
            }
        });

        panel.add(label1);
        panel.add(textField);
        panel.add(button);
        button.addActionListener(e -> dateController.clickedCheck(textField.getText()));

        frame.setContentPane(panel);
    }

    public void setDate(Date date){
        textField.setText(date.toString());
    }

    public DateController getDateController() {
        return dateController;
    }

    public void setDateController(DateController dateController) {
        this.dateController = dateController;
    }

    public void setVisibility(boolean isVisible){
        frame.setVisible(isVisible);
    }

    public void showMessage(String message,int option){

        switch (option){
            case 0:
                JOptionPane.showMessageDialog(frame,message,"Error",JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
    }

    public void closeView(){
        frame.setVisible(false);
        //frame.dispose();
    }

    public void disposeView(){
        //frame.setVisible(false);
        frame.dispose();
    }
}

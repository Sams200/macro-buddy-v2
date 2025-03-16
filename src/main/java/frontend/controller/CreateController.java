package frontend.controller;

import com.example.sams.DemoApplication;
import com.example.sams.request.admin.FoodRequest;
import com.example.sams.service.IAuthService;
import com.example.sams.service.IFoodService;
import com.example.sams.service.implementation.FoodService;
import frontend.view.CreateView;

import javax.swing.*;
import java.awt.*;

public class CreateController {
    /*
    the controller for creating a new food
     */

    private CreateView view;
    private FoodRequest food;
    private PageController pageController;

    private IFoodService foodService;

    public CreateController(PageController pageController) {
        this.foodService= DemoApplication.context.getBean(IFoodService.class);
        this.pageController=pageController;
        this.food = new FoodRequest("Name","Producer",100f, "g",0,0f,0f,0f);
        this.view=new CreateView(this);
        update();
        this.view.setVisibility(true);
    }

    public void clickedX(){
        /*
        cancelled the food creation or closed the window
         */
        //ask the user if are you sure

        int input= JOptionPane.showConfirmDialog(null,
                "Are you sure you want to exit?","",
                JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
        //System.out.println(input);

        if(input==0){
            //yes
            view.setVisibility(false);
        }
    }
    public void clickedCheck(){
        /*
        finalized the food creation
         */
        //add an entry with this food in 1970

        int input=JOptionPane.showConfirmDialog(null,
                "Are you sure you want to add this food?","",
                JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
        //System.out.println(input);

        if(input==0){
            //yes
            foodService.create(food);
            view.setVisibility(false);
            pageController.setDatePressed(false);
        }
    }

    public void update(){
        view.update(food,pageController.getTotals());
    }

    public void setServingUnit(){
        String s= JOptionPane.showInputDialog("Serving Units",
                this.food.servingUnits());

        try{
            if(s.length()<21){
                food=this.food.withServingUnits(s);
                //EntryRepo.update(entry.getId(),entry);
                update();
            }else {
                JOptionPane.showMessageDialog(null,
                        "Length must be lower than 20","",JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e){
            return;
        }
    }

    public void setServingSize(){
        String s= JOptionPane.showInputDialog("Serving Size",this.food.servingSize());

        float quantity;
        try {
            quantity=Float.parseFloat(s);
        }catch (Exception e){
            return;
        }

        if(quantity>0){
            food=this.food.withServingSize(quantity);
            //EntryRepo.update(entry.getId(),entry);
            update();
        }else {
            JOptionPane.showMessageDialog(null,
                    "Serving Size must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setMacro(){
        /*
        set the calories and the macros of the food
         */
        JPanel panel=new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();

        JTextField kcal=new JTextField(food.kcal()+"",5);
        JTextField protein=new JTextField(food.protein()+"",5);
        JTextField fat=new JTextField(food.fat()+"",5);
        JTextField carbs=new JTextField(food.carbs()+"",5);

        c.gridx=0;
        c.gridy=0;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("KCal "),c);
        c.gridx=2;
        panel.add(kcal,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);

        c.gridx=0;
        c.gridy=1;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("Protein "),c);
        c.gridx=2;
        panel.add(protein,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);

        c.gridx=0;
        c.gridy=2;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("Fat "),c);
        c.gridx=2;
        panel.add(fat,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);

        c.gridx=0;
        c.gridy=3;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("Carbs "),c);
        c.gridx=2;
        panel.add(carbs,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);




        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter Values", JOptionPane.OK_CANCEL_OPTION);

        int kcalv;
        float proteinv,fatv,carbsv;

        if(result==JOptionPane.OK_OPTION){
            try {
                kcalv=Integer.parseInt(kcal.getText());
                proteinv=Float.parseFloat(protein.getText());
                fatv=Float.parseFloat(fat.getText());
                carbsv=Float.parseFloat(carbs.getText());
            }catch (Exception e){
                return;
            }

            if(kcalv>=0 && proteinv>=0 && fatv>=0 && carbsv>=0){
                food=food.withMacro(kcalv,proteinv,fatv,carbsv);
                update();
            }else {
                JOptionPane.showMessageDialog(null,
                        "Invalid Values","",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setNameAndProducer(){
        JPanel panel=new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();

        JTextField name=new JTextField(food.name(),5);
        JTextField producer=new JTextField(food.producer(),5);

        c.gridx=0;
        c.gridy=0;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("Name "),c);
        c.gridx=2;
        panel.add(name,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);

        c.gridx=0;
        c.gridy=1;
        c.weightx=0;
        panel.add(Box.createHorizontalStrut(20),c);
        c.gridx=1;
        panel.add(new JLabel("Producer "),c);
        c.gridx=2;
        panel.add(producer,c);
        c.gridx=3;
        c.weightx=1;
        panel.add(Box.createHorizontalGlue(),c);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter Values", JOptionPane.OK_CANCEL_OPTION);
        String namev,producerv;

        if(result==JOptionPane.OK_OPTION){
            try {
                namev=name.getText();
                producerv=producer.getText();
            }catch (Exception e){
                return;
            }

            if(!namev.isEmpty() && !producerv.isEmpty()){
                food=food.withName(namev);
                food=food.withProducer(producerv);
                update();
            }else {
                JOptionPane.showMessageDialog(null,
                        "Invalid Values","",JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void setVisible(boolean b) {
        view.setVisibility(b);
    }
}

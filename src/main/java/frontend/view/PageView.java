package frontend.view;


import com.example.sams.entity.Entry;
import com.example.sams.entity.Macro;
import com.example.sams.enumeration.MealType;
import com.example.sams.response.EntryResponse;
import frontend.Methods;
import frontend.controller.PageController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;


import static java.lang.Math.min;


public class PageView extends JPanel {

    /*
    the view for the main page
     */



    PageController controller;

    ImageIcon calendarIcon = Methods.createImageIcon(Resources.calendar,
            "a small icon of a calendar");
    ImageIcon tomPlatz= Methods.createImageIcon(Resources.tomPlatz,"YOU HAVE TO LIVE IT");
    Image tomPlat= tomPlatz.getImage();

    ///date stuff
    JPanel datePanel = new JPanel();
    JLabel dateCurr = new JLabel();
    JButton datePrev=new JButton("  <  ");
    JButton dateNext=new JButton("  >  ");
    JButton dateSet=new JButton(calendarIcon);
    /////////////////////////////////////////////////


    ///day totals
    JPanel dayTotals=new JPanel();

    JPanel kcalPanel=new JPanel();
    JLabel kcalLabel=new JLabel("Calories Remaining");
    JProgressBar kcalProg=new JProgressBar(0,0,100);
    JButton kcalRemaining=new JButton();


    JPanel nutrientsPanel=new JPanel();
    JLabel nutrientsLabel=new JLabel("Nutrients Remaining");
    JProgressBar proteinProg=new JProgressBar(0,0,100);
    JButton proteinRemaining=new JButton();
    JProgressBar fatProg=new JProgressBar(0,0,100);
    JButton fatRemaining=new JButton();
    JProgressBar carbsProg=new JProgressBar(0,0,100);
    JButton carbsRemaining=new JButton();

    ///////////////////////////////////////////////


    ///breakfast
    static Font totalsFont=new Font("Aptos Black",Font.BOLD,24);
    static Font mediumFont=new Font("Aptos Black",Font.BOLD,16);

    JPanel brePanel=new JPanel();
    JPanel breTitle=new JPanel();
    JLabel breLabel=new JLabel("Breakfast");
    JLabel breCalories=new JLabel();
    JLabel breProtein=new JLabel();
    JLabel breFat=new JLabel();
    JLabel breCarbs=new JLabel();

    JPanel breContent=new JPanel();
    JScrollPane brePane=new JScrollPane(breContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel breBot=new JPanel();
    JButton breAddFood=new JButton("Add Food");
    //////////////////////////////////////////////////////////////////


    ///lunch
    JPanel lunPanel=new JPanel();
    JPanel lunTitle=new JPanel();
    JLabel lunLabel=new JLabel("Lunch");
    JLabel lunCalories=new JLabel();
    JLabel lunProtein=new JLabel();
    JLabel lunFat=new JLabel();
    JLabel lunCarbs=new JLabel();

    JPanel lunContent=new JPanel();
    JScrollPane lunPane=new JScrollPane(lunContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel lunBot=new JPanel();
    JButton lunAddFood=new JButton("Add Food");

    //////////////////////////////////////////////////////////////////

    ///dinner
    JPanel dinPanel=new JPanel();
    JPanel dinTitle=new JPanel();
    JLabel dinLabel=new JLabel("Dinner");
    JLabel dinCalories=new JLabel();
    JLabel dinProtein=new JLabel();
    JLabel dinFat=new JLabel();
    JLabel dinCarbs=new JLabel();

    JPanel dinContent=new JPanel();
    JScrollPane dinPane=new JScrollPane(dinContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel dinBot=new JPanel();
    JButton dinAddFood=new JButton("Add Food");

    //////////////////////////////////////////////////////////////////

    ///snacks
    JPanel snaPanel=new JPanel();
    JPanel snaTitle=new JPanel();
    JLabel snaLabel=new JLabel("Snacks");
    JLabel snaCalories=new JLabel();
    JLabel snaProtein=new JLabel();
    JLabel snaFat=new JLabel();
    JLabel snaCarbs=new JLabel();

    JPanel snaContent=new JPanel();
    JScrollPane snaPane=new JScrollPane(snaContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JPanel snaBot=new JPanel();
    JButton snaAddFood=new JButton("Add Food");

    //////////////////////////////////////////////////////////////////

    ///water
    JPanel watPanel=new JPanel();
    JPanel watTitle=new JPanel();
    JLabel watLabel=new JLabel("Water");
    JLabel watQuan=new JLabel();
    JPanel watBot=new JPanel();
    JButton watAddWater=new JButton("Modify Water");
    //////////////////////////////////////////////////////////////////

    JButton addRecipeButton=new JButton("+");
    JLabel addRecipeLabel=new JLabel("Create Recipe");
    JButton createFoodButton=new JButton("+");
    JLabel createFoodLabel=new JLabel("Create Food");


    JPanel top=new JPanel();
    JPanel mid=new JPanel();
    JPanel left=new JPanel();
    JPanel leftContent=new JPanel();

    JFrame frame=new JFrame("Macro Buddy");
    JPanel page=new JPanel();
    JScrollPane pane = new JScrollPane(page,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    public PageView(PageController controller){
        this.controller=controller;

        frame.setIconImage(tomPlat);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,1000);
        //frame.setMinimumSize(new Dimension(750,200));

        page.setLayout(new BorderLayout());

        ///date stuff
        dateCurr.setPreferredSize(new Dimension(100, 20));

        datePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        datePanel.add(Box.createHorizontalStrut(270));
        datePanel.add(datePrev);
        datePanel.add(dateCurr);
        datePanel.add(dateNext);
        datePanel.add(Box.createHorizontalStrut(80));
        datePanel.add(dateSet);
        datePrev.addActionListener(e -> controller.prevDate());
        dateNext.addActionListener(e -> controller.nextDate());
        dateSet.addActionListener(e -> controller.dateView());

        top.add(datePanel);
        mid.setLayout(new BoxLayout(mid,BoxLayout.Y_AXIS));
        //////////////////////////////////////////////////////////


        ///daily total stuff

        dayTotals.setLayout(new BoxLayout(dayTotals, BoxLayout.PAGE_AXIS));
        dayTotals.setMaximumSize(new Dimension(800,100));

        kcalProg.setPreferredSize(new Dimension(250,5));
        proteinProg.setPreferredSize(new Dimension(75,5));
        fatProg.setPreferredSize(new Dimension(75,5));
        carbsProg.setPreferredSize(new Dimension(75,5));

        kcalPanel.add(kcalLabel);
        kcalPanel.add(kcalRemaining);
        kcalPanel.add(kcalProg);

        nutrientsPanel.add(nutrientsLabel);
        nutrientsPanel.add(proteinRemaining);
        nutrientsPanel.add(proteinProg);

        nutrientsPanel.add(fatRemaining);
        nutrientsPanel.add(fatProg);

        nutrientsPanel.add(carbsRemaining);
        nutrientsPanel.add(carbsProg);

        kcalRemaining.addActionListener(e -> {
            controller.setDayKcal();
            controller.update();
        });
        proteinRemaining.addActionListener(e -> {
            controller.setDayProtein();
            controller.update();
        });
        fatRemaining.addActionListener(e -> {
            controller.setDayFat();
            controller.update();
        });
        carbsRemaining.addActionListener(e -> {
            controller.setDayCarbs();
            controller.update();
        });


        dayTotals.add(kcalPanel);
        dayTotals.add(nutrientsPanel);
        dayTotals.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(57,57,57),3,true),
                BorderFactory.createBevelBorder(BevelBorder.RAISED)));

        mid.add(dayTotals);
        ///////////////////////////////////////////////////////

        mid.add(Box.createRigidArea(new Dimension(0,50)));

        ///breakfast
        mealPanel(brePanel, breTitle, breLabel, breCalories, breProtein, breFat, breCarbs, breContent, brePane, breBot, breAddFood);
        mid.add(brePanel);
        breAddFood.addActionListener(e -> {
            controller.addFood(MealType.BREAKFAST);
        });
        //////////////////////////////////////////

        mid.add(Box.createRigidArea(new Dimension(0,50)));

        ///lunch
        mealPanel(lunPanel, lunTitle, lunLabel, lunCalories, lunProtein, lunFat, lunCarbs, lunContent, lunPane, lunBot, lunAddFood);
        mid.add(lunPanel);
        lunAddFood.addActionListener(e -> {
            controller.addFood(MealType.LUNCH);
        });
        //////////////////////////////////////////////

        mid.add(Box.createRigidArea(new Dimension(0,50)));

        ///dinner
        mealPanel(dinPanel, dinTitle, dinLabel, dinCalories, dinProtein, dinFat, dinCarbs, dinContent, dinPane, dinBot, dinAddFood);
        mid.add(dinPanel);
        dinAddFood.addActionListener(e -> {
            controller.addFood(MealType.DINNER);
        });
        //////////////////////////////////////////////

        mid.add(Box.createRigidArea(new Dimension(0,50)));

        ///snacks
        mealPanel(snaPanel, snaTitle, snaLabel, snaCalories, snaProtein, snaFat, snaCarbs, snaContent, snaPane, snaBot, snaAddFood);
        mid.add(snaPanel);
        snaAddFood.addActionListener(e -> {
            controller.addFood(MealType.SNACK);
        });
        //////////////////////////////////////////////

        mid.add(Box.createRigidArea(new Dimension(0,50)));

        //water
        waterPanel();
        mid.add(watPanel);
        /////////////////////////////////////////////
        mid.add(Box.createVerticalGlue());



        //left
        left.setLayout(new BoxLayout(left,BoxLayout.X_AXIS));
        leftContent.setLayout(new BoxLayout(leftContent,BoxLayout.Y_AXIS));

        left.add(Box.createHorizontalGlue());
        left.add(Box.createHorizontalStrut(20));
        left.add(leftContent);
        left.add(Box.createHorizontalGlue());

        addRecipeLabel.setFont(mediumFont);
        addRecipeButton.setFont(mediumFont);
        createFoodLabel.setFont(mediumFont);
        createFoodButton.setFont(mediumFont);

//        addEntryLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        createFoodLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        addEntryButton.setHorizontalAlignment(SwingConstants.CENTER);
//        createFoodButton.setHorizontalAlignment(SwingConstants.CENTER);

        //leftContent.add(Box.createVerticalStrut(35));

        leftContent.add(Box.createVerticalStrut(50));
        leftContent.add(createFoodLabel);
        leftContent.add(createFoodButton);
        createFoodButton.addActionListener(e -> {
            controller.createFoodView();
        });

        leftContent.add(Box.createVerticalStrut(15));

        leftContent.add(addRecipeLabel);
        leftContent.add(addRecipeButton);
        addRecipeButton.addActionListener(e -> {
            //System.out.println("miau");
            controller.createRecipeView();
        });
        leftContent.add(Box.createVerticalGlue());

//        leftContent.setBorder(BorderFactory.createLineBorder(Color.RED));
//        left.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        //////////////////////////////////////////

        page.add(top,BorderLayout.PAGE_START);
        page.add(mid,BorderLayout.CENTER);
        page.add(left,BorderLayout.LINE_START);

        //frame.add(pane,BorderLayout.LINE_END);
        frame.setContentPane(pane);
    }

    private void waterPanel(){
        watPanel.setLayout(new BorderLayout());
        watPanel.setBorder(BorderFactory.createLineBorder(
                new Color(57,57,57),3,true));
        watPanel.setMaximumSize(new Dimension(650,400));
        watPanel.setMinimumSize(new Dimension(650,400));

        watTitle.setLayout(new GridBagLayout());
        watTitle.setMinimumSize(new Dimension(650,100));
        watTitle.setPreferredSize(new Dimension(650,100));
        watTitle.setMaximumSize(new Dimension(650,100));

        GridBagConstraints breC=new GridBagConstraints();
        watTitle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        watPanel.add(watTitle,BorderLayout.PAGE_START);

        breC.fill=GridBagConstraints.HORIZONTAL;

        breC.gridx=0;
        breC.gridy=0;
        breC.gridwidth=1;
        watTitle.add(Box.createRigidArea(new Dimension(20,1)),breC);

        breC.gridx=1;
        breC.gridy=0;
        breC.gridwidth=6;
        breC.weightx=1;
        watTitle.add(watLabel,breC);
        watLabel.setFont(totalsFont);

        breC.gridx=7;
        breC.gridy=0;
        breC.gridwidth=GridBagConstraints.REMAINDER;
        breC.ipadx=0;
        breC.weightx=0;
        breC.ipadx=20;
        watTitle.add(watQuan,breC);
        watQuan.setFont(totalsFont);
        watQuan.setHorizontalAlignment(SwingConstants.RIGHT);

        watBot.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        watBot.setMaximumSize(new Dimension(650,50));
        watBot.setLayout(new FlowLayout(FlowLayout.LEFT));
        watBot.add(Box.createRigidArea(new Dimension(10,0)));
        watAddWater.setAlignmentX(0.0f);
        watBot.add(watAddWater);
        watAddWater.addActionListener(e -> {
            controller.setWater();
            controller.updateMeal(MealType.SNACK);
        });


        watPanel.add(watBot,BorderLayout.PAGE_END);
    }
    public static void mealPanel(JPanel brePanel, JPanel breTitle, JLabel breLabel, JLabel breCalories,
                                 JLabel breProtein, JLabel breFat, JLabel breCarbs, JPanel breContent,
                                 JScrollPane brePane, JPanel breBot, JButton breAddFood) {

        /*
        create the whole structure for a
        single meal since we're essentially
        doing the same thing 4 times
         */
        brePanel.setLayout(new BorderLayout());
        brePanel.setBorder(BorderFactory.createLineBorder(
                new Color(57,57,57),3,true));
        brePanel.setMaximumSize(new Dimension(650,400));
        brePanel.setMinimumSize(new Dimension(650,400));

        breTitle.setLayout(new GridBagLayout());
        breTitle.setMinimumSize(new Dimension(650,100));
        breTitle.setPreferredSize(new Dimension(650,100));
        breTitle.setMaximumSize(new Dimension(650,100));

        GridBagConstraints breC=new GridBagConstraints();
        breTitle.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        brePanel.add(breTitle,BorderLayout.PAGE_START);

        breC.fill=GridBagConstraints.HORIZONTAL;

        breC.gridx=0;
        breC.gridy=0;
        breC.gridwidth=1;
        breTitle.add(Box.createRigidArea(new Dimension(20,1)),breC);

        breC.gridx=1;
        breC.gridy=0;
        breC.gridwidth=6;
        breC.weightx=1;
        breTitle.add(breLabel,breC);
        breLabel.setFont(totalsFont);

        breC.gridx=7;
        breC.gridy=0;
        breC.gridwidth=GridBagConstraints.REMAINDER;
        breC.ipadx=0;
        breC.weightx=0;
        breC.ipadx=20;
        breTitle.add(breCalories,breC);
        breCalories.setFont(totalsFont);
        breCalories.setHorizontalAlignment(SwingConstants.RIGHT);


        breC.ipadx=10;
        breC.gridx=1;
        breC.gridy=1;
        breC.weightx=0;
        breC.gridwidth=1;
        breTitle.add(breProtein,breC);
        breC.ipadx=10;
        breC.gridx=2;
        breC.gridy=1;
        breC.gridwidth=1;
        breTitle.add(breFat,breC);
        breC.gridx=3;
        breC.gridy=1;
        breC.gridwidth=1;
        breTitle.add(breCarbs,breC);

        breC.gridx=4;
        breC.gridy=1;
        breC.ipadx=0;
        breC.weightx=1;
        breC.gridwidth=GridBagConstraints.REMAINDER;

        breTitle.add(Box.createRigidArea(new Dimension(20,1)),breC);

        breContent.setLayout(new BoxLayout(breContent,BoxLayout.Y_AXIS));
        brePane.setBorder(BorderFactory.createEmptyBorder());
        //brePane.setMaximumSize(new Dimension(650,200));

        breBot.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        breBot.setMaximumSize(new Dimension(650,50));
        breBot.setLayout(new FlowLayout(FlowLayout.LEFT));
        breBot.add(Box.createRigidArea(new Dimension(10,0)));
        breAddFood.setAlignmentX(0.0f);
        breBot.add(breAddFood);


        //brePanel.add(Box.createRigidArea(new Dimension(100,10)),BorderLayout.LINE_START);
        brePanel.add(brePane,BorderLayout.CENTER);
        brePanel.add(breBot,BorderLayout.PAGE_END);
    }

    public void setVisibility(boolean isVisible){
        frame.setVisible(isVisible);
    }

    public void setDateCurr(Date date1) {
        dateCurr.setText("       "+date1.toString());
    }


    ///daily totals functions
    public void setKcalDay(int remaining, int total){
        this.kcalProg.setValue(remaining*100/total);
        this.kcalRemaining.setText(total - remaining +" kcal");
    }
    public void setProteinDay(float remaining, float total){
        proteinProg.setValue((int) (remaining*100/total));
        proteinRemaining.setText((int)(total-remaining)+" protein");
    }
    public void setFatDay(float remaining, float total){
        fatProg.setValue((int) (remaining*100/total));
        fatRemaining.setText((int)(total-remaining)+" fat");
    }
    public void setCarbsDay(float remaining, float total){
        carbsProg.setValue((int) (remaining*100/total));
        carbsRemaining.setText((int)(total-remaining)+" carbs");
    }
    //////////////////////////////////////////////////////////////

    public void setBreTotals(Macro totals){
        setMealTotals(totals, breCalories, breProtein, breFat, breCarbs);
    }

    public void setLunTotals(Macro totals){
        setMealTotals(totals, lunCalories, lunProtein, lunFat, lunCarbs);
    }

    public void setDinTotals(Macro totals){
        setMealTotals(totals, dinCalories, dinProtein, dinFat, dinCarbs);
    }

    public void setSnaTotals(Macro totals){
        setMealTotals(totals, snaCalories, snaProtein, snaFat, snaCarbs);
    }

    private void setMealTotals(Macro totals, JLabel lunCalories, JLabel lunProtein, JLabel lunFat, JLabel lunCarbs) {
        lunCalories.setText(Integer.toString(totals.getKcal())+"  ");
        lunProtein.setText("Protein "+Integer.toString((totals.getProtein().intValue()))+"g");
        lunFat.setText("Fat "+Integer.toString((totals.getFat()).intValue())+"g");
        lunCarbs.setText("Carbs "+Integer.toString((totals.getCarbs()).intValue())+"g");
    }

    public void setBreFood(ArrayList<EntryResponse> breakfast){
        breContent.removeAll();
        if(breakfast.isEmpty()) {
            breContent.setMaximumSize(new Dimension(650,0));
            brePane.setMaximumSize(new Dimension(650,0));
            brePane.setMinimumSize(new Dimension(650,0));

            brePanel.remove(brePane);
            brePanel.setMaximumSize(new Dimension(650, breTitle.getSize().height
                    +breBot.getSize().height));
            return;
        }
        brePanel.add(brePane,BorderLayout.CENTER);

        breContent.setMaximumSize(new Dimension(650,50*breakfast.size()));
        brePane.setMaximumSize(new Dimension(650,min(250,50*breakfast.size())));
        brePane.setMinimumSize(new Dimension(650,min(250,50*breakfast.size())));
        brePane.setPreferredSize(new Dimension(650,min(250,50*breakfast.size())));
        brePanel.setMaximumSize(new Dimension(650, min(400,breTitle.getSize().height
                +50*breakfast.size()+breBot.getSize().height)));
        for(EntryResponse entry : breakfast){

            JPanel item=addFoodPanelButton(entry,MealType.BREAKFAST);
            breContent.add(item);

            //brePane.setPreferredSize(new Dimension(650,50*breakfast.size()+10));
        }
    }

    public void setLunFood(ArrayList<EntryResponse> breakfast){
        lunContent.removeAll();
        if(breakfast.isEmpty()) {
            lunContent.setMaximumSize(new Dimension(650,0));
            lunPane.setMaximumSize(new Dimension(650,0));
            lunPane.setMinimumSize(new Dimension(650,0));

            lunPanel.remove(lunPane);
            lunPanel.setMaximumSize(new Dimension(650, lunTitle.getSize().height
                    +lunBot.getSize().height));
            return;
        }
        lunPanel.add(lunPane,BorderLayout.CENTER);

        lunContent.setMaximumSize(new Dimension(650,50*breakfast.size()));
        lunPane.setMaximumSize(new Dimension(650,min(250,50*breakfast.size())));
        lunPane.setMinimumSize(new Dimension(650,min(250,50*breakfast.size())));
        lunPane.setPreferredSize(new Dimension(650,min(250,50*breakfast.size())));
        lunPanel.setMaximumSize(new Dimension(650, min(400, lunTitle.getSize().height
                +50*breakfast.size()+lunBot.getSize().height)));
        for(EntryResponse entry : breakfast){

            JPanel item=addFoodPanelButton(entry,MealType.LUNCH);
            lunContent.add(item);

            //lunPane.setPreferredSize(new Dimension(650,50*lunakfast.size()+10));
        }

    }

    public void setDinFood(ArrayList<EntryResponse> breakfast){
        dinContent.removeAll();
        if(breakfast.isEmpty()) {
            dinContent.setMaximumSize(new Dimension(650,0));
            dinPane.setMaximumSize(new Dimension(650,0));
            dinPane.setMinimumSize(new Dimension(650,0));

            dinPanel.remove(dinPane);
            dinPanel.setMaximumSize(new Dimension(650, dinTitle.getSize().height
                    +dinBot.getSize().height));
            return;
        }
        dinPanel.add(dinPane,BorderLayout.CENTER);

        dinContent.setMaximumSize(new Dimension(650,50*breakfast.size()));
        dinPane.setMaximumSize(new Dimension(650,min(250,50*breakfast.size())));
        dinPane.setMinimumSize(new Dimension(650,min(250,50*breakfast.size())));
        dinPane.setPreferredSize(new Dimension(650,min(250,50*breakfast.size())));
        dinPanel.setMaximumSize(new Dimension(650, min(400,dinTitle.getSize().height
                +50*breakfast.size()+dinBot.getSize().height)));
        for(EntryResponse entry : breakfast){

            JPanel item=addFoodPanelButton(entry,MealType.DINNER);
            dinContent.add(item);

            //dinPane.setPreferredSize(new Dimension(650,50*dinakfast.size()+10));
        }

    }

    public void setSnaFood(ArrayList<EntryResponse> breakfast){
        snaContent.removeAll();
        if(breakfast.isEmpty()) {
            snaContent.setMaximumSize(new Dimension(650,0));
            snaPane.setMaximumSize(new Dimension(650,0));
            snaPane.setMinimumSize(new Dimension(650,0));

            snaPanel.remove(snaPane);
            snaPanel.setMaximumSize(new Dimension(650, snaTitle.getSize().height
                    +snaBot.getSize().height));
            return;
        }
        snaPanel.add(snaPane,BorderLayout.CENTER);

        snaContent.setMaximumSize(new Dimension(650,50*breakfast.size()));
        snaPane.setMaximumSize(new Dimension(650,min(250,50*breakfast.size())));
        snaPane.setMinimumSize(new Dimension(650,min(250,50*breakfast.size())));
        snaPane.setPreferredSize(new Dimension(650,min(250,50*breakfast.size())));
        snaPanel.setMaximumSize(new Dimension(650, min(400,snaTitle.getSize().height
                +50*breakfast.size()+snaBot.getSize().height)));
        for(EntryResponse entry : breakfast){

            JPanel item=addFoodPanelButton(entry,MealType.SNACK);
            snaContent.add(item);

            //snaPane.setPreferredSize(new Dimension(650,50*snaakfast.size()+10));
        }

    }



    public JPanel addFoodPanelButton(EntryResponse entry, MealType mealType){
        Border raised=BorderFactory.createBevelBorder(0);
        Border lowered=BorderFactory.createBevelBorder(1);

        JPanel jButton=new JPanel();
        jButton.setAlignmentX(0.2f);
        jButton.setBorder(raised);

        MouseAdapter mouseAdapter=new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                jButton.setBorder(lowered);
                //super.mouseClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println(mealType.getMeal());
                controller.entryView(entry,mealType);
                jButton.setBorder(raised);
                //super.mouseReleased(e);
            }
        };



        JLabel name=new JLabel(entry.food().getName());
        JLabel calories=new JLabel(String.valueOf(
                (int)(entry.food().getKcal()*entry.quantity())));
        JLabel below=new JLabel(entry.food().getProducer()+", "+
                entry.quantity()*entry.food().getServingSize()+" "+
                entry.food().getServingUnits());

        jButton.addMouseListener(mouseAdapter);
//        name.addMouseListener(mouseAdapter);
//        calories.addMouseListener(mouseAdapter);
//        below.addMouseListener(mouseAdapter);

//        jButton.add(name);
//        jButton.add(below);
//        jButton.add(Box.createHorizontalStrut(400));
//        jButton.add(calories);

        jButton.setLayout(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();


        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=0;
        c.gridx=0;
        c.gridy=0;
        jButton.add(Box.createHorizontalStrut(30),c);
        c.gridx=1;
        jButton.add(name,c);
        c.gridx=2;
        c.weightx=1;
        jButton.add(Box.createHorizontalStrut(10),c);
        c.gridx=3;
        c.weightx=0;
        jButton.add(calories,c);
        c.gridx=4;
        jButton.add(Box.createHorizontalStrut(30),c);

        c.gridx=1;
        c.gridy=1;
        jButton.add(below,c);
        c.gridx=2;
        c.weightx=1;
        jButton.add(Box.createVerticalStrut(10),c);


        jButton.setPreferredSize(new Dimension(630,50));
        jButton.setMaximumSize(new Dimension(630,50));

        return jButton;
    }
}

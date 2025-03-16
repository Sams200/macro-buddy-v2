package frontend.view;

import com.example.sams.response.FoodResponse;
import com.formdev.flatlaf.FlatClientProperties;
import frontend.Methods;
import frontend.controller.AddController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AddView {
    /*
    the view for adding a new food/recipe
     */

    AddController controller;
    ImageIcon leeP= Methods.createImageIcon(Resources.lee,"Call them tall, not big");
    Image lee= leeP.getImage();
    JFrame frame=new JFrame();
    JPanel page=new JPanel();
    JPanel top=new JPanel();
    JPanel mid=new JPanel();
    JScrollPane paneFood=new JScrollPane(page,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JTextField searchBar=new JTextField();
    //JPanel searchPanel=new JPanel();

    /////////////////////////////////////

    ///recipes
    JPanel pageRecipe=new JPanel();
    JPanel topRecipe=new JPanel();
    JPanel midRecipe=new JPanel();
    JScrollPane paneRecipe=new JScrollPane(pageRecipe,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JTextField searchBarRecipe=new JTextField();

    boolean includeRecipes;

    public AddView(AddController controller,boolean includeRecipes){
        this.controller=controller;
        this.includeRecipes=includeRecipes;
        frame.setIconImage(lee);

        Dimension d=new Dimension(665,500);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //frame.setSize(660, 500);
        frame.setMinimumSize(d);
        frame.setPreferredSize(d);
        frame.setMaximumSize(d);
        frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.clickedX();
            }
        });

        paneFood.setMinimumSize(d);
        paneFood.setPreferredSize(d);
        paneFood.setMaximumSize(d);
        page.setLayout(new BorderLayout());
        page.add(top,BorderLayout.PAGE_START);
        page.add(mid,BorderLayout.CENTER);

        mid.setLayout(new BoxLayout(mid,BoxLayout.Y_AXIS));

        searchBar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Search");
        searchBar.setColumns(15);
        searchBar.addActionListener(e -> controller.searchFood(searchBar));
        searchBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        Timer timer=new Timer(250,e -> controller.searchFood(searchBar));
        timer.setRepeats(false);

        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        top.add(searchBar);

        ////////////////////////////////////////
        ///recipes

        paneRecipe.setMinimumSize(d);
        paneRecipe.setPreferredSize(d);
        paneRecipe.setMaximumSize(d);
        pageRecipe.setLayout(new BorderLayout());
        pageRecipe.add(topRecipe,BorderLayout.PAGE_START);
        pageRecipe.add(midRecipe,BorderLayout.CENTER);

        midRecipe.setLayout(new BoxLayout(midRecipe,BoxLayout.Y_AXIS));

        searchBarRecipe.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Search");
        searchBarRecipe.setColumns(15);
        searchBarRecipe.addActionListener(e -> controller.searchRecipe(searchBarRecipe));
        searchBarRecipe.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        Timer timer2=new Timer(250,e -> controller.searchRecipe(searchBarRecipe));
        timer2.setRepeats(false);

        searchBarRecipe.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timer2.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer2.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        topRecipe.add(searchBarRecipe);



        JTabbedPane tab=new JTabbedPane();
        tab.add("  Food  ",paneFood);
        if(includeRecipes)
            tab.add("  Recipes  ",pageRecipe);
        frame.add(tab);
    }

    public void reset(){
        this.searchBar.setText("");
        searchBar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Search");
    }
    public void addFoodList(ArrayList<FoodResponse> list, boolean isFood){
        /*
        draw the list of food/recipe in the window
         */
        JPanel panel;
        if(isFood)
            panel=mid;
        else
            panel=midRecipe;


        panel.removeAll();
        for(FoodResponse food : list){
            JPanel item=addFoodPanelButton(food);
            panel.add(item);
        }
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }


    public JPanel addFoodPanelButton(FoodResponse food){
        /*
        draw a single food/recipe panelButton thingy
         */
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
                //System.out.println(food.getName());
                jButton.setBorder(raised);
                controller.clickedFood(food);
                //super.mouseReleased(e);
            }
        };



        JLabel name=new JLabel(food.name());
        JLabel calories=new JLabel(String.valueOf(
                (int)(food.kcal())));
        JLabel below=new JLabel(food.producer()+", "+
                food.servingSize()+" "+
                food.servingUnits());

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

    public void setVisibility(boolean isVisible){
        frame.setVisible(isVisible);
    }

    public void closeView(){
        frame.setVisible(false);
        //frame.dispose();
    }

    public void disposeView(){
        //frame.setVisible(false);
        frame.dispose();
    }

    public void setController(AddController controller){
        this.controller=controller;
    }
}

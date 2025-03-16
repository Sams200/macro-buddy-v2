package frontend.controller;

import com.example.sams.DemoApplication;
import com.example.sams.controller.SessionManager;
import com.example.sams.entity.Entry;
import com.example.sams.entity.Food;
import com.example.sams.entity.Macro;
import com.example.sams.enumeration.MealType;
import com.example.sams.enumeration.Role;
import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.response.UserSettingsResponse;
import com.example.sams.service.IAuthService;
import com.example.sams.service.IEntryService;
import com.example.sams.service.IUserService;
import com.example.sams.service.IUserSettingsService;
import com.example.sams.service.implementation.EntryService;
import com.example.sams.service.implementation.UserSettingsService;
import frontend.model.Page;
import frontend.view.PageView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.*;
import java.sql.Date;
import java.util.*;

@Getter
@Setter
public class PageController {
    /*
    the controller for the main page of the application
     */
    private Page page;
    private PageView view;
    private Date date;
    private boolean buttonPressed;

    private Macro totals=new Macro();
    private boolean initialize;

    private IEntryService entryService;
    private IUserSettingsService userSettingsService;

    AddController addController;
    DateController dateController;
    EntryController entryController;
    CreateController createController;

    public PageController(Date date){
        this.entryService= DemoApplication.context.getBean(IEntryService.class);
        this.userSettingsService= DemoApplication.context.getBean(IUserSettingsService.class);

        this.page=new Page(date);

        updatePage(date);


        this.date=date;
        this.buttonPressed=false;

        readConfig();

    }

    public void updatePage(Date date){


        //reset the page
        page.setBreMacroTotal(new Macro(0,0f,0f,0f));
        page.setLunMacroTotal(new Macro(0,0f,0f,0f));
        page.setDinMacroTotal(new Macro(0,0f,0f,0f));
        page.setSnaMacroTotal(new Macro(0,0f,0f,0f));
        page.setDayMacroTotal(new Macro(0,0f,0f,0f));

        page.setBreakfast(new ArrayList<EntryResponse>());
        page.setLunch(new ArrayList<EntryResponse>());
        page.setDinner(new ArrayList<EntryResponse>());
        page.setSnack(new ArrayList<EntryResponse>());

        List<EntryResponse> entries =
                entryService.findByUserIdAndDate(SessionManager.getUser().getUserId(), date.toLocalDate());




        for(EntryResponse entry:entries){
            Food food=entry.food();
            Macro macro=new Macro();

            switch (entry.meal().charAt(0)){
                case 'B':
                    page.addBreakfast(entry);

                    macro=page.getBreMacroTotal();
                    macro.addMacro(food.getMacro(),entry.quantity());
                    page.setBreMacroTotal(macro);
                    page.getDayMacroTotal().addMacro(food.getMacro(),entry.quantity());
                    break;
                case 'L':
                    page.addLunch(entry);

                    macro=page.getLunMacroTotal();
                    macro.addMacro(food.getMacro(),entry.quantity());
                    page.setLunMacroTotal(macro);
                    page.getDayMacroTotal().addMacro(food.getMacro(),entry.quantity());
                    break;
                case 'D':
                    page.addDinner(entry);

                    macro=page.getDinMacroTotal();
                    macro.addMacro(food.getMacro(),entry.quantity());
                    page.setDinMacroTotal(macro);
                    page.getDayMacroTotal().addMacro(food.getMacro(),entry.quantity());
                    break;
                case 'S':
                    page.addSnack(entry);

                    macro=page.getSnaMacroTotal();
                    macro.addMacro(food.getMacro(),entry.quantity());
                    page.setSnaMacroTotal(macro);
                    page.getDayMacroTotal().addMacro(food.getMacro(),entry.quantity());
                    break;
            }
        }
    }

    public boolean isDatePressed() {
        return buttonPressed;
    }

    public void setDatePressed(boolean datePressed) {
        this.buttonPressed = datePressed;
    }

    public void nextDate(){
        //add a day to the date
        this.date.setTime(this.date.getTime()+1000*60*60*24);
        update();
    }

    public void prevDate(){
        //subtract a day from the date
        this.date.setTime(this.date.getTime()-1000*60*60*24);
        update();
    }

    public void readConfig(){
        //read the config with the app settings
        //if there is an error restore to the default settings

        UserSettingsResponse settings=
                userSettingsService.findByUserId(SessionManager.getUser().getUserId());

        this.totals.setKcal(settings.dailyKcalGoal());
        this.totals.setProtein(settings.dailyProteinGoal());
        this.totals.setFat(settings.dailyFatGoal());
        this.totals.setCarbs(settings.dailyCarbGoal());
    }

    public void writeConfig(boolean initialize, Macro macro){
        /*
        write to the config with the given settings
         */
        UserSettingsRequest request=
                new UserSettingsRequest(
                        macro.getKcal(),
                        macro.getProtein(),
                        macro.getFat(),
                        macro.getCarbs());
        userSettingsService.createOrUpdate(SessionManager.getUser().getUserId(),request);
        this.totals=macro;
    }

    public void update(){
        /*
        update all the values and items in the page
         */
        updatePage(date);
        view.setDateCurr(page.getDate());

        ////day totals
        view.setKcalDay(page.getDayMacroTotal().getKcal(),this.totals.getKcal());
        view.setProteinDay(page.getDayMacroTotal().getProtein(),
                totals.getProtein());
        view.setFatDay(page.getDayMacroTotal().getFat(),
                totals.getFat());
        view.setCarbsDay(page.getDayMacroTotal().getCarbs(),
                totals.getCarbs());
        /////////////////////////////////////////

        ///breakfast
        view.setBreTotals(page.getBreMacroTotal());
        view.setBreFood(page.getBreakfast());
        /////////////////////////////////////////
        //lunch
        view.setLunTotals(page.getLunMacroTotal());
        view.setLunFood(page.getLunch());
        /////////////////////////////////////////
        //dinner
        view.setDinTotals(page.getDinMacroTotal());
        view.setDinFood(page.getDinner());
        /////////////////////////////////////////
        //snacks
        view.setSnaTotals(page.getSnaMacroTotal());
        view.setSnaFood(page.getSnack());
        /////////////////////////////////////////
    }

    public void updateMeal(MealType mealType){
        /*
        update the values only for the specific meal, and
        update the totals accordingly
         */
        ////day totals
        view.setKcalDay(page.getDayMacroTotal().getKcal(),this.totals.getKcal());
        view.setProteinDay(page.getDayMacroTotal().getProtein(),
                totals.getProtein());
        view.setFatDay(page.getDayMacroTotal().getFat(),
                totals.getFat());
        view.setCarbsDay(page.getDayMacroTotal().getCarbs(),
                totals.getCarbs());
        /////////////////////////////////////////

        switch (mealType){
            case BREAKFAST -> {
                view.setBreTotals(page.getBreMacroTotal());
                view.setBreFood(page.getBreakfast());
                break;
            }
            case LUNCH -> {
                view.setLunTotals(page.getLunMacroTotal());
                view.setLunFood(page.getLunch());
                break;
            }
            case DINNER -> {
                view.setDinTotals(page.getDinMacroTotal());
                view.setDinFood(page.getDinner());
                break;
            }
            case SNACK -> {
                view.setSnaTotals(page.getSnaMacroTotal());
                view.setSnaFood(page.getSnack());
                break;
            }
        }
    }

    public void addFood(MealType mealType){
        /*
        add a new food to the given meal
         */
        //System.out.println(mealType);
        if (addController == null) {
            // First initialize if needed
            addController = new AddController(this);
        }
        addController.setMealType(mealType);
        addController.showView(true);
    }

    public void setDayKcal(){
        String s= JOptionPane.showInputDialog("Total Daily Calories",
                this.totals.getKcal());

        int kcal;
        try {
            kcal=Integer.parseInt(s);
        }catch (Exception e){
            return;
        }

        if(kcal>0){
            this.totals.setKcal(kcal);
            writeConfig(initialize,totals);
        } else{
            JOptionPane.showMessageDialog(null,"Calories must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDayProtein(){
        String s=JOptionPane.showInputDialog("Total Daily Protein",
                (this.totals.getProtein().intValue()));

        float protein;
        try {
            protein=Integer.parseInt(s);
        }catch (Exception e){
            return;
        }

        if(protein>0){
            this.totals.setProtein(protein);
            writeConfig(initialize,totals);
        } else{
            JOptionPane.showMessageDialog(null,"Value must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDayFat(){
        String s=JOptionPane.showInputDialog("Total Daily Fat",
                (this.totals.getFat().intValue()));

        float fat;
        try {
            fat=Integer.parseInt(s);
        }catch (Exception e){
            return;
        }

        if(fat>0){
            this.totals.setFat(fat);
            writeConfig(initialize,totals);
        } else{
            JOptionPane.showMessageDialog(null,"Value must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setDayCarbs(){
        String s=JOptionPane.showInputDialog("Total Daily Carbs",
                (this.totals.getCarbs().intValue()));

        float carbs;
        try {
            carbs=Integer.parseInt(s);
        }catch (Exception e){
            return;
        }

        if(carbs>0){
            this.totals.setCarbs(carbs);
            writeConfig(initialize,totals);
        } else{
            JOptionPane.showMessageDialog(null,"Value must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dateView(){
        /*
        open the frame with the date view
         */
        if (dateController == null) {
            // First initialize if needed
            dateController = new DateController(this,date);
        }
        dateController.setDate(date);
        dateController.setVisibility(true);

    }

    public void entryView(EntryResponse entry,MealType mealType){
        /*
        modify the given entry
         */
        if (entryController == null) {
            // First initialize if needed
            entryController = new EntryController(this,entry);
        }
        entryController.setEntry(entry);
        entryController.setMealType(mealType);
        entryController.update();
        entryController.setVisible(true);

    }

    public void createFoodView(){
        /*
        create a new food item
         */
        if(SessionManager.getUser().getRole()!=Role.ROLE_ADMIN)
            return;

        if (createController == null) {
            // First initialize if needed
            createController = new CreateController(this);
        }
        createController.update();
        createController.setVisible(true);
    }

    public void createRecipeView(){
        /*
        create a new recipe item
         */
    }

    public void setWater(){
        /*
        modify the water amount for the page
         */
//        String s= JOptionPane.showInputDialog("Water Amount",this.page.getWater().getQuantity());
//
//        short quantity;
//        try {
//            quantity=Short.parseShort(s);
//        }catch (Exception e){
//            return;
//        }

    }

}

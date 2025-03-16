package frontend.controller;

import com.example.sams.DemoApplication;
import com.example.sams.controller.SessionManager;
import com.example.sams.entity.Food;
import com.example.sams.enumeration.MealType;
import com.example.sams.mapper.EntryMapper;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.response.FoodResponse;
import com.example.sams.service.IAuthService;
import com.example.sams.service.IEntryService;
import com.example.sams.service.IFoodService;
import com.example.sams.service.implementation.EntryService;
import com.example.sams.service.implementation.FoodService;
import frontend.view.AddView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.swing.*;
import java.sql.Date;
import java.util.ArrayList;

import com.example.sams.entity.Entry;

@Getter
@Setter
public class AddController {
    /*
    the controller for adding a new food
    The mealtype represents the meal to which we add the food
    foodlist represents the options available to add from
     */
    protected MealType mealType;
    protected AddView view;
    protected PageController pageController;
    protected ArrayList<FoodResponse> foodList;


    private IFoodService foodService;
    private Pageable page= PageRequest.of(0, 100);
    private IEntryService entryService;

    public AddController(PageController pageController){
        foodService=DemoApplication.context.getBean(IFoodService.class);
        entryService= DemoApplication.context.getBean(IEntryService.class);
        this.pageController=pageController;
        view=new AddView(this,true);

        foodList=new ArrayList<>(foodService.findAll(page).getContent());

        view.addFoodList(foodList,true);
    }

    public void clickedX(){
        /*
        cancelled the addition or closed the window
         */
        view.setVisibility(false);
        pageController.setDatePressed(false);
    }

    public void searchFood(JTextField searchBar)
    {
        /*
        search the food with the name on the searchbar
         */
        String s=searchBar.getText();
        foodList=new ArrayList<>(foodService.findByNameMatches(s,page).getContent());

        view.addFoodList(foodList,true);
    }

    public void searchRecipe(JTextField searchBar)
    {
        /*
        search the recipe with the name on the searchbar
         */
        //System.out.println(2222);
        //String s=searchBar.getText();
        //System.out.println(s);
    }

    public void clickedFood(FoodResponse food){
        /*
        add this food as an entry to current date
         */
        EntryRequest entryRequest=new EntryRequest(
                pageController.getDate().toLocalDate(),
                mealType.getMeal(),
                1f,
                food.foodId());
        EntryResponse response=entryService.create(SessionManager.getUser().getUserId(),entryRequest);
        clickedX();


        pageController.entryView(response,mealType);
        pageController.update();
    }

    public void showView(boolean show){
        view.reset();
        this.view.setVisibility(show);
    }
}

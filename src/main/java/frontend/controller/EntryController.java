package frontend.controller;

import com.example.sams.DemoApplication;
import com.example.sams.controller.SessionManager;
import com.example.sams.entity.Entry;
import com.example.sams.enumeration.MealType;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.service.IAuthService;
import com.example.sams.service.IEntryService;
import com.example.sams.service.implementation.EntryService;
import frontend.view.EntryView;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@Setter
public class EntryController {
    /*
    the controller for modifying an entry
     */

    protected EntryView view;
    protected EntryResponse entry;
    protected EntryResponse entryCopy;
    protected PageController pageController;
    protected MealType mealType;


    private IEntryService entryService;

    public EntryController(PageController pageController,EntryResponse entry) {
        this.entryService= DemoApplication.context.getBean(IEntryService.class);
        this.pageController=pageController;
        this.view=new EntryView(this);
        this.entry=entry;
        update();
        this.view.setVisibility(true);

        //make a copy of the initial entry in case we need to revert
        this.entryCopy=new EntryResponse(entry.entryId(), entry.date(),entry.meal(),entry.quantity(),entry.food());
    }

    public void setVisible(boolean visible) {
        view.reset();
        this.view.setVisibility(visible);
    }
    public EntryResponse getEntry() {
        return entry;
    }

    public void setEntry(EntryResponse entry) {
        this.entry = entry;
    }

    public void revertEntry(){
        this.entry=this.entryCopy;
    }
    public void clickedCheck(){
        /*
        close the tab
         */
        view.reset();
        view.setVisibility(false);
    }

    public void update(){
        view.update(entry,pageController.getTotals());
    }

    public void deleteEntry(){
        /*
        the method for deleting the entry
         */
        int input= JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this entry?","",
                JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
        //System.out.println(input);

        if(input==0){
            //yes
            entryService.deleteById(entry.entryId());

            pageController.update();
            clickedCheck();
        }else {
            //no
        }
    }

    public void setQuantity(){
        String s= JOptionPane.showInputDialog("Quantity",this.entry.quantity());

        float quantity;
        try {
            quantity=Float.parseFloat(s);
        }catch (Exception e){
            return;
        }

        if(quantity>0){
            entry=entry.withQuantity(quantity);
            EntryRequest request=new EntryRequest(
                    entry.date(),
                    entry.meal(),
                    entry.quantity(),
                    entry.food().getFoodId()
            );
            entryService.save(SessionManager.getUser().getUserId(), entry.entryId(), request);
            pageController.update();
        }else {
            JOptionPane.showMessageDialog(null,
                    "Quantity must be greater than 0","",JOptionPane.ERROR_MESSAGE);
        }
    }
}

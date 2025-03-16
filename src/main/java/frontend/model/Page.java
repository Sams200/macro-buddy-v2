package frontend.model;

import com.example.sams.entity.Entry;
import com.example.sams.entity.Macro;
import com.example.sams.response.EntryResponse;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
public class Page {
    /*
    a page is a collection of entries which are separated into multiple
    categories: breakfast, lunch, dinner, and snack

    the macro totals represent the sum of the macros for each individual
    meal
     */
    private ArrayList<EntryResponse> breakfast;
    private ArrayList<EntryResponse> lunch;
    private ArrayList<EntryResponse> dinner;
    private ArrayList<EntryResponse> snack;
    private Macro breMacroTotal;
    private Macro lunMacroTotal;
    private Macro dinMacroTotal;
    private Macro snaMacroTotal;
    private Macro dayMacroTotal;
    private Date date;


    public Page(Date date){
        this.date=date;
        this.breakfast=new ArrayList<>();
        this.lunch=new ArrayList<>();
        this.dinner=new ArrayList<>();
        this.snack=new ArrayList<>();

        this.breMacroTotal=new Macro(0,0f,0f,0f);
        this.lunMacroTotal=new Macro(0,0f,0f,0f);
        this.dinMacroTotal=new Macro(0,0f,0f,0f);
        this.snaMacroTotal=new Macro(0,0f,0f,0f);
        this.breMacroTotal=new Macro(0,0f,0f,0f);

    }

    public void addBreakfast(EntryResponse entryResponse){
        this.breakfast.add(entryResponse);
    }
    public void addLunch(EntryResponse entryResponse){
        this.lunch.add(entryResponse);
    }
    public void addDinner(EntryResponse entryResponse){
        this.dinner.add(entryResponse);
    }
    public void addSnack(EntryResponse entryResponse){
        this.snack.add(entryResponse);
    }
}
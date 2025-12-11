package ats.algo.core.incidentgenerator;

import java.util.ArrayList;
import java.util.List;

public class GuiDataComponent {


    private boolean onlyPeriodChangeSelectable;
    private String title;
    private String list1Name;
    private String list2Name;
    private List<String> list1;
    private List<String> list2;

    public GuiDataComponent(String title, String list1Name, String list2Name) {
        this.title = title;
        this.list1Name = list1Name;
        this.list2Name = list2Name;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
    }

    public boolean isOnlyPeriodChangeSelectable() {
        return onlyPeriodChangeSelectable;
    }

    public void setOnlyPeriodChangeSelectable(boolean onlyPeriodStartSelectable) {
        this.onlyPeriodChangeSelectable = onlyPeriodStartSelectable;
    }



    public void addToList1(String item) {
        list1.add(item);
    }

    public void addToList2(String item) {
        list2.add(item);
    }

    public String getTitle() {
        return title;
    }

    public String getList1Name() {
        return list1Name;
    }

    public String getList2Name() {
        return list2Name;
    }

    public List<String> getList1() {
        return list1;
    }

    public List<String> getList2() {
        return list2;
    }



}

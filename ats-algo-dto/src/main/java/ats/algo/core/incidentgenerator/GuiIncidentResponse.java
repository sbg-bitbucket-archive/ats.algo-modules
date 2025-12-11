package ats.algo.core.incidentgenerator;


public class GuiIncidentResponse {
    private int guiDataComponentIndex;
    private String sourceSystem;
    private String list1SelectedOption;
    private String list2SelectedOption;
    private int elapsedTime;



    public int getGuiDataComponentIndex() {
        return guiDataComponentIndex;
    }

    public void setGuiDataComponentIndex(int guiDataComponentIndex) {
        this.guiDataComponentIndex = guiDataComponentIndex;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getList1SelectedOption() {
        return list1SelectedOption;
    }

    public void setList1SelectedOption(String list1SelectedOption) {
        this.list1SelectedOption = list1SelectedOption;
    }

    public String getList2SelectedOption() {
        return list2SelectedOption;
    }

    public void setList2SelectedOption(String list2SelectedOption) {
        this.list2SelectedOption = list2SelectedOption;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }



}

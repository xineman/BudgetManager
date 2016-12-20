package nf.co.xine.budgetmanager.dataObjects;


public class Budget {
    private int categoryId;
    private double value;
    private int period;

    public Budget(int categoryId, double value, int period) {
        this.categoryId = categoryId;
        this.value = value;
        this.period = period;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getValue() {
        return value;
    }

    public int getPeriod() {
        return period;
    }
}

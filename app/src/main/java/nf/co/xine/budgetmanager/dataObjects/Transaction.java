package nf.co.xine.budgetmanager.dataObjects;

import java.util.Date;


public class Transaction {
    private int accountId;
    private String name;
    private int categoryId;
    private Date date;
    private double value;

    public Transaction(int accountId, String name, int categoryId, Date date, double value) {
        this.accountId = accountId;
        this.name = name;
        this.categoryId = categoryId;
        this.date = date;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getCategory() {
        return categoryId;
    }

    public double getValue() {
        return value;
    }

    public int getAccountId() {
        return accountId;
    }

    public void increaseAccountId(int value) {
        accountId += value;
    }
}

package nf.co.xine.budgetmanager.dataObjects;


//import android.os.Parcel;
//import android.os.Parcelable;

public class Account {
    private String name;
    private String type;
    private double value;
    private double startValue;
    private String currency;

    public Account(String name, String type, double startValue, String currency) {
        this.name = name;
        this.type = type;
        this.startValue = startValue;
        this.value = startValue;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

//    public int describeContents() {
//        return 0;
//    }
//
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(name);
//        out.writeString(type);
//        out.writeDouble(value);
//        out.writeString(currency);
//    }
//
//    public static final Parcelable.Creator<Account> CREATOR
//            = new Parcelable.Creator<Account>() {
//        public Account createFromParcel(Parcel in) {
//            return new Account(in);
//        }
//
//        public Account[] newArray(int size) {
//            return new Account[size];
//        }
//    };
//
//    private Account(Parcel in) {
//        name = in.readString();
//        type = in.readString();
//        value = in.readDouble();
//        currency = in.readString();
//    }

    @Override
    public String toString() {
        return name;
    }

    public void addToValue(double value) {
        this.value += value;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }
}

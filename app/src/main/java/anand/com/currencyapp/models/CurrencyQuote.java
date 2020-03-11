package anand.com.currencyapp.models;

import io.realm.RealmObject;

/**
 * Created by Anand M Joseph
 */
public class CurrencyQuote extends RealmObject {
    public int ID;
    public String code;
    public String name;
    public double quote; // Based on USD

    public CurrencyQuote(){

    }
    public CurrencyQuote(CurrencyQuote t){
        code = t.code;
        name = t.name;
        quote = t.quote;
    }
}

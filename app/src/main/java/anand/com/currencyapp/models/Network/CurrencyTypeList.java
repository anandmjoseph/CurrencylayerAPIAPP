package anand.com.currencyapp.models.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Anand M Joseph
 */
public class CurrencyTypeList {
    public boolean success;
    public String terms;
    public String privacy;
    public Map<String, String> currencies;

    public List<CurrencyType> getList() {
        List<CurrencyType> tmp = new ArrayList<>();

        for (String key : currencies.keySet()) {
            CurrencyType t = new CurrencyType();
            t.code = key;
            t.name = currencies.get(key);
            tmp.add(t);
        }
        return tmp;
    }
}
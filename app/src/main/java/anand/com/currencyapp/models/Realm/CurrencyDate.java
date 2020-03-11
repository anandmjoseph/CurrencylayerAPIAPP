package anand.com.currencyapp.models.Realm;

import anand.com.currencyapp.models.CurrencyQuote;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;
import java.util.List;

/**
 * Created by Anand M Joseph
 */
public class CurrencyDate extends RealmObject {

    @Required
    public String key;
    public RealmList<CurrencyQuote> quotes;

    public CurrencyDate(){ }

    public CurrencyDate(List<CurrencyQuote> data){
        quotes = new RealmList<>();

        for (int i = 0; i < data.size(); i++) {
            quotes.add(new CurrencyQuote(data.get(i)));
        }
    }

}

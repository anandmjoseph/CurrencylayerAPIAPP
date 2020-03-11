package anand.com.currencyapp.managers;

import anand.com.currencyapp.models.CurrencyQuote;
import android.content.Context;
import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand M Joseph
 *
 * Singleton for keeping data while app is running
 */
public class Repository {
    private static final String TAG = Repository.class.getName();

    public Context mAppContext = null;
    public int selectedID = -1;
    public double baseValue = 1.0;
    public List<List<CurrencyQuote>> data = null;
    private List<CurrencyQuote> lastList = null;

    private int lastID = -1;

    // Singleton ...
    private static final Repository ourInstance = new Repository();
    public static Repository getInstance() {
        return ourInstance;
    }
    private Repository() {}

    /**
     * Calculate quotes for given currency
     *
     * @param id Currency id in list
     * @return Calculated currencies
     */
    public List<CurrencyQuote> getQuotes(int id){

        if ( lastID == id ){
            return lastList;
        }

        List<CurrencyQuote> tmp = new ArrayList<>();

        for (int i = 0; i < this.getDataForToday().size(); i++) {
            CurrencyQuote t = new CurrencyQuote(this.getDataForToday().get(i));
            tmp.add(t);
        }

        CurrencyQuote x = tmp.get(id);
        tmp.remove(id);

        double usdQ = baseValue / x.quote;

        for (int i = 0; i < tmp.size(); i++) {
            double t = tmp.get(i).quote;
            t = t * usdQ;
            tmp.get(i).quote = t;
        }

        lastID = id;
        lastList = tmp;
        return tmp;
    }


    /**
     * Get data for current day
     *
     * @return List of data for current day
     */
    public List<CurrencyQuote> getDataForToday(){ return getDataForDay(0);}

    /**
     * Get data for given day
     *
     * @param i Index of day to get data for
     * @return List of data for given day
     */
    public List<CurrencyQuote> getDataForDay(int i){ return data.get(i); }

    /**
     * Get data for last 7 days for given currency.
     * It returns data used for LineChart.
     *
     * @param id Id of currency
     * @return Data for last 7 days
     */
    public ArrayList<Entry> getCurrencyForWeek(int id){
        ArrayList<Entry> data = new ArrayList<>();

        for (int i = 0; i < this.data.size(); i++) {
            data.add(new Entry((float) this.data.get(i).get(id).quote,i));
        }

        return data;
    }

}

package anand.com.currencyapp.managers;

import anand.com.currencyapp.models.API.CurrencyAPI;
import anand.com.currencyapp.models.Network.CurrencyRateList;
import anand.com.currencyapp.models.Network.CurrencyTypeList;
import anand.com.currencyapp.utils.DateHelper;
import anand.com.currencyapp.utils.ServiceGenerator;
import android.util.Log;
import java.util.GregorianCalendar;
import retrofit2.Call;

/**
 * Created by Anand M Joseph
 *
 * Manager for handling network calls
 */
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getName();
    private final CurrencyAPI currencyAPI;

    /**
     * Create one service per instance
     */
    public NetworkManager(){
        currencyAPI = ServiceGenerator.createServiceAuth(CurrencyAPI.class);
    }

    /**
     * Get data for current day
     * @return Data for current day
     */
    private CurrencyRateList getDataForToday() {
        Call<CurrencyRateList> call = currencyAPI.getLiveQuotes();
        return this.execute(call);
    }

    /**
     * Get data for specific date
     * @param date Date to get data for
     * @return Date for given date
     */
    public CurrencyRateList getDataForDate(GregorianCalendar date) {
        // today
        if ( DateHelper.getKeyForDate(date).equals(DateHelper.getKeyForDate(new GregorianCalendar())))
            return getDataForToday();

        Log.d(TAG,"Getting data for day" + DateHelper.getKeyForDate(date));
        Call<CurrencyRateList> call = currencyAPI.getQuotesForDate(DateHelper.getKeyForDate(date));
        return this.execute(call);
    }

    /**
     * Get all currency types
     *
     * @return All currency types
     */
    public CurrencyTypeList getCurrencyTypes() {

        Call<CurrencyTypeList> call = currencyAPI.getAllCurrencies();
        return this.execute(call);
    }

    /**
     * Execute API call
     * @param call Call to
     * @return Body of called API
     */
    private <S>S execute(Call<S> call){
        S result = null;

        try {
           result = call.execute().body();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return  result;
    }
}

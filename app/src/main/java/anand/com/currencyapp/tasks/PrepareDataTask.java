package anand.com.currencyapp.tasks;

import anand.com.currencyapp.managers.NetworkManager;
import anand.com.currencyapp.managers.Repository;
import anand.com.currencyapp.managers.StorageManager;
import anand.com.currencyapp.models.CurrencyQuote;
import anand.com.currencyapp.models.Network.CurrencyRateList;
import anand.com.currencyapp.models.Network.CurrencyTypeList;
import anand.com.currencyapp.models.Realm.CurrencyDate;
import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Ammar Hadzic on 4/8/16.
 *
 * Get data from DB, if not get it from API
 */
public class PrepareDataTask extends AsyncTask<Void, Void, List<List<CurrencyQuote>>> {
    private static final String TAG = PrepareDataTask.class.getName();
    public PrepareDataTaskInterface delegate = null;

    private final NetworkManager n = new NetworkManager();
    private StorageManager db = null;
    private CurrencyTypeList currencyTypes = null;
    private final List<List<CurrencyQuote>> data = new ArrayList<>();

    @Override
    protected List<List<CurrencyQuote>> doInBackground(Void... params) {
        db = new StorageManager();
        try {
            loopTroughDays();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(List<List<CurrencyQuote>> data) {
        if (data.size() != 7) {
            delegate.prepareDataFinished(false);
        } else {
            Repository.getInstance().data = data;
            delegate.prepareDataFinished(true);
        }
    }

    /**
     * Loop trough last seven days
     */
    private void loopTroughDays() {
        GregorianCalendar today = new GregorianCalendar();

        for (int i = 0; i < 7; i++) {
            Log.d(TAG, "Getting data for day " + i);
            List<CurrencyQuote> currencyQuotes = this.gedDataForDay(today);
            if (currencyQuotes == null) {
                return;
            }
            data.add(currencyQuotes);
            today.add(Calendar.DAY_OF_YEAR, -1); // Previous day
        }
    }

    /**
     * Get data for given dated
     * First check DB, and than go to API
     *
     * @param date Date to check data for
     * @return List of data for given day
     */
    private List<CurrencyQuote> gedDataForDay(GregorianCalendar date) {
        CurrencyDate dataForDay = db.getDataForDay(date);

        List<CurrencyQuote> today;

        if (dataForDay == null) {

            currencyTypes = n.getCurrencyTypes();
            CurrencyRateList dataForDate = n.getDataForDate(date);
            if (dataForDate == null) {
                return null;
            }
            today = this.setNameForCode(dataForDate.getList());

            Log.d(TAG, "Got data from API");
        } else {
            Log.d(TAG, "Got data from DB");

            //Copy data
            today = new ArrayList<>();
            for (int i = 0; i < dataForDay.quotes.size(); i++) {
                CurrencyQuote currencyQuote = new CurrencyQuote();
                currencyQuote.ID = i;
                currencyQuote.name = dataForDay.quotes.get(i).name;
                currencyQuote.code = dataForDay.quotes.get(i).code;
                currencyQuote.quote = dataForDay.quotes.get(i).quote;
                today.add(currencyQuote);
            }
        }
        return today;
    }

    /**
     * Add full names of currencies based on their code
     *
     * @param list List of currencies to add full name
     * @return Modified list of currencies with full name
     */
    private List<CurrencyQuote> setNameForCode(List<CurrencyQuote> list) {

        if (currencyTypes != null) {
            for (int i = 0; i < list.size(); i++)
                list.get(i).name = currencyTypes.currencies.get(list.get(i).code);
        }

        return list;
    }
}
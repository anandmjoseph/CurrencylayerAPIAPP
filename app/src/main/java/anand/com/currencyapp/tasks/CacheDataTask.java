package anand.com.currencyapp.tasks;

import anand.com.currencyapp.managers.Repository;
import anand.com.currencyapp.managers.StorageManager;
import anand.com.currencyapp.models.Realm.CurrencyDate;
import anand.com.currencyapp.utils.DateHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ammar Hadzic on 4/8/16.
 *
 * Cache data for later use
 * Save data to DB if it isn't already saved
 */
public class CacheDataTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = CacheDataTask.class.getName();
    private StorageManager db = null;

    @Override
    protected Void doInBackground(Void... params) {
        db = new StorageManager();
        loopTroughDays();
        return null;
    }

    /**
     * Loop trough last seven days
     */
    private void loopTroughDays() {
        GregorianCalendar today = new GregorianCalendar();

        for (int i = 0; i < 7; i++) {
            this.saveDataForDay(today, i);
            today.add(Calendar.DAY_OF_YEAR, -1); // Previous day
        }
    }

    /**
     * Save data for given day if it isn't already saved
     *
     * @param date Date to save data for
     * @param i Index of day
     */
    private void saveDataForDay(GregorianCalendar date, int i) {
        CurrencyDate dataForDay = db.getDataForDay(date);

        if (dataForDay == null) {
            CurrencyDate tmp = new CurrencyDate(Repository.getInstance().getDataForDay(i));
            tmp.key = DateHelper.getKeyForDate(date);
            db.setDataForDay(tmp, date);
            Log.d(TAG, "Data cached fot day " + i);
        } else {
            Log.d(TAG, "Data already exists for day " + i);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(Repository.getInstance().mAppContext, "Data saved for offline use.",
        Toast.LENGTH_LONG).show();
    }
}

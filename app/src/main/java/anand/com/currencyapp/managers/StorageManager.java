package anand.com.currencyapp.managers;

import anand.com.currencyapp.models.Realm.CurrencyDate;
import anand.com.currencyapp.utils.DateHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import java.util.GregorianCalendar;

/**
 *  Created by Anand M Joseph
 */
public class StorageManager {
    private static final String TAG = StorageManager.class.getName();

    private final Realm realm;
    public StorageManager(){
       // RealmConfiguration realmConfig = new RealmConfiguration.Builder(Repository.getInstance().mAppContext).build();
        RealmConfiguration  realmConfig = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfig);
    }

    /**
     * Get data for given day
     *
     * @param date for database search
     * @return CurrencyDate
     */
    public CurrencyDate getDataForDay(GregorianCalendar date){

        String key = DateHelper.getKeyForDate(date);
        return  realm.where(CurrencyDate.class).equalTo("key",key).findFirst();
    }

    /**
     * Save data for given day
     *
     * @param data  Data to save in database
     * @param date  Date for given data
     */
    public void setDataForDay(CurrencyDate data, GregorianCalendar date){
        data.key = DateHelper.getKeyForDate(date);
        realm.beginTransaction();
        realm.copyToRealm(data);
        realm.commitTransaction();
    }

    /**
     *  Clear all data from cache
     */
    public void clearDB(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
}

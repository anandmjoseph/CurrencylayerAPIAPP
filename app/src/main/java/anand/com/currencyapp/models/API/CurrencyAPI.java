package anand.com.currencyapp.models.API;


import anand.com.currencyapp.models.Network.CurrencyRateList;
import anand.com.currencyapp.models.Network.CurrencyTypeList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Anand M Joseph
 */
public interface CurrencyAPI {

    // FIXME: Remove key from url to builder
    @GET("api/list")
    Call<CurrencyTypeList> getAllCurrencies();

    @GET("api/live")
    Call<CurrencyRateList> getLiveQuotes();

    @GET("api/historical")
    Call<CurrencyRateList> getQuotesForDate(@Query("date") String date);
}

package anand.com.currencyapp.fragments;

import anand.com.currencyapp.R;
import anand.com.currencyapp.managers.Repository;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrencyWeekOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrencyWeekOverviewFragment extends Fragment {

    private static final String TAG = CurrencyWeekOverviewFragment.class.getName();
    private static final String ARG_CURRENCY_ID = "currencyID";

    @BindView(R.id.weekChart) LineChart weekChart;

    public CurrencyWeekOverviewFragment() {
        // Required empty public constructor
    }

    public static CurrencyWeekOverviewFragment newInstance(int currencyID) {
        CurrencyWeekOverviewFragment fragment = new CurrencyWeekOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENCY_ID, currencyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
        inflater.inflate(R.layout.fragment_currency_week_overview, container, false);
        ButterKnife.bind(this, rootView);
        int id = getArguments().getInt(ARG_CURRENCY_ID);

        Log.d(TAG, "Currency ID " + id);

        weekChart.setData(prepareChartData(id));
        weekChart.setTouchEnabled(false);
        weekChart.invalidate();
        return rootView;
    }

    /**
     * Prepare data for {@link LineChart}
     *
     * @param id Currency id to get data for
     * @return Data for {@link LineChart}
     */
    private LineData prepareChartData(int id) {
        // creating list of entry
        ArrayList<Entry> entries = Repository.getInstance().getCurrencyForWeek(id);
        LineDataSet lineDataSet = new LineDataSet(entries, "Quote based on USD");
        lineDataSet.setColor(Color.parseColor("#3F51B5"));
        lineDataSet.setFillColor(Color.parseColor("#C6CCEB"));
        lineDataSet.setCircleColor(Color.parseColor("#212A5E"));
        lineDataSet.setCircleRadius(6);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(15);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        return new LineData(dataSets);
    }
}


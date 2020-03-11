package anand.com.currencyapp.adapters;

import anand.com.currencyapp.R;
import anand.com.currencyapp.managers.Repository;
import anand.com.currencyapp.models.CurrencyQuote;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anand M Joseph (anand.joseph@plexure.com)
 */
public class CurrencyTypeListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater = null;
    public List<CurrencyQuote> mData = null;

    public CurrencyTypeListAdapter(Activity context) {
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = Repository.getInstance().getDataForToday();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mLayoutInflater.inflate(R.layout.currency_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.code.setText(mData.get(position).code);
        holder.name.setText(mData.get(position).name);
        return convertView;
    }

    /**
     * Bind properties to view
     */
    static class ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.code) TextView code;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void filter(String part){

        List<CurrencyQuote> tmp = new ArrayList<>();

        List<CurrencyQuote> data = Repository.getInstance().getDataForToday();
        for (int i = 0; i <data.size(); i++) {

            CurrencyQuote t = data.get(i);

            if(t.code.toLowerCase().contains(part) || t.name.toLowerCase().contains(part)){
                tmp.add(t);
            }
        }

        this.mData = tmp;
        this.notifyDataSetChanged();
    }
}

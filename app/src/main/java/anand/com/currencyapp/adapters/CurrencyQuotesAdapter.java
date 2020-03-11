package anand.com.currencyapp.adapters;

import anand.com.currencyapp.R;
import anand.com.currencyapp.managers.Repository;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Anand M Joseph (anand.joseph@plexure.com)
 */
public class CurrencyQuotesAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private int currencyID = 0;

    public CurrencyQuotesAdapter(Activity context,int currencyID) {
        this.currencyID = currencyID;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Repository.getInstance().getQuotes(currencyID).size();
    }

    @Override
    public Object getItem(int position) {
        return Repository.getInstance().getQuotes(currencyID).get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.currency_quotes_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.code.setText(Repository.getInstance().getQuotes(currencyID).get(position).code);
        holder.name.setText(Repository.getInstance().getQuotes(currencyID).get(position).name);

        holder.quote.setText(String.format("%.4f", Repository.getInstance().getQuotes(currencyID).get(position).quote));
        return convertView;
    }

    /**
     * Bind properties to view
     */
    static class ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.code) TextView code;
        @BindView(R.id.quote) TextView quote;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

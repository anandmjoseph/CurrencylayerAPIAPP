package anand.com.currencyapp.activities;

import anand.com.currencyapp.R;
import anand.com.currencyapp.adapters.CurrencyTypeListAdapter;
import anand.com.currencyapp.adapters.LeftDrawerAdapter;
import anand.com.currencyapp.fragments.ChangeAmountFragment;
import anand.com.currencyapp.managers.Repository;
import anand.com.currencyapp.managers.StorageManager;
import anand.com.currencyapp.tasks.CacheDataTask;
import anand.com.currencyapp.tasks.PrepareDataTask;
import anand.com.currencyapp.tasks.PrepareDataTaskInterface;
import anand.com.currencyapp.ui.CurrencyOverviewActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.realm.Realm;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PrepareDataTaskInterface {

    private static final String TAG = MainActivity.class.getName();
    private ProgressDialog mDialog;
    private ActionBarDrawerToggle mDrawerToggle;
    private CurrencyTypeListAdapter adapter;

    List<String> menuItems = Arrays.asList("Change amount", "Clear cache", "Exit");

    @BindView(R.id.currencyList) ListView currencyListView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer) ListView leftDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Repository.getInstance().mAppContext = getApplicationContext();
        Realm.init(this);
        this.setupToolbar();
        prepareData();
    }

    private void  setupToolbar() {
        toolbar.setTitle("Currency list from ApiLayer");
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_36dp);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
        toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        leftDrawer.setAdapter(new LeftDrawerAdapter(this, menuItems));

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_currency_overview, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    private Activity getActivityContext() {
        return this;
    }

    //region Data preparation
    private void prepareData() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        mDialog.show();

        PrepareDataTask t = new PrepareDataTask();
        t.delegate = this;
        t.execute();
    }

    @Override
    public void prepareDataFinished(Boolean finished) {
        Log.d(TAG, finished ? "FINISHED" : "NOT FINISHED");
        if (finished) {
            adapter = new CurrencyTypeListAdapter(MainActivity.this.getActivityContext());
            currencyListView.setAdapter(adapter);
            if (mDialog != null)
                mDialog.cancel();
            CacheDataTask  task = new CacheDataTask();
            task.execute();
        }
    }
    //endregion

    //region UI Events

    @OnItemClick(R.id.left_drawer)
    void onDrawerItemSelected(int position){

        mDrawerLayout.closeDrawers();

        switch (position){
            case 0:
                this.showChangeAmountDialog();
                break;
            case 1:
                // Clear data from DB
                StorageManager s = new StorageManager();
                s.clearDB();
                prepareData();
                break;
            case 2:
                finish();
                break;
        }
    }

    @OnItemClick(R.id.currencyList)
    void onCurrencyItemSelected(int position) {
        Repository.getInstance().selectedID = adapter.mData.get(position).ID;
        Intent intent = new Intent(this, CurrencyOverviewActivity.class);
        startActivity(intent);
    }

    //endregion

    void showChangeAmountDialog(){
        ChangeAmountFragment dialog = new ChangeAmountFragment();
        dialog.show(getFragmentManager(),"Change base amount");
    }
}
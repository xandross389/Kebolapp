package com.inbitart.kebolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.inbitart.kebolapp.controller.SQLiteDatabaseHelper;
import com.inbitart.kebolapp.model.Country;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchCountryActivity extends AppCompatActivity {

    private SQLiteDatabaseHelper sqliteDatabaseHelper = null;
    private SearchView searchView;
    private RecyclerView recViewSearchResults;
    private SearchCountryResultsAdapter adaptador;
    private List<Country> searchResultCountryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_country);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResultCountryList = new ArrayList<>();

        populateDataBase("Afganistán", "93", "Afghanistan");
        populateDataBase("Albania", "355", "");
        populateDataBase("Cuba", "53", "");
        populateDataBase("Estados Unidos", "1", "United States");
        populateDataBase("Alemania", "49", "Germany");
        populateDataBase("Brasil", "55", "Brazil");
        populateDataBase("Canadá", "1", "Canada");
        populateDataBase("Chile", "56", "");
        populateDataBase("Ecuador", "593", "");
        populateDataBase("España", "34", "Spain");
        populateDataBase("Italia", "39", "Italy");
        populateDataBase("México", "56", "Mexico");
        populateDataBase("Puerto Rico", "1", "");

        initViewObjects();
        initRecyclerView();

    }

    private void initRecyclerView() {
        // fill searchResultCountryList
        try {
            final Dao<Country, Integer> countryDao = getSQLiteDatabaseHelper().getCountryDao();
            searchResultCountryList.clear();
            searchResultCountryList = countryDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        adaptador = new SearchCountryResultsAdapter(
                getApplicationContext(), (ArrayList<Country>) searchResultCountryList);

        //Inicialización RecyclerView
        recViewSearchResults = (RecyclerView) findViewById(R.id.recViewSerachResults);

        recViewSearchResults.setAdapter(adaptador);
        recViewSearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewSearchResults.setItemAnimator(new DefaultItemAnimator());

        /*// set itemClickLIstener
        adaptador.setOnItemClickListener(new CountryCodeItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(getApplicationContext(), Integer.toString(position),
                        Toast.LENGTH_LONG).show();

                returnSelectedCountryAndFinish(position);



            }
        });*/

        //  set SearchResultsAdapter
      //  adaptador = (SearchCountryResultsAdapter) recViewSearchResults.getAdapter();
      //  adaptador.refreshAdapterData((ArrayList<Country>) searchResultCountryList);
     //   adaptador.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_OK);
        finish();
    }

    private void populateDataBase(String name, String code, String desc) {
        Country newCountry = new Country(name, code, desc);

        // inserto algunos registros en la BD
        try {
            final Dao<Country, Integer> insertCountry = getSQLiteDatabaseHelper().getCountryDao();

            insertCountry.create(newCountry);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initViewObjects() {
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {

                  search(query);

                } else {
                    Toast.makeText(getApplicationContext(),
                           getString(R.string.search_country_specify_valid_criteria),
                            Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {

                   search(newText);
                }
                return false;
            }
        });
    }

    private void search(String pattern){
        searchResultCountryList.clear();

        if (pattern.length() == 0) {

            try {
                final Dao<Country, Integer> countryDao = getSQLiteDatabaseHelper().getCountryDao();
                searchResultCountryList = countryDao.queryForAll();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            try {
                final Dao<Country, Integer> countryDao = getSQLiteDatabaseHelper().getCountryDao();
                searchResultCountryList = countryDao.query(
                            countryDao.queryBuilder().where()
                            .like(Country.NAME_FIELD_NAME, "%" + pattern + "%")
                            .or()
                            .like(Country.DESCRIPTION_FIELD_NAME, "%" + pattern + "%")
                            .or()
                            .like(Country.CODE_FIELD_NAME, "%" + pattern + "%")
                            .prepare());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //  SearchResultsAdapter
       // adaptador = (SearchCountryResultsAdapter) recViewSearchResults.getAdapter();
        adaptador.refreshAdapterData((ArrayList<Country>) searchResultCountryList);
        adaptador.notifyDataSetChanged();
    }

    private SQLiteDatabaseHelper getSQLiteDatabaseHelper() {
        if (sqliteDatabaseHelper == null) {
            sqliteDatabaseHelper = OpenHelperManager
                    .getHelper(getApplicationContext(), SQLiteDatabaseHelper.class);
        }
        return sqliteDatabaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sqliteDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            sqliteDatabaseHelper = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        adaptador.setOnItemClickListener(new CountryCodeItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                returnSelectedCountryAndFinish(position);
            }
        });
    }

    private void returnSelectedCountryAndFinish(Integer position) {
        Bundle selectedCuntryBundle = new Bundle();

        selectedCuntryBundle.putString(RegisterActivity.COUNTRY_CODE,
                searchResultCountryList.get(position).getCode());
        selectedCuntryBundle.putString(RegisterActivity.COUNTRY_NAME,
                searchResultCountryList.get(position).getName());
        selectedCuntryBundle.putString(RegisterActivity.COUNTRY_DESCRIPTION,
                searchResultCountryList.get(position).getDescription());

        Intent intent = new Intent();
        intent.putExtras(selectedCuntryBundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}

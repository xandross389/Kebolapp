package com.inbitart.kebolapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inbitart.kebolapp.controller.SQLiteDatabaseHelper;
import com.inbitart.kebolapp.model.Country;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    public static final int SEARCH_COUNTRY_REQUEST_CODE = 2740;
    public static final String COUNTRY_NAME = "country_name";
    public static final String COUNTRY_CODE = "country_code";
    public static final String COUNTRY_DESCRIPTION = "country_description";

    private final Context mContext = this;
    private SQLiteDatabaseHelper sqliteDatabaseHelper = null;
    private Spinner spinnerCountries;
  //  private FloatingActionButton fabRegistrer;
    private ImageButton fabRegistrer;
    private EditText edtCountryCode;
    private EditText edtCountry;
    private EditText edtPhoneNumber;
    private Country selectedCountry;
    private Button btnCountry;

    private String[] countryCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fillCountriesArrayData();
        initViewObjects();

        fabRegistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) { // if user is registered ...
                    // Start the next activity
                    Intent mainIntent = new Intent().setClass(
                            RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }
            }
        });

    }

    public void initViewObjects(){
        fabRegistrer = (ImageButton) findViewById(R.id.fabRegister);
        edtCountryCode = (EditText) findViewById(R.id.edtCountryCode);
        btnCountry = (Button) findViewById(R.id.btnCountry);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
        edtPhoneNumber.requestFocus();
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchCountryActivity.class);
                startActivityForResult(searchIntent, SEARCH_COUNTRY_REQUEST_CODE);
            }
        });
    }

    private Country getCountryByCode(String countryCode) {
        try {
            final Dao<Country, Integer> countryDao = getSQLiteDatabaseHelper().getCountryDao();
            return countryDao.queryForFirst(
                    countryDao.queryBuilder().where()
                            .eq(Country.CODE_FIELD_NAME, countryCode)
                            .prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_COUNTRY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();

                if (bundle.getString(COUNTRY_CODE) != "") {
                    selectedCountry = new Country();
                    selectedCountry.setName( bundle.getString(COUNTRY_NAME) );
                    selectedCountry.setDescription( bundle.getString(COUNTRY_DESCRIPTION) );
                    selectedCountry.setCode( bundle.getString(COUNTRY_CODE) );
                    edtCountryCode.setText(selectedCountry.getCode());
                    btnCountry.setText(selectedCountry.getName());
                    edtPhoneNumber.requestFocus();

                } else {
                    edtCountryCode.setText("");
                    edtCountryCode.requestFocus();
                }
            }
        }
    }

    private void fillCountriesArrayData() {

        try {
            final Dao<Country, Integer> countryDao = getSQLiteDatabaseHelper().getCountryDao();
            List<Country> countriesList;
            countriesList = countryDao.queryForAll();
            countryCodes = new String[countriesList.size()];
            if (countriesList.size() > 0) {
                for (int i = 0; i < countriesList.size(); i++) {
                    countryCodes[i] = countriesList.get(i).getName()
                                      +" (+"+countriesList.get(i).getCode()+")";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDatabaseHelper getSQLiteDatabaseHelper() {
        if (sqliteDatabaseHelper == null) {
            sqliteDatabaseHelper = OpenHelperManager
                    .getHelper(getApplicationContext(), SQLiteDatabaseHelper.class);
        }
        return sqliteDatabaseHelper;
    }
}

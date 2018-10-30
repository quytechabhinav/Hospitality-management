package cityscann.com.city_scann.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Search extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = Search.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    Button search_button;
    EditText search_bar;
    String search;
    ProgressDialog pd;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    private ConnectionDetector cd;
    GPSTracker gps;
    ImageView seach;
    ImageButton img1, img2, img3,img4;
    LinearLayout sear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        if( preferencesClass.getUserToken() != null ){

            sear = (LinearLayout) findViewById(R.id.sear);
            sear.setVisibility(View.VISIBLE);
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                defineviewforitem();
            }
        }else {
            Intent i = new Intent(Search.this, selection_hotel.class);
            startActivity(i);

        }
    }

    private void defineviewforitem() {
        seach=(ImageView)findViewById(R.id.logout);
        seach.setVisibility(View.GONE);
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesClass.setUserToken(null);
                preferencesClass.setusername(null);
                preferencesClass.setphone(null);
                preferencesClass.setuser_email(null);
                preferencesClass.setStateName(null);
                Intent i = new Intent(Search.this, Home.class);
                startActivity(i);
            }

        });

        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("CityScann");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search_bar = (EditText)findViewById(R.id.search_bar);
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        /* img3 = (ImageButton) findViewById(R.id.img3);
       img4 = (ImageButton) findViewById(R.id.img4);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/

        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    isInternetPresent = cd.isConnectingToInternet();
                    isGPSPresent = cd.isGPSon();
                    if (isInternetPresent)
                    {
                        if(isGPSPresent) {
                            if (checkValidation()) {
                                search();
                            }
                        }else{
                            showSettingsAlert();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        search_button = (Button)findViewById(R.id.search_button);

        search_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        isGPSPresent = cd.isGPSon();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.search_button:
                    if (isGPSPresent) {
                        if (checkValidation()) {
                            search();
                        }
                    } else {
                        showSettingsAlert();
                    }
                    break;

                case R.id.img1:
                    Intent home = new Intent(this, Home.class);
                    startActivity(home);
                    break;
               /* case R.id.img4:
                    final Dialog dialog = new Dialog(Search.this);
                    dialog.setContentView(R.layout.logout);
                    dialog.show();

                    break;*/

                case R.id.img2:
                    Intent category = new Intent(this, CategoryList.class);
                    startActivity(category);
            }
        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Location");

        alertDialog.setMessage("Location is not enabled. Enable Now?");

        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void search() {
        search = search_bar.getText().toString();

        Intent i = new Intent(Search.this,SearchResult.class);
        i.putExtra("search",search);
        startActivity(i);
    }

    private boolean checkValidation() {
        search = search_bar.getText().toString();
        if (search.length() == 0) {
            search_bar.requestFocus();
            search_bar.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, CategoryList.class);
        startActivity(i);
    }
}

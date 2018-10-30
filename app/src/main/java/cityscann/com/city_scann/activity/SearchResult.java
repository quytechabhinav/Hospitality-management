package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.SearchAdapter;
import cityscann.com.city_scann.listener.RecyclerItemClickListener;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class SearchResult extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String TAG = SearchResult.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    EditText search_bar;
    Button search_button;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    private List<SupplierItem> searchsuppList, searchlinklist, searchcatlist, filteredSearchlist;
    ProgressDialog pd;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    private ConnectionDetector cd;
    String search, isSub_cat, isSupp, isUsefulLink;
    LinearLayout nodata;
    LinearLayout data;
    GPSTracker gps;
    double latitude, longitude, sup_lat, sup_long;
    ImageButton img1, img2, img3;
    Spinner spinnersort;
    TextView mapView, filter;
    private String[] sortbyList ={"Distance", "Rating"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        mContext = getApplicationContext();

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {
            defineviewforitem();
            definerecyclerview();
        }
    }

    private void defineviewforitem() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Search Results");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search = getIntent().getExtras().getString("search");

        spinnersort = (Spinner) findViewById(R.id.spinner);
        search_bar = (EditText)findViewById(R.id.search_bar);
        search_button = (Button)findViewById(R.id.search_button);
        data = (LinearLayout)findViewById(R.id.data);
        nodata = (LinearLayout)findViewById(R.id.no_data);
        filter = (TextView) findViewById(R.id.filter);
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageButton) findViewById(R.id.img3);
        mapView = (TextView) findViewById(R.id.mapview);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        mapView.setOnClickListener(this);
        filter.setOnClickListener(this);

        spinnersort.setOnItemSelectedListener(this);
        ArrayAdapter<String> sortby = new ArrayAdapter<String>(this, R.layout.drawable, sortbyList);
        sortby.setDropDownViewResource(R.layout.drawable);
        spinnersort.setAdapter(sortby);

        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    isInternetPresent = cd.isConnectingToInternet();
                    isGPSPresent = cd.isGPSon();
                    if (isInternetPresent) {
                        isGPSPresent = cd.isGPSon();
                        if(isGPSPresent) {
                            if (checkValidation()) {
                                downloadList();
                            }
                        }else {
                            showSettingsAlert();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        search_bar.setText(search);

        search_button.setOnClickListener(this);
    }

    private void definerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                isGPSPresent = cd.isGPSon();
                if (isGPSPresent) {
                    Intent intent = new Intent(SearchResult.this, SupplierDetail.class);
                    intent.putExtra("item", (Serializable) searchsuppList);
                    intent.putExtra("Positions", position);
                    startActivity(intent);
                } else {
                    showSettingsAlert();
                }
            }
        }));
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Location");

        alertDialog.setMessage("Location is not enabled. Enable Now?");

        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        downloadList();
    }

    private void downloadList() {
        search = search_bar.getText().toString();

        data.setVisibility(View.VISIBLE);
        map=new HashMap<>();
        searchsuppList = new ArrayList<>();
        searchcatlist = new ArrayList<>();
        searchlinklist = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        String wrev = search;
        wrev = wrev.replace(" ","%20");

        Log.d("searchurl", getResources().getString(R.string.ip_address) + "request_search.php?query=" + wrev
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName() + "&propertyid=" + preferencesClass.getHid());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_search.php?query=" + wrev
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName() + "&propertyid=" + preferencesClass.getHid(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);
                    } else if (response.getString("status").equals("-1")) {
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(SearchResult.this, Login.class);
                        startActivity(i);
                        finish();
                    } else if (response.getString("status").equals("1")) {
                        nodata.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SupplierItem supplierItem = new SupplierItem();
                            supplierItem.setSup_id(jsonObject.getString("id"));
                            supplierItem.setSup_name(jsonObject.getString("name"));
                            supplierItem.setSup_rate(jsonObject.getString("rating"));
                            supplierItem.setSup_lat(jsonObject.getString("latitude"));
                            sup_lat = Double.parseDouble(jsonObject.getString("latitude"));
                            sup_long = Double.parseDouble(jsonObject.getString("longitude"));
                            float distance = getDistanceInMeter(sup_lat, sup_long);
                            distance = (distance/1000);
                            String distString = String.format("%.1f", distance);
                            supplierItem.setSup_dist(distString);
                            supplierItem.setCat_id(jsonObject.getString("catid"));
                            supplierItem.setCat_name(jsonObject.getString("catname"));
                            supplierItem.setSup_long(jsonObject.getString("longitude"));
                            supplierItem.setSup_img(jsonObject.getString("image"));
                            supplierItem.setSup_img2(jsonObject.getString("image2"));
                            supplierItem.setAbt_sup(jsonObject.getString("detail"));
                            supplierItem.setSup_link(jsonObject.getString("website"));
                            supplierItem.setSup_add(jsonObject.getString("address"));
                            supplierItem.setSup_city(jsonObject.getString("city"));
                            supplierItem.setSup_state(jsonObject.getString("state"));
                            supplierItem.setSup_country(jsonObject.getString("country"));
                            supplierItem.setSup_zip(jsonObject.getString("zip"));
                            supplierItem.setSup_phone1(jsonObject.getString("phone1"));
                            supplierItem.setSup_phone2(jsonObject.getString("phone2"));
                            supplierItem.setCat_label(jsonObject.getString("catlabel"));
                            supplierItem.setCuisines(jsonObject.getString("cuisines"));
                            supplierItem.setCostfortwo(jsonObject.getString("costfortwo"));
                            supplierItem.setHours(jsonObject.getString("hours"));
                            supplierItem.setCouponcode(jsonObject.getString("couponcode"));
                            supplierItem.setBrand(jsonObject.getString("brand"));
                            supplierItem.setOpeningdays(jsonObject.getString("openingdays"));
                            supplierItem.setFromtime(jsonObject.getString("fromtime"));
                            supplierItem.setTotime(jsonObject.getString("totime"));

                            searchsuppList.add(supplierItem);

                        }

                        Collections.sort(searchsuppList, new Comparator<SupplierItem>() {
                            @Override
                            public int compare(SupplierItem lhs, SupplierItem rhs) {
                                double val1 = Double.parseDouble(lhs.getSup_dist());
                                double val2 = Double.parseDouble(rhs.getSup_dist());
                                return (val1 < val2) ? -1 : (val1 > val2) ? 1 : 0;
                            }
                        });


                        SearchAdapter dAdapter = new SearchAdapter(mContext, searchsuppList);
                        recyclerView.setAdapter(dAdapter);
                    }pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    float getDistanceInMeter(double lat, double lng) {

        gps = new GPSTracker(mContext);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{

            gps.showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat, lng, dist);
        return dist[0];
    }

    @Override
    public void onClick(View v) {
        search = search_bar.getText().toString();
        isInternetPresent = cd.isConnectingToInternet();
        isGPSPresent = cd.isGPSon();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.search_button:
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    isGPSPresent = cd.isGPSon();
                    if (isGPSPresent) {
                        if (checkValidation()) {
                            downloadList();
                        }
                    } else {
                        showSettingsAlert();
                    }
                    break;
                case R.id.img1:
                    Intent home = new Intent(this, selection_hotel.class);
                    startActivity(home);
                    break;

                case R.id.img2:
                    Intent category = new Intent(this, CategoryList.class);
                    startActivity(category);
                    break;

                case R.id.img3:
                    Intent searc = new Intent(this, Search.class);
                    startActivity(searc);
                    break;

                case R.id.mapview:
                    Intent mapv = new Intent(this, MapView.class);
                    mapv.putExtra("mapview", (Serializable) searchsuppList);
                    startActivity(mapv);
                    break;

                case R.id.filter:
                    isGPSPresent = cd.isGPSon();
                    if (isGPSPresent) {
                        Intent i = new Intent(this, FilterSearchResult.class);
                        i.putExtra("search", search);
                        startActivityForResult(i, 2);
                    } else {
                        showSettingsAlert();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                filteredSearchlist = (ArrayList<SupplierItem>) data.getSerializableExtra("filterlist");
                sortbyDistance(filteredSearchlist);
                searchsuppList = filteredSearchlist;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Distance")){
            sortbyDistance(searchsuppList);

        }else if(item.equals("Rating")){
            sortbyRating(searchsuppList);

        }
    }

    private void sortbyRating(List<SupplierItem> searchsuppList) {
        Collections.sort(searchsuppList, new Comparator<SupplierItem>() {
            @Override
            public int compare(SupplierItem lhs, SupplierItem rhs) {
                float val1 = Float.parseFloat(lhs.getSup_rate());
                float val2 = Float.parseFloat(rhs.getSup_rate());
                return (val1 > val2) ? -1 : (val1 < val2) ? 1 : 0;
            }
        });

        SearchAdapter dAdapter = new SearchAdapter(mContext, searchsuppList);
        recyclerView.setAdapter(dAdapter);
    }

    private void sortbyDistance(List<SupplierItem> searchsuppList) {
        Collections.sort(searchsuppList, new Comparator<SupplierItem>() {
            @Override
            public int compare(SupplierItem lhs, SupplierItem rhs) {
                double val1 = Double.parseDouble(lhs.getSup_dist());
                double val2 = Double.parseDouble(rhs.getSup_dist());
                return (val1 < val2) ? -1 : (val1 > val2) ? 1 : 0;
            }
        });

        SearchAdapter dAdapter = new SearchAdapter(mContext, searchsuppList);
        recyclerView.setAdapter(dAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class FilterSupplierList extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;
    private static String TAG = FilterSupplierList.class.getSimpleName();
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    SeekBar distance, rating;
    HashMap<String,String> map;
    private List<SupplierItem> filternull, filterdist, filterrat, filterboth;
    Button apply;
    ProgressDialog pd;
    RelativeLayout lay_distance_bar, lay_rating_bar;
    GPSTracker gps;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    int min_distance, max_distance;
    Double min_rating, max_rating;
    RangeSeekBar seekBarInteger, seekBarDouble;
    TextView minTextInt, maxtextInt, minTextDouble, maxTextDouble;
    double latitude, longitude, sup_lat, sup_long;
    String cat_id, cat_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        Intent i = getIntent();
        cat_id = i.getStringExtra("cat_id");
        cat_label = i.getStringExtra("cat_label");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            defineview();
        }
    }

    private void defineview() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Filter");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        seekBarInteger = (RangeSeekBar) findViewById(R.id.seekbar);
        minTextInt = (TextView) findViewById(R.id.seekValuemin);
        maxtextInt = (TextView) findViewById(R.id.seekValuemax);
        seekBarDouble = (RangeSeekBar) findViewById(R.id.seekbarDouble);
        minTextDouble = (TextView) findViewById(R.id.seekValueminDouble);
        maxTextDouble = (TextView) findViewById(R.id.seekValuemaxDouble);
        lay_distance_bar = (RelativeLayout) findViewById(R.id.integer);
        lay_rating_bar = (RelativeLayout) findViewById(R.id.rating);
        apply = (Button)findViewById(R.id.apply);

        if(cat_label.equals("places") ){
            lay_rating_bar.setVisibility(View.GONE);
        } else if (cat_label.equals("trvl")){
            lay_distance_bar.setVisibility(View.GONE);
        }

        seekBarInteger.setRangeValues(0, 20);
        seekBarInteger.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {


            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                min_distance = minValue;
                max_distance = maxValue;
                minTextInt.setText("Min " + minValue + " Km");
                maxtextInt.setText("Max " + maxValue + " Km");

            }

        });

        seekBarDouble.setRangeValues(0.0, 5.0);
        seekBarDouble.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Double>() {


            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue, Double maxValue) {
                Log.e("value", minValue + "  " + maxValue);
                min_rating = minValue;
                max_rating = maxValue;
                minTextDouble.setText("Min " + minValue);
                maxTextDouble.setText("Max " + maxValue);

            }

        });

        apply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        isGPSPresent = cd.isGPSon();
        if (isInternetPresent) {
            if(isGPSPresent) {
                Log.d("test",""+min_distance+" "+max_distance+" "+min_rating+" "+max_rating);
                if((min_distance == 0 && max_distance==0) && (min_rating==null && max_rating==null)){
                    Log.d("filtertest","No filter");
                    onBackPressed();

                }else if((min_distance == 0 && max_distance==0)){
                    Log.d("filtertest","rating filter");
                    filter_rat(min_rating, max_rating);


                }else if((min_rating==null && max_rating==null)){
                    Log.d("filtertest","distance filter");
                    filter_dist(min_distance, max_distance);


                }else {
                    Log.d("filtertest","both filter");
                    filter_both(min_distance, max_distance, min_rating, max_rating);
                }


            }else{
                showSettingsAlert();
            }
        }
    }

    private float getDistanceInMeter(double sup_lat, double sup_long) {
        gps = new GPSTracker(this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{

            gps.showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, sup_lat, sup_long, dist);
        return dist[0];
    }

    private void filter_both(final int min_distance, final int max_distance, final Double min_rating,final Double max_rating) {
        map=new HashMap<>();
        filterboth = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_subcategory.php?catId=" + cat_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&propertyid=" + preferencesClass.getHid() + "&cityid=" + preferencesClass.getStateName(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d("FilterJoin", response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                    } else if (response.getString("status").equals("-1")) {
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(FilterSupplierList.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        if (response.getString("status").equals("1")) {
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
                                distance = (distance / 1000);
                                String distString = String.format("%.1f", distance);
                                supplierItem.setSup_dist(distString);
                                supplierItem.setSup_long(jsonObject.getString("longitude"));
                                supplierItem.setSup_img(jsonObject.getString("image"));
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
                                supplierItem.setSup_img2(jsonObject.getString("image2"));

                                if ((Double.parseDouble(supplierItem.getSup_rate()) >= min_rating) && (max_rating >= Double.parseDouble(supplierItem.getSup_rate()))) {
                                    if (((Double.parseDouble(supplierItem.getSup_dist()) >= min_distance) && (max_distance >= Double.parseDouble(supplierItem.getSup_dist()))) || (Double.parseDouble(supplierItem.getSup_dist()) >= 5000)) {
                                        filterboth.add(supplierItem);
                                    }
                                }
                            }
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("filterlist", (Serializable) filterboth);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                    pd.dismiss();
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

    private void filter_dist(final int min_distance, final int max_distance) {
        map=new HashMap<>();
        filterdist = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_subcategory.php?catId=" + cat_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName()
                + "&propertyid=" + preferencesClass.getHid(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                    } else if (response.getString("status").equals("-1")) {
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(FilterSupplierList.this, Login.class);
                        startActivity(i);
                        finish();
                    } else if (response.getString("status").equals("1")) {
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
                            supplierItem.setSup_long(jsonObject.getString("longitude"));
                            supplierItem.setSup_img(jsonObject.getString("image"));
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
                            supplierItem.setSup_img2(jsonObject.getString("image2"));

                            if(((Double.parseDouble(supplierItem.getSup_dist()) >= min_distance) && (max_distance >= Double.parseDouble(supplierItem.getSup_dist()))) || (Double.parseDouble(supplierItem.getSup_dist()) >= 5000)) {
                                filterdist.add(supplierItem);
                            }
                        }
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("filterlist", (Serializable) filterdist);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
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

        pd.dismiss();
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void filter_rat(final Double min_rating,final Double max_rating) {
        map=new HashMap<>();
        filterrat = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_subcategory.php?catId=" + cat_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName()
                + "&propertyid=" + preferencesClass.getHid(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                    } else if (response.getString("status").equals("-1")) {
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(FilterSupplierList.this, Login.class);
                        startActivity(i);
                        finish();
                    } else if (response.getString("status").equals("1")) {
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
                            supplierItem.setSup_long(jsonObject.getString("longitude"));
                            supplierItem.setSup_img(jsonObject.getString("image"));
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
                            supplierItem.setSup_img2(jsonObject.getString("image2"));

                            if((Double.parseDouble(supplierItem.getSup_rate()) >= min_rating) && (max_rating >= Double.parseDouble(supplierItem.getSup_rate()))) {
                                filterrat.add(supplierItem);
                                Log.d("testing", supplierItem.getSup_dist() + ", " + supplierItem.getSup_rate());
                            }
                        }Intent returnIntent = new Intent();
                        returnIntent.putExtra("filterlist", (Serializable) filterrat);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
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
        pd.dismiss();
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
}

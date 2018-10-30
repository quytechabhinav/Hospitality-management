package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cityscann.com.city_scann.adapter.SupplierListAdapter;
import cityscann.com.city_scann.listener.RecyclerItemClickListener;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

/**
 * Created by shuser on 03-12-2016.
 */
public class SupplierListTest extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static String TAG = SupplierListTest.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    String cat_id, cat_name, cat_label,googlekey,google_type;
    int min_dist, max_dist;
    double min_rat, max_rat;
    LinearLayout nodata;
    RelativeLayout data, lay_sort;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    private List<SupplierItem> supplierItemList, supplierItemList1, filteredlist;
    ProgressDialog pd;
    boolean isGPSPresent = false;
    int position;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    TextView mapView, filter;
    ImageButton img1, img2, img3,img4;
    GPSTracker gps;
    double latitude, longitude, sup_lat, sup_long;
    Spinner spinnersort;
    private String[] sortbyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplierlist);
        mContext = getApplicationContext();

        final ArrayList<HashMap<String, String>> leadslist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");
        cat_id = leadslist.get(position).get("cat_id");
        cat_label = leadslist.get(position).get("catlabel");
        cat_name = leadslist.get(position).get("cat_name");
        googlekey = leadslist.get(position).get("google_key");
        google_type = leadslist.get(position).get("google_type");
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();
            definerecyclerview();
        }
    }

    private void defineviewforitem() {
        nodata=(LinearLayout)findViewById(R.id.no_data);
        data=(RelativeLayout) findViewById(R.id.data);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText(cat_name);
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        spinnersort = (Spinner) findViewById(R.id.spinner);

        filter = (TextView) findViewById(R.id.filter);
        mapView = (TextView) findViewById(R.id.mapview);
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);*/
        lay_sort = (RelativeLayout) findViewById(R.id.lay_sort);

        if (cat_label.equals("trvl") || cat_label.equals("airl") || cat_label.equals("rail") || cat_label.equals("bser") || cat_label.equals("cpn") || cat_label.equals("payment") || cat_label.equals("cbsr") || cat_label.equals("tinf")){
            lay_sort.setVisibility(View.GONE);
        }

        if (cat_label.equals("places")){
            sortbyList = new String[]{"Distance"};
        } else if (cat_label.equals("trvl")){
            mapView.setVisibility(View.GONE);
            sortbyList = new String[]{"Rating"};
        } else {
            sortbyList = new String[]{"Distance","Rating"};
        }

        filter.setOnClickListener(this);
        mapView.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/

        spinnersort.setOnItemSelectedListener(this);
        ArrayAdapter<String> sortby = new ArrayAdapter<String>(this, R.layout.drawable, sortbyList);
        sortby.setDropDownViewResource(R.layout.drawable);
        spinnersort.setAdapter(sortby);
    }

    private void definerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SupplierListTest.this, SupplierDetail.class);
                intent.putExtra("item", (Serializable) supplierItemList1);
                intent.putExtra("Positions", position);
                startActivity(intent);
            }
        }));
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        downloadList();
        getdataarraylist();
    }

    private void sortbyDistance(List<SupplierItem> supplierItemList) {
        Collections.sort(supplierItemList, new Comparator<SupplierItem>() {
            @Override
            public int compare(SupplierItem lhs, SupplierItem rhs) {
                double val1 = Double.parseDouble(lhs.getSup_dist());
                double val2 = Double.parseDouble(rhs.getSup_dist());
                return (val1 < val2) ? -1 : (val1 > val2) ? 1 : 0;
            }
        });

        SupplierListAdapter dAdapter = new SupplierListAdapter(mContext, supplierItemList);
        recyclerView.setAdapter(dAdapter);
    }

    private void sortbyRating(List<SupplierItem> supplierItemList) {
        Collections.sort(supplierItemList, new Comparator<SupplierItem>() {
            @Override
            public int compare(SupplierItem lhs, SupplierItem rhs) {
                float val1 = Float.parseFloat(lhs.getSup_rate());
                float val2 = Float.parseFloat(rhs.getSup_rate());
                return (val1 > val2) ? -1 : (val1 < val2) ? 1 : 0;
            }
        });

        SupplierListAdapter dAdapter = new SupplierListAdapter(mContext, supplierItemList);
        recyclerView.setAdapter(dAdapter);
    }

    private void getdataarraylist(){
        map=new HashMap<>();
        supplierItemList1 = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");
        pd.dismiss();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.5586434,77.2002744&radius=1000&type=restaurant&keyword=chinese&key=AIzaSyDbZBja2EoZRlwbxPV3bs5Yc6r9Yc7qM4w", new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                        nodata.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = response.getJSONArray("results");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SupplierItem supplierItem = new SupplierItem();

                            supplierItem.setSup_name(jsonObject.getString("name"));
                            supplierItem.setSup_rate("");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                            supplierItem.setSup_lat(String.valueOf(jsonObject2.getDouble("lat")));
                            sup_lat = Double.parseDouble(String.valueOf(jsonObject2.getDouble("lat")));
                            sup_long = Double.parseDouble(String.valueOf(jsonObject2.getDouble("lng")));
                            float distance = getDistanceInMeter(sup_lat, sup_long);
                            distance = (distance/1000);
                            String distString = String.format("%.1f", distance);
                            supplierItem.setSup_dist(distString);
                            supplierItem.setSup_long(String.valueOf(jsonObject2.getDouble("lng")));

                            supplierItem.setAbt_sup("");
                            supplierItem.setSup_link("");
                            supplierItem.setSup_add(jsonObject.getString("vicinity"));
                            supplierItem.setSup_city("");
                            supplierItem.setSup_state("");
                            supplierItem.setSup_country("");
                            supplierItem.setSup_zip("");
                            supplierItem.setSup_phone1("");
                            supplierItem.setSup_phone2("");
                            supplierItem.setCat_label("chin");
                            supplierItem.setFromtime("");
                            supplierItem.setTotime("");

                            supplierItemList1.add(supplierItem);
                        }
                        Log.d("tester", String.valueOf(supplierItemList1));

                        Collections.sort(supplierItemList1, new Comparator<SupplierItem>() {
                            @Override
                            public int compare(SupplierItem lhs, SupplierItem rhs) {
                                double val1 = Double.parseDouble(lhs.getSup_dist());
                                double val2 = Double.parseDouble(rhs.getSup_dist());
                                return (val1 < val2) ? -1 : (val1 > val2) ? 1 : 0;
                            }
                        });

                        SupplierListAdapter dAdapter = new SupplierListAdapter(mContext, supplierItemList1);
                        recyclerView.setAdapter(dAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tester", e.getMessage());
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

    private void downloadList() {

        map=new HashMap<>();
        supplierItemList = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(true);
        pd.show();
        pd.dismiss();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=28.5586434,77.2002744&radius=1000&type=restaurant&keyword=chinese&key=AIzaSyDbZBja2EoZRlwbxPV3bs5Yc6r9Yc7qM4w", new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                try {

                        nodata.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = response.getJSONArray("results");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            SupplierItem supplierItem = new SupplierItem();

                            supplierItem.setSup_name(jsonObject.getString("name"));
                            supplierItem.setSup_rate("");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                            supplierItem.setSup_lat(String.valueOf(jsonObject2.getDouble("lat")));
                            sup_lat = Double.parseDouble(String.valueOf(jsonObject2.getDouble("lat")));
                            sup_long = Double.parseDouble(String.valueOf(jsonObject2.getDouble("lng")));
                            float distance = getDistanceInMeter(sup_lat, sup_long);
                            distance = (distance/1000);
                            String distString = String.format("%.1f", distance);
                            supplierItem.setSup_dist(distString);
                            supplierItem.setSup_long(String.valueOf(jsonObject2.getDouble("lng")));

                            supplierItem.setAbt_sup("");
                            supplierItem.setSup_link("");
                            supplierItem.setSup_add(jsonObject.getString("vicinity"));
                            supplierItem.setSup_city("");
                            supplierItem.setSup_state("");
                            supplierItem.setSup_country("");
                            supplierItem.setSup_zip("");
                            supplierItem.setSup_phone1("");
                            supplierItem.setSup_phone2("");
                            supplierItem.setCat_label("chin");

                            supplierItem.setFromtime("");
                            supplierItem.setTotime("");

                            supplierItemList.add(supplierItem);
                        }

                    Log.d("tester", String.valueOf(supplierItemList));


                        Collections.sort(supplierItemList, new Comparator<SupplierItem>() {
                            @Override
                            public int compare(SupplierItem lhs, SupplierItem rhs) {
                                double val1 = Double.parseDouble(lhs.getSup_dist());
                                double val2 = Double.parseDouble(rhs.getSup_dist());
                                return (val1 < val2) ? -1 : (val1 > val2) ? 1 : 0;
                            }
                        });


                        SupplierListAdapter dAdapter = new SupplierListAdapter(mContext, supplierItemList);
                        recyclerView.setAdapter(dAdapter);

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

            showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat, lng, dist);
        return dist[0];
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        isGPSPresent = cd.isGPSon();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.filter:
                    isGPSPresent = cd.isGPSon();
                    if (isGPSPresent) {
                        Intent i = new Intent(this, FilterSupplierList.class);
                        i.putExtra("cat_id",cat_id);
                        i.putExtra("cat_label", cat_label);
                        startActivityForResult(i, 1);
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

                /*case R.id.img3:
                    Intent search = new Intent(this, Search.class);
                    startActivity(search);
                    break;
                case R.id.img4:
                    final Dialog dialog = new Dialog(SupplierListTest.this);
                    dialog.setContentView(R.layout.logout);
                    dialog.show();

                    break;*/

                case R.id.mapview:
                    isGPSPresent = cd.isGPSon();
                    if (isGPSPresent) {
                        Intent mapv = new Intent(this, MapView.class);
                        mapv.putExtra("mapview", (Serializable) supplierItemList);
                        startActivity(mapv);
                    } else {
                        showSettingsAlert();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                filteredlist = (ArrayList<SupplierItem>) data.getSerializableExtra("filterlist");
                sortbyDistance(filteredlist);
                supplierItemList1 = filteredlist;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Distance")){
            sortbyDistance(supplierItemList1);

        }else if(item.equals("Rating")){
            sortbyRating(supplierItemList1);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

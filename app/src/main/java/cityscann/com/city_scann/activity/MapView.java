package cityscann.com.city_scann.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

import static cityscann.com.city_scann.R.id.map;


public class MapView extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 500;
    private GoogleMap mMap;
    private static String TAG = MapView.class.getSimpleName();
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<SupplierItem> latlngs, maparray;
    HashMap<String,String> hmap;
    ProgressDialog pd;
    LatLng myposition;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isInternetPresent = false;
    double latitude, longitude;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    private ConnectionDetector cd;
    Location location;
    ImageButton img1, img2, img3;
    String cat_id, cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        maparray = (ArrayList<SupplierItem>) intent.getSerializableExtra("mapview");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            defineviewforitem();
        }

        getLocation();
    }

    private void getmarker() {
        for (int i=0; i< maparray.size(); i++){
            final SupplierItem supplierItem = maparray.get(i);
            if (Double.parseDouble(supplierItem.getSup_lat())!=0){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(supplierItem.getSup_lat()),Double.parseDouble(supplierItem.getSup_long())))
                        .title(supplierItem.getSup_name()));
            }
        }
    }

    private void defineviewforitem() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Map");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
      /*  img3 = (ImageButton) findViewById(R.id.img3);
*/
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);*/
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                showSettingsAlert();
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return null;
                            }
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
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

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return  longitude;
    }

    private void getsupplierlist() {
        hmap = new HashMap<>();
        pd = new ProgressDialog(this);
        latlngs = new ArrayList<>();
        pd.setMessage("Loading..");
        pd.setCancelable(true);
        pd.show();
        pd.dismiss();
        hmap.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_subcategory.php?catId=" + cat_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName()
                + "&propertyid=" + preferencesClass.getHid(), new JSONObject(hmap), new Response.Listener<JSONObject>() {
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
                        Intent i = new Intent(MapView.this, Login.class);
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

                            if (Double.parseDouble(supplierItem.getSup_lat())!=0){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(supplierItem.getSup_lat()),Double.parseDouble(supplierItem.getSup_long())))
                                        .title(supplierItem.getSup_name()));
                                latlngs.add(supplierItem);
                            }
                        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        myposition = new LatLng(getLatitude(), getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myposition,12));
        mMap.setMyLocationEnabled(true);
        getmarker();
    }

    @Override
    public void onLocationChanged(Location location) {
        myposition = new LatLng(getLatitude(), getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()) {
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
                    break;*/
            }
        }
    }

}
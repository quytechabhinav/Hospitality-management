package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.RestaurantListAdapter;
import cityscann.com.city_scann.listener.RecyclerItemClickListener;
import cityscann.com.city_scann.model.HotelItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class RestaurantList extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = RestaurantList.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    private List<HotelItem> hotelrestaurantList;
    ProgressDialog pd;
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    private ConnectionDetector cd;
    ImageButton img1, img2, img3;
    int position;
    String facility_titel, facility_id;
    ImageView search;
    PreferencesClass preferencesClass=new PreferencesClass(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_recyclerview_only);

        final List<HotelItem> hotelItemList = (ArrayList<HotelItem>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");
        facility_id = getIntent().getExtras().getString("facilityid");
        facility_titel = getIntent().getExtras().getString("facilitytital");

        Log.d("facility",facility_titel);
        mContext = getApplicationContext();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            defineviewforitem();
            definerecyclerview();
        }
    }

    private void definerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(RestaurantList.this, HotelDetail.class);
                intent.putExtra("item", (Serializable) hotelrestaurantList);
                intent.putExtra("Positions", position);
                startActivity(intent);
            }
        }));
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        downloadList();
    }

    private void downloadList() {
        map=new HashMap<>();
        hotelrestaurantList=new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        Log.d("facility_id",facility_id);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_hotelfacilitydetails.php?fid=" + facility_id /*facility_id*/, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d("TAGHOTEl", response.toString());
                try {
                    if (response.getString("status").equals("0")) {

                    }  else if (response.getString("status").equals("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HotelItem hotelItem = new HotelItem();
                            hotelItem.setHotelItemName(jsonObject.getString("name"));
                            hotelItem.setHotelItemImage(jsonObject.getString("image1"));
                            hotelItem.setHotelItemImage2(jsonObject.getString("image2"));
                            hotelItem.setHotelItemDescription(jsonObject.getString("description"));
                            hotelItem.setHotelItemphone2(jsonObject.getString("phone2"));

                            hotelrestaurantList.add(hotelItem);
                        }
                        RestaurantListAdapter dAdapter = new RestaurantListAdapter(mContext, hotelrestaurantList);
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

    private void defineviewforitem() {
        search=(ImageView)findViewById(R.id.logout);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesClass.setUserToken(null);
                preferencesClass.setusername(null);
                preferencesClass.setphone(null);
                preferencesClass.setuser_email(null);
                Intent i = new Intent(RestaurantList.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        toolbartitle=(TextView)findViewById(R.id.toolbar_title);

        toolbartitle.setText(facility_titel);

        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                break;

            case R.id.img2:
                Intent category = new Intent(this, CategoryList.class);
                startActivity(category);
                break;

           /* case R.id.img3:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                break;*/
        }
    }
}

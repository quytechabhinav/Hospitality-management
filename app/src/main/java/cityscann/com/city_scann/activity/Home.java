package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.HotelDetailListAdapter;
import cityscann.com.city_scann.listener.RecyclerItemClickListener;
import cityscann.com.city_scann.model.HotelItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private ImageButton img1, img2, img3, img4;
    private static String TAG = Home.class.getSimpleName();
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    private List<HotelItem> hotelDetailList;
    private ConnectionDetector cd;
    ProgressDialog pd;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    ImageView banner;
    String hotel_name, hotel_banner, hotel_desc;
    AppBarLayout appBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if( preferencesClass.getUserToken() != null ){

        }else {
            //Log.d("Session",""+preferencesClass.getUserToken());
            Intent i = new Intent(Home.this,Licence_agreement.class);
            startActivity(i);
        }

        if(preferencesClass.getHid() != null && preferencesClass.getUserToken() != null) {
            mContext = getApplicationContext();
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                definerecyclerview();
            }
            defineviewforitem();
        }else {
            Intent i = new Intent(Home.this, PropertyCode.class);
            startActivity(i);
        }
    }

    private void definerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HotelItem hotelItem = new HotelItem();
                hotelItem = hotelDetailList.get(position);
                if(hotelItem.getHotelItemSubCat().equals("0")) {
                    Intent intent = new Intent(Home.this, HotelDetail.class);
                    intent.putExtra("item", (Serializable) hotelDetailList);
                    intent.putExtra("Positions", position);
                    startActivity(intent);
                }else if(hotelItem.getHotelItemSubCat().equals("3")) {
                    Intent intent = new Intent(Home.this,HotelDetail.class);
                    //Intent intent = new Intent(Home.this,HotelDetail.class);
                    intent.putExtra("facilityid",hotelItem.getHotelItemid());
                    intent.putExtra("facilitytital",hotelItem.getHotelItemName());
                    startActivity(intent);
                }
                else {
                    Intent i = new Intent(Home.this, RestaurantList.class);
                    i.putExtra("item", (Serializable) hotelDetailList);
                    i.putExtra("facilityid",hotelItem.getHotelItemid());
                    i.putExtra("facilitytital",hotelItem.getHotelItemName());
                    i.putExtra("positions", position);
                    startActivity(i);
                }
            }
        }));
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        hotelDetail();
    }

    private void hotelDetail() {
        map=new HashMap<>();
        hotelDetailList = new ArrayList<>();
        pd = new ProgressDialog(this);
        pd.setMessage(" ");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_hoteldetails.php?hid="+ preferencesClass.getHid()+"&token="+preferencesClass.getUserToken(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), "Please Login !!!", Toast.LENGTH_SHORT).show();
                    } else if (response.getString("status").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        //Log.d("AbhinavData",response.toString());
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HotelItem hotelItem = new HotelItem();
                            hotelItem.setHotelItemid(jsonObject.getString("id"));
                            hotelItem.setHotelItemSubCat(jsonObject.getString("sub_cat"));
                            hotelItem.setHotelItemName(jsonObject.getString("name"));
                            hotelItem.setHotelItemImage(jsonObject.getString("image1"));
                            hotelItem.setHotelItemImage2(jsonObject.getString("image2"));
                            hotelItem.setHotelItemDescription(jsonObject.getString("description"));
                            hotelItem.setHotelItemphone2(jsonObject.getString("phone2"));
                            hotelDetailList.add(hotelItem);
                        }

                        HotelDetailListAdapter dAdapter = new HotelDetailListAdapter(mContext, hotelDetailList);
                        recyclerView.setAdapter(dAdapter);
                    }pd.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "please contact our Reception for help !!!", Toast.LENGTH_SHORT).show();
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
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);*/
        banner = (ImageView) findViewById(R.id.image_id);
        banner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,hotel_details.class);
                startActivity(intent);
            }
        });


        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/
        img1.setOnClickListener(this);
        downloadHotelTitel();
    }

    private void downloadHotelTitel() {
        map=new HashMap<>();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_hotel.php?hid=" + preferencesClass.getHid()+"&token="+preferencesClass.getUserToken(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), "Please Login !!!", Toast.LENGTH_SHORT).show();
                    } else if (response.getString("status").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hotel_name = jsonObject.getString("hname");
                            hotel_banner = jsonObject.getString("banner");
                            hotel_desc = jsonObject.getString("description");

                            toolbar = (Toolbar) findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle(hotel_name);

                            Picasso.with(Home.this).load(getString(R.string.ip_address_images)+ hotel_banner).placeholder(R.drawable.banner).error(R.drawable.banner).into(banner);
                        }

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

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()){
                case R.id.img2:
                    Intent category = new Intent(Home.this, CategoryList.class);
                    startActivity(category);
                    break;
               /* case R.id.img3:
                    Intent search = new Intent(Home.this, Search.class);
                    startActivity(search);
                    break;
                case R.id.img4:
                    final Dialog dialog = new Dialog(Home.this);
                    dialog.setContentView(R.layout.logout);
                    *//*Intent Logout = new Intent(Home.this, Logout.class);
                    startActivity(Logout);*//*
                    dialog.show();
                    break;*/
                case R.id.img1:
                    Intent intent = new Intent(Home.this, selection_hotel.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        System.exit(0);
    }
}

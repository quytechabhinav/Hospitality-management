package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.GoogleReviewAdopter;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class SupplierDetail extends AppCompatActivity implements View.OnClickListener {

    PreferencesClass preferencesClass=new PreferencesClass(this);
    private static String TAG = SupplierDetail.class.getSimpleName();
    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle, supName, supRate, supDist, abtSup, supPhone1, supAdd, revNo, hour, costfortwo, cuisines, coupancode,
            coupon_brand, day, city, state, zip, country, end_rev, rev_head, mapview, website,Name,revrating,detail;
    Button link, wrRev, vuRev;
    ImageView reviewimage;
    View rev_underline;
    ImageButton img1, img2, img3;
    LinearLayout addLay, contactLay, lay_dist, wrRev_lay, revtxt_lay, timing_lay, lay_costfortwo, lay_cuisines, lay_rat,
            lay_day, lay_city, lay_state, lay_country, about_sup, lay_website;
    RelativeLayout link_lay;
    int position;
    ProgressDialog pd;
    String sup_id, sup_name, sup_rate, sup_dist, abt_sup, sup_link, sup_img, sup_lat, sup_long, sup_add, brand,
            sup_city, sup_state, sup_country, sup_zip, sup_phone1, sup_phone2, cat_label, timing, costfor2, cuisi, coupon_code,
            opening_days, fromtime, totime, from_time, to_time, str1,phone,web;
    double lat1, lng1, latitude, longitude;
    GPSTracker gps;
    HashMap<String,String> map;
    List<SupplierItem> supplierItemList;
    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;
    private ArrayList<String> author_name;
    private ArrayList<String> text;
    private ArrayList<String> image;
    String[] photo;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_detail);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();
            getDetail();
        }
        author_name = new ArrayList<String>();
        text= new ArrayList<String>();
        image= new ArrayList<String>();
    }

    private void detaildatalist() {
        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        map.put("", "");

        Log.d("suppltt", sup_id + " " + preferencesClass.getUserToken() + " " + preferencesClass.getuser_email() + " " + preferencesClass.getStateName());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_supplier.php?email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName() + "&sid=" + sup_id + "&propertyid=" + preferencesClass.getHid(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                    } else if (response.getString("status").equals("-1")){
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(SupplierDetail.this, Login.class);
                        startActivity(i);
                        finish();
                    } else if (response.getString("status").equals("1")) {

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
                Intent i = new Intent(SupplierDetail.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });


        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.main1);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Detail");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        supplierItemList = (ArrayList<SupplierItem>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");

        sup_id = supplierItemList.get(position).getSup_id();
        cat_label = supplierItemList.get(position).getCat_label();
        sup_name = supplierItemList.get(position).getSup_name();
        sup_rate = supplierItemList.get(position).getSup_rate();
        sup_dist = supplierItemList.get(position).getSup_dist();
        coupon_code = supplierItemList.get(position).getCouponcode();
        sup_lat = supplierItemList.get(position).getSup_lat();
        sup_long = supplierItemList.get(position).getSup_long();
        abt_sup = supplierItemList.get(position).getAbt_sup();
        cuisi = supplierItemList.get(position).getCuisines();
        sup_link = supplierItemList.get(position).getSup_link();
        brand = supplierItemList.get(position).getBrand();
        sup_img = supplierItemList.get(position).getSup_img2();
        costfor2 = supplierItemList.get(position).getCostfortwo();
        timing = supplierItemList.get(position).getHours();
        sup_add = supplierItemList.get(position).getSup_add();
        sup_city = supplierItemList.get(position).getSup_city();
        sup_state = supplierItemList.get(position).getSup_state();
        sup_country = supplierItemList.get(position).getSup_country();
        sup_zip = supplierItemList.get(position).getSup_zip();
        sup_phone1 = supplierItemList.get(position).getSup_phone1();
        sup_phone2 = supplierItemList.get(position).getSup_phone2();
        opening_days = supplierItemList.get(position).getOpeningdays();
        from_time = supplierItemList.get(position).getFromtime();
        to_time = supplierItemList.get(position).getTotime();
        detaildatalist();
        nameOfCity();
        sliderLayout =  (SliderLayout) findViewById(R.id.sup_img);
        rev_underline = (View) findViewById(R.id.rev_underline);

        supName =  (TextView) findViewById(R.id.sup_name);
        supRate =  (TextView) findViewById(R.id.sup_rate);
        supDist =  (TextView) findViewById(R.id.sup_dist);
        hour =     (TextView) findViewById(R.id.hour);
        abtSup =   (TextView) findViewById(R.id.abt_sup);
        supAdd =   (TextView) findViewById(R.id.address1);
        revNo =    (TextView) findViewById(R.id.sup_rev);
        cuisines = (TextView) findViewById(R.id.cuisines);
        supPhone1 =(TextView) findViewById(R.id.contact1);
        costfortwo=(TextView) findViewById(R.id.costfortwo);
        coupancode=(TextView) findViewById(R.id.coupancode);
        coupon_brand=(TextView)findViewById(R.id.coupon_brand);
        day =      (TextView) findViewById(R.id.day);
        rev_head = (TextView) findViewById(R.id.rev_head);
        end_rev =  (TextView) findViewById(R.id.end_rev);
        city =     (TextView) findViewById(R.id.city);
        state =    (TextView) findViewById(R.id.state);
        zip =      (TextView) findViewById(R.id.zip);
        country =  (TextView) findViewById(R.id.country);
        mapview = (TextView) findViewById(R.id.mapview);
        website = (TextView) findViewById(R.id.website);
        lay_cuisines = (LinearLayout) findViewById(R.id.lay_cuisines);
        timing_lay =   (LinearLayout) findViewById(R.id.opentiming_lay);
        contactLay =   (LinearLayout) findViewById(R.id.lay_contact);
        addLay =       (LinearLayout) findViewById(R.id.lay_add);
        lay_dist =     (LinearLayout) findViewById(R.id.sup_distance);
        lay_rat =      (LinearLayout) findViewById(R.id.sup_rating);
        lay_costfortwo=(LinearLayout) findViewById(R.id.lay_costfortwo);
        wrRev_lay =    (LinearLayout) findViewById(R.id.abt_rev_lay);
        revtxt_lay =   (LinearLayout) findViewById(R.id.lay_sup_rev);
        lay_day =      (LinearLayout) findViewById(R.id.lay_opening_day);
        lay_city =     (LinearLayout) findViewById(R.id.lay_city);
        lay_state =    (LinearLayout) findViewById(R.id.lay_state);
        lay_country =  (LinearLayout) findViewById(R.id.lay_country);
        about_sup =    (LinearLayout) findViewById(R.id.about_sup);
        lay_website =  (LinearLayout) findViewById(R.id.lay_website);
        link_lay =   (RelativeLayout) findViewById(R.id.sup_link_lay);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SupplierDetail.this);
        recyclerView.setLayoutManager(layoutManager);
        link =  (Button) findViewById(R.id.link);
        wrRev = (Button) findViewById(R.id.write_rev);
        vuRev = (Button) findViewById(R.id.view_rev);

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
      /*  img3 = (ImageButton) findViewById(R.id.img3);*/

        if (!((from_time.equals(null) || from_time.equals("") || from_time.equals("null") ))) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
                final Date ft = sdf.parse(from_time);
                String checktime = new SimpleDateFormat("K:mm a").format(ft);
                String CurrentString = checktime;
                String[] separated = CurrentString.split(":");
                String firsttime = separated[0];
                String secondtime = separated[1];
                if (firsttime.equals("0")){
                    firsttime = String.valueOf(12);
                }
                fromtime = firsttime+":"+secondtime;
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d("imagestest", cat_label);


        day.setText(opening_days);

     //   supImg.setImageResource(R.drawable.untitled);
        supName.setText(sup_name);
        //supRate.setText(sup_rate);
        if(Double.parseDouble(sup_dist)>=5000){
            lay_dist.setVisibility(View.GONE);
        }else {
            supDist.setText(sup_dist);
        }

        if (abt_sup.equals("") || abt_sup.equals(null) || abt_sup.equals(" ") || abt_sup.equals("null")) {
            about_sup.setVisibility(View.GONE);
        }
        abtSup.setText(abt_sup);

        Log.d("testing_time", fromtime+ " " + to_time);
        if(!(from_time.equals("00:00:00") && to_time.equals("00:00:00"))) {
            if (!((from_time.equals(null) || from_time.equals("") || from_time.equals("null")))) {
                if (to_time.equals(null) || to_time.equals("") || to_time.equals("null")) {
                    hour.setText(fromtime);
                } else {
                    hour.setText(fromtime + "  To  " + totime);
                }
            } else {
                hour.setText("");
            }
        }else{
            timing_lay.setVisibility(View.GONE);
        }

        if (!((sup_phone1.equals("null") || sup_phone1.equals("") || sup_phone1.equals(" ")))) {
            contactLay.setVisibility(View.VISIBLE);
            if(sup_phone2.equals("") || sup_phone2.equals("null") || sup_phone2.equals(" ")){
                supPhone1.setText(sup_phone1);
            } else{
                supPhone1.setText(sup_phone1+ "\n\n" + sup_phone2);
            }
        }

        if (!((sup_link.equals("null") || sup_link.equals("") || sup_link.equals(" ") || sup_link.equals(null)))){
            lay_website.setVisibility(View.VISIBLE);
        }

        if (!(sup_link.equals("") || sup_link.equals("null") || sup_link.equals(null) || sup_link.equals(" "))) {
            String str = sup_link;
            String lastchar = str.substring(str.length() -1, str.length());
            if (lastchar.equals("/")) {
                str1 = str.substring(0, str.length() - 1);
            }else{
                str1 = sup_link;
            }
            website.setText(str1);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(sup_link.toString().trim());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        supAdd.setText(sup_add);
        if (sup_city.equals("null") || sup_city.equals("") || sup_city.equals(" ") || sup_city.equals("0")){
            lay_city.setVisibility(View.GONE);
        }
        if (sup_country.equals("null") || sup_country.equals("") || sup_country.equals(" ")){
            lay_country.setVisibility(View.GONE);
        }
        if (sup_state.equals("null") || sup_state.equals("") || sup_state.equals(" ")){
            lay_state.setVisibility(View.GONE);
        }
        if ((sup_zip.equals("null") || sup_zip.equals("") || sup_zip.equals("0")) && (!(sup_state.equals("null") || sup_state.equals("") || sup_state.equals(" ")))){
            zip.setVisibility(View.GONE);
        }

        country.setText(sup_country);
        state.setText(sup_state);
        zip.setText(sup_zip);

       // Picasso.with(this).load((getResources().getString(R.string.ip_address_images) + sup_img).toString().trim()).into(supImg);
        noOfreview();

        link.setOnClickListener(this);
        wrRev.setOnClickListener(this);
        vuRev.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);*/
        mapview.setOnClickListener(this);
    }

    private void nameOfCity() {
        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_cityname.php?cid=" + sup_city, new JSONObject(map), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        revNo.setText("0");
                    } else if (response.getString("status").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String city_name = jsonObject.getString("cname");
                            city.setText(city_name);
                            lay_city.setVisibility(View.GONE);
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
        pd.dismiss();
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void noOfreview() {
        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_review.php?sid=" + sup_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName(), new JSONObject(map), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        revNo.setText("0");

                    } else if (response.getString("status").equals("-1")){
                        revNo.setText("0");
                    }else if (response.getString("status").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        revNo.setText(""+end);
                        revNo.setTextColor(getResources().getColor(R.color.blue_500));
                        end_rev.setTextColor(getResources().getColor(R.color.blue_500));
                        revtxt_lay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent2 = new Intent(SupplierDetail.this, Review.class);
                                intent2.putExtra("sup_id",sup_id);
                                startActivity(intent2);
                            }
                        });
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

    private float getDistanceInMeter(double lat1, double lng1) {
        gps = new GPSTracker(this);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat1, lng1, dist);
        return dist[0];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.link:
                if (!(sup_phone1.equals(" ") || sup_phone1.equals("") || sup_phone1.equals(null) || sup_phone1.equals("null"))){
                    String phone_no = sup_phone1.replaceAll("-", "");
                    phone_no = phone_no.replaceAll(" ", "");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:" + phone_no));
                    startActivity(callIntent);

                } else if (!(sup_link.equals(" ") || sup_link.equals("") || sup_link.equals(null) || sup_link.equals("null"))){
                    Uri uri = Uri.parse(sup_link.toString().trim());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                break;

            case R.id.write_rev:
                Intent intent1 = new Intent(this, SubmitReview.class);
                intent1.putExtra("sup_id",sup_id);
                startActivity(intent1);
                break;
            case R.id.view_rev:
                Intent intent2 = new Intent(this, Review.class);
                intent2.putExtra("sup_id",sup_id);
                startActivity(intent2);
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
                break;*/
            case R.id.mapview:
                Intent intent3 = new Intent(this, MapView_Detail.class);
                intent3.putExtra("dest_lat", sup_lat);
                intent3.putExtra("dest_lng", sup_long);
                //intent3.putExtra("maparray", (Serializable) supplierItemList);
                startActivity(intent3);
                break;
        }
    }


    private void getDetail() {
        map=new HashMap<>();
        map.put("", "");

        Log.d("supplt11t", "https://maps.googleapis.com/maps/api/place/details/json?placeid="+sup_rate+"&key=AIzaSyBhEw4XHH9hP-Uws4VYzjZM7qZM-hNM0oo");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, "https://maps.googleapis.com/maps/api/place/details/json?placeid="+sup_rate+"&key=AIzaSyBhEw4XHH9hP-Uws4VYzjZM7qZM-hNM0oo", new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("result");
                      web = jsonObject.getString("website");
                    website.setText("Click Now");
                    website.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(web.toString().trim());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                    String  ret = jsonObject.getString("rating");
                    supRate.setText(ret);
                    phone = jsonObject.getString("formatted_phone_number");

                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            startActivity(callIntent);
                        }
                    });
                        //  link.setText(phone);
                    Log.d("1---",ret);
                    Hash_file_maps = new HashMap<String, String>();
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
                    photo = new String[jsonObject.length()];
                    final int end = jsonArray.length();
                    for (int i = 0; i < end; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //SupplierItem supplierItem = new SupplierItem();
                        photo[i] = jsonObject1.getString("photo_reference");
                        if(jsonObject1.getString("photo_reference").equals("")||jsonObject1.getString("photo_reference").equals(null)||jsonObject1.getString("photo_reference").equals(" "))
                        {
                            sliderLayout.setVisibility(View.GONE);
                        }
                        Log.d("im--",photo[i]);
                        Hash_file_maps.put(i+"" , "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyBhEw4XHH9hP-Uws4VYzjZM7qZM-hNM0oo&photoreference="+ photo[i]);
                        //Picasso.with(SupplierDetail.this).load(("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyBhEw4XHH9hP-Uws4VYzjZM7qZM-hNM0oo&photoreference="+ photo[0]).toString().trim()).into(supImg);

                    }
                    for(String name : Hash_file_maps.keySet()){
                        TextSliderView textSliderView = new TextSliderView(SupplierDetail.this);
                        textSliderView
                                .description(name)
                                .image(Hash_file_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        //.setOnSliderClickListener(this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);
                        sliderLayout.addSlider(textSliderView);
                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(3000);

                    //Review Code From Google
                    JSONArray jsonArray1 = jsonObject.getJSONArray("reviews");
                  //  time = new String[jsonObject.length()];
                   // re_rating = new String[jsonObject.length()];
                    final int end1 = jsonArray1.length();
                    for (int j = 0; j < end1; j++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                        author_name.add(jsonObject1.getString("author_name"));
                        //Name.setText(author_name[j]);
                        text.add(jsonObject1.getString("text"));
                        //detail.setText(text[j]);
                        //time[j] = jsonObject1.getString("relative_time_description");
                        image.add(jsonObject1.getString("profile_photo_url"));
                    }
                    GoogleReviewAdopter adapter = new GoogleReviewAdopter(SupplierDetail.this,author_name,text,image);
                    recyclerView.setAdapter(adapter);

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

}

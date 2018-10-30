package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.ReviewAdapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Review extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = Review.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    String sup_id;
    ArrayList<HashMap<String, String>> itemLists;
    ProgressDialog pd;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    LinearLayout nodata;
    RelativeLayout data;
    ImageButton img1, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_recyclerview_only);

        LinearLayout nodata = (LinearLayout) findViewById(R.id.no_data);
        RelativeLayout data = (RelativeLayout) findViewById(R.id.data);
        nodata.setVisibility(View.GONE);
        data.setVisibility(View.VISIBLE);
        mContext = getApplicationContext();

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();
            definerecyclerview();
        }
    }

    private void definerecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        downloadList();
    }

    private void downloadList() {
        map=new HashMap<>();
        itemLists=new ArrayList<>();
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
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);

                    } else if (response.getString("status").equals("-1")) {
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(Review.this, Login.class);
                        startActivity(i);
                        finish();
                    } else if (response.getString("status").equals("1")) {
                        nodata.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("sup_id", jsonObject.getString("supplierid"));
                            hashMap.put("rev_head", jsonObject.getString("heading"));
                            hashMap.put("rev_sub", jsonObject.getString("reviewtext"));
                            hashMap.put("rat", jsonObject.getString("rating"));
                            hashMap.put("user_id", jsonObject.getString("useremail"));
                            itemLists.add(hashMap);

                        }
                        ReviewAdapter dAdapter = new ReviewAdapter(mContext, itemLists);
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
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Review");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        nodata=(LinearLayout)findViewById(R.id.no_data);
        data=(RelativeLayout) findViewById(R.id.data);

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageButton) findViewById(R.id.img3);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);

        sup_id = getIntent().getExtras().getString("sup_id");
        Log.d("sup_id",""+sup_id);
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.img1:
                    Intent home = new Intent(this, Home.class);
                    startActivity(home);
                    break;

                case R.id.img2:
                    Intent category = new Intent(this, CategoryList.class);
                    startActivity(category);
                    break;

                case R.id.img3:
                    Intent search = new Intent(this, Search.class);
                    startActivity(search);
                    break;
            }
        }
    }
}

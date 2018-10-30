package cityscann.com.city_scann.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import cityscann.com.city_scann.adapter.CategoryL2Adapter;
import cityscann.com.city_scann.listener.RecyclerItemClickListener;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class CategoryL2List extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = CategoryList.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    ArrayList<HashMap<String, String>> itemLists;
    ProgressDialog pd;
    int position;
    LinearLayout nodata;
    RelativeLayout data;
    String parent_cat_id, parent_cat_name;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    private ConnectionDetector cd;
    CategoryList cat;
    ImageButton img1, img2, img3,img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorylevel);

        final ArrayList<HashMap<String, String>> leadslist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");
        parent_cat_id = leadslist.get(position).get("cat_id");
        Log.d("cat_id",""+parent_cat_id);
        parent_cat_name = leadslist.get(position).get("cat_name");

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
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final HashMap<String, String> hm;
                hm = itemLists.get(position);

                String cat_name = hm.get("cat_name");

                isGPSPresent = cd.isGPSon();
                if (isGPSPresent) {

                        Intent intent = new Intent(CategoryL2List.this, SupplierList.class);
                        intent.putExtra("item", itemLists);
                        intent.putExtra("Positions", position);
                        startActivity(intent);

                } else {
                    showSettingsAlert();
                }
            }
        }));
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);



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

                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_subcategory.php?catId=" + parent_cat_id
                + "&email=" + preferencesClass.getuser_email() + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName(), new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d("HelloArundata", response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);

                    } else if (response.getString("status").equals("-1")){
                        nodata.setVisibility(View.VISIBLE);
                        data.setVisibility(View.GONE);
                        preferencesClass.setUserToken(null);
                        preferencesClass.setusername(null);
                        preferencesClass.setphone(null);
                        preferencesClass.setuser_email(null);
                        Intent i = new Intent(CategoryL2List.this, selection_hotel.class);
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
                            hashMap.put("cat_id", jsonObject.getString("id"));
                            hashMap.put("cat_name", jsonObject.getString("catname"));
                            hashMap.put("cat_description", jsonObject.getString("catdescription"));
                            hashMap.put("cat_image", jsonObject.getString("catimage"));
                            hashMap.put("level", jsonObject.getString("level"));
                            hashMap.put("parentid", jsonObject.getString("parentid"));
                            hashMap.put("catlabel", jsonObject.getString("catlabel"));
                            hashMap.put("google_key", jsonObject.getString("google_key"));
                            hashMap.put("google_type", jsonObject.getString("google_type"));
                            itemLists.add(hashMap);
                        }
                        CategoryL2Adapter dAdapter = new CategoryL2Adapter(mContext, itemLists);
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

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void defineviewforitem() {
        nodata=(LinearLayout)findViewById(R.id.no_data);
        data=(RelativeLayout) findViewById(R.id.data);
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        Log.d("parent_name", "" + parent_cat_name);
        toolbartitle.setText(parent_cat_name);
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);*/
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
      /*  img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location");
        alertDialog.setMessage("Location is not enabled. Enable Now?");
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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

               /* case R.id.img3:
                    Intent search = new Intent(this, Search.class);
                    startActivity(search);
                    break;
                case R.id.img4:
                    final Dialog dialog = new Dialog(CategoryL2List.this);
                    dialog.setContentView(R.layout.logout);
                    dialog.show();

                    break;*/
            }
        }
    }
}

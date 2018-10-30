package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.Laundery_category_adapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Laundry_category extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    public static final String TAG_NAME = "category_name";
    public static final String ID = "m_id";
    public static final String DATA_URL = "request_all_menu_category.php";
    public static final String IMAGE = "image";
    LinearLayout Opencart;
    ArrayList prgmName;
    private RelativeLayout mRelativeLayout;
    private ArrayList<String> names;
    private ArrayList<String> categoryid;
    private ArrayList<String> image;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3,img4;
    Intent intent;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    String type;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laundry_category);
        defineviewforitem();
        Bundle gt=getIntent().getExtras();
        if(gt !=null) {
            type = gt.getString("Cid");
        }
//        Log.d("typeFood",type);

        recyclerView = (RecyclerView)findViewById(R.id.e_menu);
        recyclerView.setHasFixedSize(true);

        names = new ArrayList<String>();
        categoryid= new ArrayList<String>();
        image= new ArrayList<String>();

        // mRelativeLayout = (RelativeLayout) findViewById(R.id.Laundry_category);
        layoutManager = new LinearLayoutManager(Laundry_category.this);

       /* mLayoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        );*/

        recyclerView.setLayoutManager(layoutManager);

        Opencart=(LinearLayout)findViewById(R.id.Opencart);
        Opencart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Laundry_category.this,HouseKeepingCart.class);
                startActivity(intent);
            }
        });
        getData();

    }

    private void getData(){

        AppController.getInstance().getRequestQueue().getCache().invalidate(DATA_URL, true);
        final ProgressDialog loading = ProgressDialog.show(this, "","",false,false);
        Log.d("RAMJSON",getResources().getString(R.string.ip_address)+DATA_URL+"?type="+type+"&h_id="+preferencesClass.getHid());
        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?type="+type+"&h_id="+preferencesClass.getHid()+"&token="+preferencesClass.getUserToken(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                showGrid(response);
                Log.d("Hellocategory",""+response);
            }

            private void showGrid(JSONArray jsonArray) {
                //Looping through all the elements of json array
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        Log.d("Imageobj",""+obj.getString(IMAGE));
                        names.add(obj.getString(TAG_NAME));
                        categoryid.add(obj.getString(ID));
                        image.add(obj.getString(IMAGE));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Laundery_category_adapter adapter = new Laundery_category_adapter(Laundry_category.this,names,categoryid,image);
                recyclerView.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

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
                Intent i = new Intent(Laundry_category.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Menu Items");
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
       /* img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(Laundry_category.this, selection_hotel.class);
                startActivity(intent);
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
                final Dialog dialog = new Dialog(Laundry_category.this);
                dialog.setContentView(R.layout.logout);
                dialog.show();
                break;*/
        }
    }

}

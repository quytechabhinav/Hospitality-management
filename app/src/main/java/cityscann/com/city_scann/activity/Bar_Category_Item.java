package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.Bar_Item_Adopter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

import static cityscann.com.city_scann.utils.AppController.getContext;

public class Bar_Category_Item extends AppCompatActivity implements View.OnClickListener {
    String cat_id,spa_id,CATNAME;
    //Web api url
    public static final String DATA_URL = "request_all_menu_item.php";
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "item_name";
    public static final String PRICE = "cost";
    public static final String DEC = "item_details";
    public static final String T_ID ="it_id";
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> price;
    private ArrayList<String> it_ids;
    private ArrayList<String> sdescription;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3,img4;
    ImageView search;
    LinearLayout Opencart;
    ArrayList<Integer> itemno;
    PreferencesClass preferencesClass=new PreferencesClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar__category__item);
        defineviewforitem();
        if( preferencesClass.getUserToken() != null ){

        }else {
            Log.d("Session",""+preferencesClass.getUserToken());
            Intent i = new Intent(Bar_Category_Item.this,Licence_agreement.class);
            startActivity(i);
        }

        Bundle bundle = getIntent().getExtras();
        cat_id = bundle.getString("URTEXT");
        CATNAME = bundle.getString("CATNAME");
        Opencart=(LinearLayout)findViewById(R.id.Opencart);
        Opencart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Bar_Category_Item.this,HouseKeepingCart.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.item_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        images = new ArrayList<>();
        names = new ArrayList<>();
        price = new ArrayList<>();
        it_ids=new ArrayList<>();
        itemno=new ArrayList<Integer>();
        sdescription= new ArrayList<>();
        getData();

    }

    private void getData(){
        AppController.getInstance().getRequestQueue().getCache().invalidate(DATA_URL+"?cat_id="+cat_id, true);
        final ProgressDialog loading = ProgressDialog.show(this, " "," ",false,false);

        //Creating a json array request to get the json from our api
       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?m_id="+cat_id+"&type="+5+"&h_id="+preferencesClass.getHid()+"&token="+preferencesClass.getUserToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loading.dismiss();
                        showGrid(response);

                    }
                    private void showGrid(JSONArray jsonArray) {
                        for(int i = 0; i<jsonArray.length(); i++){
                            JSONObject obj = null;
                            try {
                                obj = jsonArray.getJSONObject(i);
                                Log.d("Objectofid",""+obj.getString(T_ID));
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                                price.add(obj.getString(PRICE));
                                it_ids.add(obj.getString(T_ID));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Bar_Item_Adopter adapter = new Bar_Item_Adopter(Bar_Category_Item.this,names,images,price,it_ids,sdescription);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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
                Intent i = new Intent(Bar_Category_Item.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        Bundle bundle = getIntent().getExtras();
        String cat_name = bundle.getString("CATNAME");
        toolbartitle.setText(cat_name);
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
        img4 = (ImageButton) findViewById(R.id.img4);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(Bar_Category_Item.this, selection_hotel.class);
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
                final Dialog dialog = new Dialog(Bar_Category_Item.this);
                dialog.setContentView(R.layout.logout);
                dialog.show();

                break;*/
        }
    }
}

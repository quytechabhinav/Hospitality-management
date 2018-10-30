package cityscann.com.city_scann.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import cityscann.com.city_scann.adapter.Menu_category_itemAdapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

import static cityscann.com.city_scann.utils.AppController.getContext;

public class Menu_category_item extends AppCompatActivity implements View.OnClickListener {
    String cat_id;
    //Web api url
    public static final String DATA_URL = "request_all_menu_item.php";
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "item_name";
    public static final String PRICE = "cost";
    public static final String DEC = "details";
    public static final String ID = "it_id";
    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> price;
    private ArrayList<String> id;
    private ArrayList<String> sdescription;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3,img4;
    LinearLayout Opencart;
    ArrayList<Integer> itemno;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_category_item);
        defineviewforitem();

        Bundle bundle = getIntent().getExtras();
        cat_id = bundle.getString("URTEXT");
        //Log.d("Abhinav",cat_id);
        Opencart=(LinearLayout)findViewById(R.id.Opencart);
        Opencart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Menu_category_item.this,HouseKeepingCart.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.item_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        images = new ArrayList<>();
        names = new ArrayList<>();
        price = new ArrayList<>();
        id=new ArrayList<>();
        itemno=new ArrayList<Integer>();
        sdescription= new ArrayList<>();
        getData();

    }

    private void getData(){
        AppController.getInstance().getRequestQueue().getCache().invalidate(DATA_URL+"?cat_id="+cat_id, true);
        final ProgressDialog loading = ProgressDialog.show(this, " "," ",false,false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?m_id="+cat_id+"&type="+6+"&h_id="+preferencesClass.getHid()+"&token="+preferencesClass.getUserToken(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        loading.dismiss();
                        //Displaying our grid
                        showGrid(response);

                    }
                    private void showGrid(JSONArray jsonArray) {
                        //Looping through all the elements of json array
                        for(int i = 0; i<jsonArray.length(); i++){
                            //Creating a json object of the current index
                            JSONObject obj = null;
                            try {
                                //getting json object from current index
                                obj = jsonArray.getJSONObject(i);
                                images.add(obj.getString(TAG_IMAGE_URL));
                                names.add(obj.getString(TAG_NAME));
                                price.add(obj.getString(PRICE));
                                sdescription.add(obj.getString(DEC));
                                id.add(obj.getString(ID));
                                itemno.add(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Menu_category_itemAdapter adapter = new Menu_category_itemAdapter(Menu_category_item.this,names,images,price,sdescription,id);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }


    private void defineviewforitem() {
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
       /* img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);
        img4.setOnClickListener(this);*/

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(Menu_category_item.this, selection_hotel.class);
                startActivity(intent);
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
                final Dialog dialog = new Dialog(Menu_category_item.this);
                dialog.setContentView(R.layout.logout);
                dialog.show();
                break;*/
        }
    }
}

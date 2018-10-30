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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.CartItemsAdapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class HouseKeepingCart extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private Context context;
    private EditText sinstruction;
    public static final String ItemName = "item_name";
    public static final String Quentity = "quantity";
    public static final String COST = "cost";
    public static final String ITEM_ID = "item_id";
    public static final String TOTAL = "total_cost";
    public static final String STATUS = "status";
    public static final String DATA_URL = "request_all_order_view.php";
    public static final String DATA_URL_conform = "request_all_order_confirmed.php";
    PreferencesClass preferencesClass=new PreferencesClass(this);
    private ArrayList<String> Oredr_Itemname;
    private ArrayList<String>  Oredr_Quentity;
    private ArrayList<String>  Oredr_COST;
    private ArrayList<String>  Oredr_TOTAL;
    private ArrayList<String>  MSG_STATUS;
    private ArrayList<String>  order_ID;
    public TextView Conformorder;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageView search;
    ImageButton img1, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.housekeepingcart);
        defineviewforitem();
        recyclerView = (RecyclerView)findViewById(R.id.cart_item);
        recyclerView.setHasFixedSize(true);

        Oredr_Itemname = new ArrayList<String>();
        Oredr_Quentity= new ArrayList<String>();
        Oredr_COST= new ArrayList<String>();
        Oredr_TOTAL=new ArrayList<String>();
        MSG_STATUS=new ArrayList<String>();
        order_ID=new ArrayList<String>();

        layoutManager = new LinearLayoutManager(HouseKeepingCart.this);
        recyclerView.setLayoutManager(layoutManager);
        sinstruction=(EditText)findViewById(R.id.sinstruction);
        Conformorder=(TextView)findViewById(R.id.Conformorder);


        Conformorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().getRequestQueue().getCache().invalidate(DATA_URL, true);
                String instruction = sinstruction.getText().toString();
                instruction=instruction.replaceAll(" ", "%20");
                instruction=instruction.replaceAll(" ", "%20");
                final ProgressDialog loading = ProgressDialog.show(HouseKeepingCart.this, "We process your order. this may take a few second"," ",false,false);
                Log.d("instruction",getResources().getString(R.string.ip_address)+DATA_URL_conform+"?h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone()+"&instruction="+instruction);
                 StringRequest orderconform = new StringRequest(getResources().getString(R.string.ip_address)+DATA_URL_conform+"?h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone()+"&instruction="+instruction, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("success")) {
                            loading.dismiss();
                            Toast.makeText(HouseKeepingCart.this, "Your Order has been Sucessfully placed. You will recive call from our Kitchen !!!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HouseKeepingCart.this,Home.class);
                            startActivity(intent);
                        }
                        loading.dismiss();
                        Toast.makeText(HouseKeepingCart.this, "Network Issue ,Please try after some time !!!",
                                Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(HouseKeepingCart.this);
                requestQueue.add(orderconform);
            }



        });


        getData();
    }

    private void getData(){
        AppController.getInstance().getRequestQueue().getCache().invalidate(getResources().getString(R.string.ip_address)+DATA_URL+"?h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone(), true);
        final ProgressDialog loading = ProgressDialog.show(this, " "," ",false,false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                showGrid(response);
            }

            private void showGrid(JSONArray jsonArray) {
                //Looping through all the elements of json array
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        Oredr_Itemname.add(obj.getString(ItemName));
                        Oredr_Quentity.add(obj.getString(Quentity));
                        Oredr_COST.add(obj.getString(COST));
                        Oredr_TOTAL.add(obj.getString(TOTAL));
                        order_ID.add(obj.getString(ITEM_ID));
                        if(!obj.getString(ITEM_ID).equals("") ||!obj.getString(ITEM_ID).equals(" ") ||!obj.getString(ITEM_ID).equals(null))
                        {
                            Conformorder.setVisibility(View.VISIBLE);
                            sinstruction.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                CartItemsAdapter adapter = new CartItemsAdapter(HouseKeepingCart.this,Oredr_Itemname,Oredr_Quentity,Oredr_COST,Oredr_TOTAL,order_ID);
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
                Intent i = new Intent(HouseKeepingCart.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Order");
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

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(HouseKeepingCart.this, selection_hotel.class);
                startActivity(intent);
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

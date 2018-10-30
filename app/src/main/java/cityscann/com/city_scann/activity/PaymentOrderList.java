package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.PaymentOrderList_Adopter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class PaymentOrderList extends AppCompatActivity {

    public static final String ItemName = "item_name";
    public static final String Quentity = "quantity";
    public static final String COST = "cost";
    public static final String DATE = "order_date";
    public static final String TOTAL = "total_cost";
    public static final String DATA_URL_PaymentOrderList = "request_user_bill_detail.php";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView PayBill,Comment,txtsum;

    PreferencesClass preferencesClass=new PreferencesClass(this);
    private ArrayList<String> Oredr_Itemname;
    private ArrayList<String>  Oredr_Quentity;
    private ArrayList<String>  Oredr_COST;
    private ArrayList<String>  Oredr_TOTAL;
    private ArrayList<String>  Oredr_DATE;
    double sum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_order_list);


        recyclerView = (RecyclerView)findViewById(R.id.cart_item);
        recyclerView.setHasFixedSize(true);

        PayBill=(TextView)findViewById(R.id.PayBill);
        Comment=(TextView)findViewById(R.id.Comment);
        txtsum=(TextView)findViewById(R.id.txtsum);

        Oredr_Itemname = new ArrayList<String>();
        Oredr_Quentity= new ArrayList<String>();
        Oredr_COST= new ArrayList<String>();
        Oredr_TOTAL=new ArrayList<String>();
        Oredr_DATE=new ArrayList<String>();
        layoutManager = new LinearLayoutManager(PaymentOrderList.this);
        recyclerView.setLayoutManager(layoutManager);
        getData();


        PayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PaymentOrderList.this,Bill_Payment.class);
                intent.putExtra("Sum",String.valueOf(sum));
                startActivity(intent);
            }
        });

        Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment = new Intent(PaymentOrderList.this,Paymentcomment.class);
                startActivity(comment);
            }
        });
    }

    private void getData(){
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait..."," ",false,false);
        Log.d("Payment_Data",getResources().getString(R.string.ip_address)+DATA_URL_PaymentOrderList+"?token="+preferencesClass.getUserToken()+"&hid="+preferencesClass.getHid()+"&id="+preferencesClass.getphone());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL_PaymentOrderList+"?token="+preferencesClass.getUserToken()+"&hid="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                showGrid(response);
               // Log.d("Hello",""+response);
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
                        Oredr_DATE.add(obj.getString(DATE));
                        double d=Double.parseDouble(obj.getString(TOTAL));
                        sum+=d;
                        loading.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("qtySum",String.valueOf(sum) );
                txtsum.setText("Total: "+String.valueOf(sum));
                PaymentOrderList_Adopter adapter = new PaymentOrderList_Adopter(PaymentOrderList.this,Oredr_Itemname,Oredr_Quentity,Oredr_COST,Oredr_TOTAL,Oredr_DATE);
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
}

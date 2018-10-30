package cityscann.com.city_scann.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

/**
 * Created by Praveen on 06-04-17.
 */

public class Spa_cart_adopter extends RecyclerView.Adapter<Spa_cart_adopter.ViewHolder>  {

    private Context context;
    private ArrayList<String> Oredr_Itemname;
    private ArrayList<String> Oredr_Quentity;
    private ArrayList<String> Oredr_COST;
    private ArrayList<String> Oredr_TOTAL;
    private ArrayList<String>order_ID;
    private String DATA_URL="request_menu_cart_update_quentity.php";
    private int a,c;
    private String item_ID;
    PreferencesClass preferencesClass;
    public Spa_cart_adopter(Context context,ArrayList<String> Oredr_Itemname,ArrayList<String> Oredr_Quentity,ArrayList<String> Oredr_COST,ArrayList<String> Oredr_TOTAL,ArrayList<String> order_ID) {
        this.context = context;
        this.Oredr_Itemname = Oredr_Itemname;
        this.Oredr_Quentity=Oredr_Quentity;
        this.Oredr_COST=Oredr_COST;
        this.Oredr_TOTAL=Oredr_TOTAL;
        this.order_ID=order_ID;
        preferencesClass=new PreferencesClass(context);
    }

    public Spa_cart_adopter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spa_cart_adopter, viewGroup, false);
        return new Spa_cart_adopter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Spa_cart_adopter.ViewHolder holder, int i) {
        holder.textView.setText(Oredr_Itemname.get(i));
        holder.quentity.setText(Oredr_Quentity.get(i));
        holder.cost.setText(Oredr_COST.get(i));
        holder.tot.setText(Oredr_TOTAL.get(i));
    }

    @Override
    public int getItemCount() {

        return Oredr_Itemname.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,quentity,cost,tot,sub,add;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txt);
            quentity = (TextView) view.findViewById(R.id.quentity);
            cost = (TextView) view.findViewById(R.id.price);
            tot=(TextView)view.findViewById(R.id.total);
            sub=(TextView)view.findViewById(R.id.sub);
            add=(TextView)view.findViewById(R.id.add);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            a= Integer.parseInt(Oredr_Quentity.get(getAdapterPosition()));
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a=a-1;
                    if(a<=0)
                    {
                        a=0;
                    }
                    quentity.setText(String.valueOf(a));
                    item_ID=order_ID.get(getAdapterPosition());
                    c= Integer.parseInt(Oredr_COST.get(getAdapterPosition()))*a;
                    tot.setText(String.valueOf(c));
                    additemoncart();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a=a+1;
                    quentity.setText(String.valueOf(a));
                    item_ID=order_ID.get(getAdapterPosition());
                    c= Integer.parseInt(Oredr_COST.get(getAdapterPosition()))*a;
                    tot.setText(String.valueOf(c));
                    additemoncart();
                }
            });


        }
    }

    private void additemoncart() {
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait..","wow, Your selection is awesome..!",false);
        StringRequest stringRequest = new StringRequest(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+item_ID+"&quantity="+a+"&h_id="+preferencesClass.getHid()+"&type="+"spa", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddDataINDataBase",String.valueOf(response));
                loading.dismiss();
                if(response.contains("success")) {
                    if (response != null) {
                        /*Intent intent = new Intent(context, E_menu_category.class);
                        context.startActivity(intent);*/
                        // Toast.makeText(context, "Item ADD to Cart ,Sucessfully !", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(context, "Try Again !!!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","Error in log D");
                Toast.makeText(context.getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
}

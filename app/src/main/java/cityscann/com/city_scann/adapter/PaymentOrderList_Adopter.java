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

public class PaymentOrderList_Adopter extends RecyclerView.Adapter<PaymentOrderList_Adopter.ViewHolder>  {

    private Context context;
    private ArrayList<String> Oredr_Itemname;
    private ArrayList<String> Oredr_Quentity;
    private ArrayList<String> Oredr_COST;
    private ArrayList<String> Oredr_TOTAL;
    private ArrayList<String>Oredr_DATE;
    PreferencesClass preferencesClass;
    private String DATA_URL="request_menu_cart_update_quentity.php";
    private String item_ID;
    public PaymentOrderList_Adopter(Context context,ArrayList<String> Oredr_Itemname,ArrayList<String> Oredr_Quentity,ArrayList<String> Oredr_COST,ArrayList<String> Oredr_TOTAL,ArrayList<String> Oredr_DATE) {
        this.context = context;
        this.Oredr_Itemname = Oredr_Itemname;
        this.Oredr_Quentity=Oredr_Quentity;
        this.Oredr_COST=Oredr_COST;
        this.Oredr_TOTAL=Oredr_TOTAL;
        Log.d("PayItem",""+Oredr_TOTAL);
        this.Oredr_DATE=Oredr_DATE;
        preferencesClass=new PreferencesClass(context);
    }

    public PaymentOrderList_Adopter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_order_list__adopter, viewGroup, false);
        return new PaymentOrderList_Adopter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentOrderList_Adopter.ViewHolder holder, int i) {
        holder.textView.setText(Oredr_Itemname.get(i));
        holder.quentity.setText(Oredr_Quentity.get(i));
        holder.cost.setText(Oredr_COST.get(i));
        holder.tot.setText(Oredr_TOTAL.get(i));
        holder.DateView.setText(Oredr_DATE.get(i));
    }

    @Override
    public int getItemCount() {

        return Oredr_Itemname.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView,quentity,cost,tot,DateView;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txt);
            quentity = (TextView) view.findViewById(R.id.quentity);
            cost = (TextView) view.findViewById(R.id.price);
            tot=(TextView)view.findViewById(R.id.total);
            DateView=(TextView)view.findViewById(R.id.DateView);

        }


    }
}

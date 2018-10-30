package cityscann.com.city_scann.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.activity.E_menu_category;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;


/**
 * Created by Praveen on 04-04-17.
 */

public class Spa_category_itemAdapter extends RecyclerView.Adapter<Spa_category_itemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> itemName;
    private ArrayList<String> images;
    private ArrayList<String> price;
    private ArrayList<String> it_id;
    private ArrayList<String> sdescription;
    public  ArrayList<Integer> a;
    public  int c;
    PreferencesClass preferencesClass;
    public String ItemID,itemquentity,Instruction;
    public String DATA_URL="request_all_type_item_order.php";

    public Spa_category_itemAdapter(Context context,ArrayList<String> names,ArrayList<String> images,ArrayList<String> price,ArrayList<String>it_id,ArrayList<String> sdescription) {
        this.context = context;
        this.itemName = names;
        this.images=images;
        this.it_id=it_id;
        this.price = price;
        preferencesClass=new PreferencesClass(context);

        this.sdescription=sdescription;
        Log.d("ItemID_all",""+it_id);
        a=new ArrayList<Integer>();
        for(int i=0;i<=names.size();i++)
        {
            a.add(0);
            Log.d("NameSize",""+i);
        }

    }

    @Override
    public Spa_category_itemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spa_category_item_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Spa_category_itemAdapter.ViewHolder viewHolder, final int i) {

        //viewHolder.textView.setText("abhi");
        viewHolder.textView.setText(itemName.get(i));
        viewHolder.priceitem.setText("Price: "+price.get(i));
        Picasso.with(context).load("http://cityscann.in/media/"+images.get(i)).into(viewHolder.itemimg);
      //  viewHolder.detail_item.setText(sdescription.get(i));

        if (a.get(i)==0){
            viewHolder.quentity.setText("0");
        }else {
            viewHolder.quentity.setText(a.get(i)+"");
        }


        viewHolder.additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemID=it_id.get(i);
                Log.d("ItemID",ItemID);
                c = a.get(i);
                c++;
                viewHolder.quentity.setText(c+"");
                a.set(i,c);
                additemoncart();
            }

        });

        viewHolder.subitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemID=it_id.get(i);
                if (c >0) {

                    c = a.get(i);
                    c--;
                    viewHolder.quentity.setText(c+"");
                    a.set(i,c);
                    subitemoncart();
                }
                else{
                    c=0;
                    viewHolder.quentity.setText(c+"");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView,priceitem,detail_item,additem,subitem,quentity;
        public ImageView itemimg;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView)view.findViewById(R.id.item);
            priceitem = (TextView)view.findViewById(R.id.price);
            itemimg =(ImageView)view.findViewById(R.id.itemimage);
            detail_item=(TextView)view.findViewById(R.id.detail);
            additem=(TextView)view.findViewById(R.id.add);
            subitem=(TextView)view.findViewById(R.id.sub);
            quentity=(TextView)view.findViewById(R.id.qu);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           /* ItemID=id.get(getAdapterPosition());
            additem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.set( getAdapterPosition(), a.get(getAdapterPosition()) +1 );
                    //quentity.setText(a.get(getAdapterPosition())+"");
                    itemquentity= String.valueOf(a.get(getAdapterPosition()));
                    Log.d("vineet", getAdapterPosition()+"");
                    additemoncart();
                    quentity.setText(itemquentity);

                }

            });*/






          /*  final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.emenu_popup);
            dialog.setTitle(itemName.get(getAdapterPosition()));

            TextView titelview = (TextView) dialog.findViewById(R.id.title);
            titelview.setText(itemName.get(getAdapterPosition()));

            TextView description=(TextView)dialog.findViewById(R.id.description);
            description.setText(sdescription.get(getAdapterPosition()));

            TextView textprice=(TextView)dialog.findViewById(R.id.textprice);
            textprice.setText(price.get(getAdapterPosition()));

            Button buttonsub =(Button)dialog.findViewById(R.id.buttonsub);
            buttonsub.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    s=s-1;
                    TextView TxtQuentity=(TextView)dialog.findViewById(R.id.quentity);
                    if(s<0) {
                        TxtQuentity.setText("0");
                        s=0;
                    }
                    else{TxtQuentity.setText(String.valueOf(s));}

                }
            });

            Button buttonadd =(Button)dialog.findViewById(R.id.buttonadd);
            buttonadd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    s=s+1;
                    TextView TxtQuentity=(TextView)dialog.findViewById(R.id.quentity);
                    TxtQuentity.setText(String.valueOf(s));

                }
            });

            Button Addtocart =(Button)dialog.findViewById(R.id.addtocard);
            Addtocart.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    TextView EdtQuentity=(TextView)dialog.findViewById(R.id.specialrequest);
                    Instruction= EdtQuentity.getText().toString();
                    itemquentity=String.valueOf(s);
                    ItemID=id.get(getAdapterPosition());

                    StringRequest stringRequest = new StringRequest(DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&instruction="+Instruction, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            //  Log.d("AddDataINDataBase",String.valueOf(response));
                            if(response.contains("success")) {
                                if (response != null) {
                                    Intent intent = new Intent(context, E_menu_category.class);
                                    context.startActivity(intent);

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
                               *//* params.put("price", orderprice);
                                params.put("quentity", String.valueOf(c));
                                params.put("Tableid", session.getTableid());
                                params.put("Waiterid", session.getWaiterid());*//*

                            return params;
                        }


                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }
            });


          dialog.show();
*/
        }
    }

    private void additemoncart() {

        Instruction=" ";
        Log.d("item_id",ItemID);
        itemquentity= String.valueOf(c);
      //  AppController.getInstance().getRequestQueue().getCache().invalidate(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+10+"type="+"spa", true);

        final ProgressDialog loading = ProgressDialog.show(context, "Please wait..","wow, Your selection is awesome..!",false,false);
        Log.d("OURl",context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&type="+"spa"+"&instruction="+Instruction);
        StringRequest stringRequest = new StringRequest(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&type="+"spa"+"&instruction="+Instruction, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AddDataINDataBase",String.valueOf(response));
                loading.dismiss();
                if(response.contains("success")) {
                    if (response != null) {

                        /*Intent intent = new Intent(context, E_menu_category.class);
                        context.startActivity(intent);*/
                        //Toast.makeText(context, "Item ADD to Cart ,Sucessfully !", Toast.LENGTH_LONG).show();

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
                               /* params.put("price", orderprice);
                                params.put("quentity", String.valueOf(c));
                                params.put("Tableid", session.getTableid());
                                params.put("Waiterid", session.getWaiterid());*/

                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }



    private void subitemoncart() {
        itemquentity= String.valueOf(c);
        Instruction="";
        Log.d("item_id",ItemID);

        final ProgressDialog loading = ProgressDialog.show(context, "Please wait..","...",false,false);
        Log.d("OURl",context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&instruction="+Instruction+"&h_id="+10);
        StringRequest stringRequest = new StringRequest(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&type="+"spa"+"&instruction="+Instruction, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AddDataINDataBase",String.valueOf(response));
                loading.dismiss();
                if(response.contains("success")) {
                    if (response != null) {
                        //loading.dismiss();
                        /*Intent intent = new Intent(context, E_menu_category.class);
                        context.startActivity(intent);*/
                        //Toast.makeText(context, "Item ADD to Cart ,Sucessfully !", Toast.LENGTH_LONG).show();

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
                               /* params.put("price", orderprice);
                                params.put("quentity", String.valueOf(c));
                                params.put("Tableid", session.getTableid());
                                params.put("Waiterid", session.getWaiterid());*/

                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


}

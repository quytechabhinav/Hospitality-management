package cityscann.com.city_scann.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;


/**
 * Created by Praveen on 04-04-17.
 */

public class Menu_category_itemAdapter extends RecyclerView.Adapter<Menu_category_itemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> itemName;
    private ArrayList<String> images;
    private ArrayList<String> price;
    private ArrayList<String> sdescription;
    private ArrayList<String> id;
    public  ArrayList<Integer> a;
    public  int c,c1;

    public String ItemID,itemquentity,Instruction;
    public String DATA_URL="request_all_type_item_order.php";
    PreferencesClass preferencesClass;

    public Menu_category_itemAdapter(Context context,ArrayList<String> names,ArrayList<String> images,ArrayList<String> price,ArrayList<String> sdescription,ArrayList<String> id) {
        this.context = context;
        this.itemName = names;
        this.images=images;
        this.price = price;
        this.id=id;
        this.sdescription=sdescription;

        a=new ArrayList<Integer>();
        for(int i=0;i<=names.size();i++)
        {
            a.add(0);
        }
        preferencesClass=new PreferencesClass(context);
    }

    @Override
    public Menu_category_itemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.e_menu_item_list_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Menu_category_itemAdapter.ViewHolder viewHolder, final int i) {

        //viewHolder.textView.setText("abhi");
        viewHolder.textView.setText(itemName.get(i));
        viewHolder.priceitem.setText("Price: "+price.get(i));
        Picasso.with(context).load("http://cityscann.in/media/"+images.get(i)).into(viewHolder.itemimg);
        //viewHolder.detail_item.setText(sdescription.get(i));

        if (a.get(i)==0){
            viewHolder.quentity.setText("0");
        }else {
            viewHolder.quentity.setText(a.get(i)+"");
        }


        viewHolder.additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemID=id.get(i);
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
                ItemID=id.get(i);
                if (c >0) {

                    c = a.get(i);
                    c--;
                    additemoncart();
                    viewHolder.quentity.setText(c+"");
                    a.set(i,c);
                    //subitemoncart();
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
        return price.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView,priceitem,detail_item,additem,subitem,quentity;
        private ImageView itemimg;
        private LinearLayout sup_img_lay;
        private RelativeLayout rel;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.item);
            priceitem = (TextView)itemView.findViewById(R.id.price);
           itemimg =(ImageView)itemView.findViewById(R.id.menuimage);
           // detail_item=(TextView)itemView.findViewById(R.id.detail);
            additem=(TextView)itemView.findViewById(R.id.add);
            subitem=(TextView)itemView.findViewById(R.id.sub);
            quentity=(TextView)itemView.findViewById(R.id.qu);
            rel=(RelativeLayout)itemView.findViewById(R.id.menuxmlid);

            rel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            c1=0;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.emenu_popup);
            dialog.setTitle(itemName.get(getAdapterPosition()));

            TextView titelview = (TextView) dialog.findViewById(R.id.title);
            titelview.setText(itemName.get(getAdapterPosition()));

            TextView description=(TextView)dialog.findViewById(R.id.description);
            description.setText(sdescription.get(getAdapterPosition()));

            TextView textprice=(TextView)dialog.findViewById(R.id.textprice);
            textprice.setText("RS "+price.get(getAdapterPosition()));

            ImageView Img=(ImageView)dialog.findViewById(R.id.img);
            Picasso.with(context).load("http://cityscann.in/media/"+images.get(getAdapterPosition())).into(Img);

            ImageView closed=(ImageView)dialog.findViewById(R.id.closed);
            closed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


           /* Button buttonsub =(Button)dialog.findViewById(R.id.buttonsub);
            buttonsub.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    c1 = c1 - 1;
                    TextView TxtQuentity=(TextView)dialog.findViewById(R.id.quentity);
                    if(c1<0) {
                        TxtQuentity.setText("0");
                        c1=0;
                    }
                    else{TxtQuentity.setText(String.valueOf(c1)); c=c1;}

                }
            });

            Button buttonadd =(Button)dialog.findViewById(R.id.buttonadd);
            buttonadd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    c1=c1+1;
                    TextView TxtQuentity=(TextView)dialog.findViewById(R.id.quentity);
                    TxtQuentity.setText(String.valueOf(c1));
                    c=c1;

                }
            });

            Button Addtocart =(Button)dialog.findViewById(R.id.addtocard);
            Addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemID=id.get(getAdapterPosition());
                    additemoncart();
                    dialog.onBackPressed();
                }
            });
        */
            dialog.show();
        }
    }

    private void additemoncart() {

        Instruction=" ";
        String type="InRoomDining";
        itemquentity= String.valueOf(c);
        AppController.getInstance().getRequestQueue().getCache().invalidate(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid(), true);
        Log.d("additemoncart",context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone()+"&mobile="+preferencesClass.getMobilenu()+"&room_no="+preferencesClass.getRoomNu()+"&type="+type);
        final ProgressDialog loading = ProgressDialog.show(context, "Please wait..","wow, Your selection is awesome..!",false,false);
        StringRequest stringRequest = new StringRequest(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&uid="+preferencesClass.getphone()+"&mobile="+preferencesClass.getMobilenu()+"&room_no="+preferencesClass.getRoomNu()+"&type="+type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")) {
                    if (response != null) {
                        loading.dismiss();
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

        StringRequest stringRequest = new StringRequest(context.getString(R.string.ip_address)+DATA_URL+"?item_id="+ItemID+"&quantity="+itemquentity+"&h_id="+preferencesClass.getHid()+"&type="+"InRoomDining"+"&uid="+preferencesClass.getphone()+"&mobile="+preferencesClass.getMobilenu()+"&room_no="+preferencesClass.getRoomNu(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


}

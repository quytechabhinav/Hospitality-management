package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import static cityscann.com.city_scann.utils.AppController.getContext;

public class Treatment_package {

    public static final String DATA_URL = "http://cityscann.in/app/request_menu_item_master.php";
    public static final String TAG_IMAGE_URL = "item_image";
    public static final String TAG_NAME = "item";
    public static final String PRICE = "price";
    public static final String DEC = "item_details";
    public static final String ID = "id";
    public String p_id;

    private ArrayList<String> images;
    private ArrayList<String> names;
    private ArrayList<String> price;
    private ArrayList<String> id;
    private ArrayList<String> sdescription;
    RecyclerView recyclerView;

    private final Dialog dialog;
    private Activity act;
    private ImageButton closeButton;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;

    public Treatment_package(final Activity act,String ID) {
        p_id=ID;
        this.act=act;
        dialog =new Dialog(act,R.style.FullHeightDialog);
        dialog.setContentView(R.layout.treatment_package);
        closeButton = (ImageButton)dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        recyclerView = (RecyclerView)dialog.findViewById(R.id.P_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        images = new ArrayList<>();
        names = new ArrayList<>();
        price = new ArrayList<>();
        id=new ArrayList<>();
        sdescription= new ArrayList<>();

        Getpackage();
        dialog.show();
    }

    private void Getpackage() {

            // AppController.getInstance().getRequestQueue().getCache().invalidate(DATA_URL+"?cat_id="+cat_id, true);
            final ProgressDialog loading = ProgressDialog.show(act, "Please wait...","Fetching data...",false,false);

            //Creating a json array request to get the json from our api
            Log.d("aurl",DATA_URL+"?cat_id="+p_id);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL+"?cat_id="+p_id,
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                         //   Menu_category_itemAdapter adapter = new Menu_category_itemAdapter(act,names,images,price,sdescription,id);
                         //   recyclerView.setAdapter(adapter);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            //Creating a request queue
            RequestQueue requestQueue = Volley.newRequestQueue(act);
            //Adding our request to the queue
            requestQueue.add(jsonArrayRequest);



    }


}

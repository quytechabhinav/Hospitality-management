package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.SpaBaseAdapterFirst;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class SPA_First extends AppCompatActivity implements View.OnClickListener {
    private GridView spaGridView;
    private ProgressDialog pd;
    private static String TAG = SPA_First.class.getSimpleName();
    private String[] name,image,logoImage,description,id;
    HashMap<String,String> map;
    private String URL ="http://cityscann.in/app/request_hotelfacilitydetails.php";
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3;
    String hid;
    ImageView search;
    PreferencesClass preferencesClass=new PreferencesClass(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spa__first);
        spaGridView = (GridView)findViewById(R.id.spaGridView);
        Bundle extras = getIntent().getExtras();
        hid= extras.getString("facilityid");
        defineviewforitem();
        downloadList();
    }

    private void downloadList() {
        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,URL+"?fid="+hid , new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("g_url", response.toString());
                try {
                    if (response.getString("status").equals("0")) {

                    }  else if (response.getString("status").equals("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");
                        name = new String[response.length()];
                        image = new String[response.length()];
                        logoImage = new String[response.length()];
                        description = new String[response.length()];
                        id=new String[response.length()];
                        final int end = response.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            name[i] = jsonObject.getString("name");
                            image[i]= jsonObject.getString("image1");
                            logoImage[i] = jsonObject.getString("image2");
                            description[i] = jsonObject.getString("description");
                            id[i]=jsonObject.getString("id");


                        }

                        spaGridView.setAdapter( new SpaBaseAdapterFirst(SPA_First.this, name,image,logoImage,description,id));

                    }pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);

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
                Intent i = new Intent(SPA_First.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Spa");
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
                Intent home = new Intent(this, Home.class);
                startActivity(home);
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

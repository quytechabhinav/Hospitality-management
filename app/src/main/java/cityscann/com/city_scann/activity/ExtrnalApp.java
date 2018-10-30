package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.UsefulLinkAdapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class ExtrnalApp extends AppCompatActivity implements View.OnClickListener {

    PreferencesClass preferencesClass=new PreferencesClass(this);
    private static String TAG = ExtrnalApp.class.getSimpleName();
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    private Context mContext;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    ArrayList<HashMap<String, String>> itemLists;
    ProgressDialog pd;
    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    int position;
    String parent_cat_id, parent_cat_name;
    LinearLayout nodata;
    RelativeLayout data;
    ImageButton img1, img2, img3;
    LinearLayout ola,uber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extrnal_app);

        mContext = getApplicationContext();

        final ArrayList<HashMap<String, String>> leadslist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");
        parent_cat_id = leadslist.get(position).get("cat_id");
        parent_cat_name = leadslist.get(position).get("cat_name");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();

        }
        uber=(LinearLayout)findViewById(R.id.uber);
        ola=(LinearLayout)findViewById(R.id.ola);


        ola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.olacabs.customer")));
                }
            }
        });


        uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ubercab");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.ubercab")));
                }
            }
        });
    }

    private void defineviewforitem() {
        nodata=(LinearLayout)findViewById(R.id.no_data);
        data=(RelativeLayout) findViewById(R.id.data);
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText(parent_cat_name);
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
*/
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);*/
    }







    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.img1:
                    Intent home = new Intent(this, selection_hotel.class);
                    startActivity(home);
                    break;

                case R.id.img2:
                    Intent category = new Intent(this, CategoryList.class);
                    startActivity(category);
                    break;

                /*case R.id.img3:
                    Intent search = new Intent(this, Search.class);
                    startActivity(search);
                    break;*/
            }
        }
    }
}






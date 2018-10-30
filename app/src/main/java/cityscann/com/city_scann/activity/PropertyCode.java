package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

/**
 * Created by Abhinav on 14-01-2017.
 */

public class PropertyCode extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    boolean isInternetPresent = false;
    android.support.v7.widget.Toolbar toolbar;
    private static String TAG = PropertyCode.class.getSimpleName();
    TextView toolbartitle;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    HashMap<String,String> map;
    ProgressDialog pd;
    EditText property_code;
    Button submit_propCode;
    String propertyCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_code);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        
        defineView();
    }

    private void defineView() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Property Code");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        property_code = (EditText) findViewById(R.id.prop_code);
        submit_propCode = (Button) findViewById(R.id.submit_property_code);

        submit_propCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (checkValidation()) {
                submitpropCode();
               // Toast.makeText(getApplicationContext(), "Property XML: 1", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void submitpropCode() {
        propertyCode = property_code.getText().toString();

        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage(" ");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");
      //  Toast.makeText(getApplicationContext(), "Submit : 1", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_hotelid.php?hlabel=" + propertyCode , new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               // Toast.makeText(getApplicationContext(), "Server : 1", Toast.LENGTH_SHORT).show();

                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(PropertyCode.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (response.getString("status").equals("1")) {
                        String data = response.getString("data");
                        JSONObject jsonObject1 = new JSONObject(data);
                        String hotelid = jsonObject1.getString("id");
                        String statename = jsonObject1.getString("city");
                        preferencesClass.setStateName(propertyCode);
                        preferencesClass.setHid(hotelid);
                       // Log.d("propertyCode",propertyCode);
                        Intent i =new Intent(PropertyCode.this, selection_hotel.class);
                       // i.putExtra("PROPCODE", propertyCode);
                        startActivity(i);
                        finish();
                    }pd.dismiss();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check hotel property Code "+e, Toast.LENGTH_SHORT).show();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private boolean checkValidation() {
        propertyCode = property_code.getText().toString();

        if (propertyCode.length() == 0) {
            property_code.requestFocus();
            property_code.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        System.exit(0);
    }
}

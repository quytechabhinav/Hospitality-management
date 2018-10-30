package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Login extends AppCompatActivity implements View.OnClickListener {

    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    private ConnectionDetector cd;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    EditText mobile_no, pass;
    Button verify, register;
    String mobileNo, password;
    private static String TAG = Login.class.getSimpleName();
    CategoryList categoryList;
    GPSTracker gps;
    HashMap<String,String> map;
    ProgressDialog pd;
    ImageButton img1, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (preferencesClass.getUserToken() != null){
            Intent intent=new Intent(Login.this,CategoryList.class);
            startActivity(intent);
        }
        else {
            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            defineviewforitem();
            /*Intent intent=new Intent(Login.this,CategoryList.class);
            startActivity(intent);*/
        }
    }

    private void defineviewforitem() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Sign Up");

        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageButton) findViewById(R.id.img3);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        pass = (EditText) findViewById(R.id.password);
        verify = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.regis);

        verify.setOnClickListener(this);
        register.setOnClickListener(this);
        img1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        isGPSPresent = cd.isGPSon();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.login:
                    if(isGPSPresent) {
                        if (checkValidation()) {
                            downloadList();
                        }
                    }else {
                        showSettingsAlert();
                    }
                    break;
                case R.id.regis:
                    Intent i = new Intent(this, Register.class);
                    startActivity(i);
                    break;
                case R.id.img1:
                    Intent home = new Intent(this, Home.class);
                    startActivity(home);
                    break;

            }
        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Location");

        alertDialog.setMessage("Location is not enabled. Enable Now?");

        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void downloadList() {
        mobileNo = mobile_no.getText().toString();
        password = pass.getText().toString();
        stateName();

        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_login.php?phone=" + mobileNo + "&pwd=" + password, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d("Authoncationdata", response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(Login.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (response.getString("status").equals("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        final int end = jsonArray.length();
                        for (int i = 0; i < end; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            preferencesClass.setUserToken(jsonObject.getString("token"));
                            preferencesClass.setuser_email(jsonObject.getString("email"));
                            preferencesClass.setusername(jsonObject.getString("name"));
                            preferencesClass.setphone(jsonObject.getString("phone"));
                            Intent intent =new Intent(Login.this, CategoryList.class);
                            startActivity(intent);
                            finish();
                        }
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

    private void stateName() {
        gps = new GPSTracker(this);

        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            }else{

            gps.showSettingsAlert();
        }
    }

    private boolean checkValidation() {
        mobileNo = mobile_no.getText().toString();
        password = pass.getText().toString();

        if (mobileNo.length() == 0) {
            mobile_no.requestFocus();
            mobile_no.setError("Required");
            return false;
        }else if (password.length() == 0) {
            pass.requestFocus();
            pass.setError("Required");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }
}

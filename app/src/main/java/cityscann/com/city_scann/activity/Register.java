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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;


public class Register extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector cd;
    private static String TAG = Register.class.getSimpleName();
    PreferencesClass preferencesClass=new PreferencesClass(this);
    HashMap<String,String> map;
    ProgressDialog pd;
    boolean isInternetPresent = false;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    EditText mobile_no, no_of_days, pass1, pass2, email, name, prperty_code, device_id;
    String usermobile, username, useremail, password1, password2, days, propertcode, deviceid;
    Button register;
    ImageButton img1, img2, img3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        defineview();
    }

    private void defineview() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("New Sign Up");
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
        mobile_no = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        no_of_days = (EditText) findViewById(R.id.no_of_days);
        prperty_code = (EditText) findViewById(R.id.property_code);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(this);
        img1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.register:
                    if (checkValidation()) {
                        registeruser();
                    }
                    break;
                case R.id.img1:
                    Intent home = new Intent(this, Home.class);
                    startActivity(home);
                    break;
                case R.id.img2:
                    onBackPressed();
                    break;
                case R.id.img3:
                    onBackPressed();
                    break;
            }
        }
    }

    private void registeruser() {

        username = name.getText().toString();
        useremail = email.getText().toString();
        usermobile = mobile_no.getText().toString();
        password1 = pass1.getText().toString();
        days = no_of_days.getText().toString();
        propertcode = prperty_code.getText().toString();

        map=new HashMap<>();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        map.put("", "");

        String uname = username;
        uname = uname.replace(" ","%20");
        uname = uname.replace("\n","%0A");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address) + "request_registration.php?name=" + uname + "&email=" +
                useremail + "&phone=" + usermobile + "&pwd=" + password1 + "&noofdays=" + days + "&propertycode=" + propertcode + "&deviceid=" + "", new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getString("status").equals("0")) {
                        Toast.makeText(Register.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (response.getString("status").equals("1")) {
                        Intent i =new Intent(Register.this, Login.class);
                        startActivity(i);
                        finish();
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

    private boolean checkValidation() {
        username = name.getText().toString();
        useremail = email.getText().toString();
        usermobile = mobile_no.getText().toString();
        password1 = pass1.getText().toString();
        password2 = pass2.getText().toString();
        days = no_of_days.getText().toString();
        propertcode = prperty_code.getText().toString();

        String emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(useremail);

        if (username.length() == 0) {
            name.requestFocus();
            name.setError("Required");
            return false;
        } else if (useremail.length() == 0) {
            email.requestFocus();
            email.setError("Required");
            return false;
        } else if (usermobile.length() == 0) {
            mobile_no.requestFocus();
            mobile_no.setError("Required");
            return false;
        } else if (days.length() == 0) {
            no_of_days.requestFocus();
            no_of_days.setError("Required");
            return false;
        } else if (propertcode.length() == 0) {
            prperty_code.requestFocus();
            prperty_code.setError("Required");
            return false;
        } else if (password1.length() == 0) {
            pass1.requestFocus();
            pass1.setError("Required");
            return false;
        } else if (password2.length() == 0) {
            pass2.requestFocus();
            pass2.setError("Required");
            return false;
        } else if (!password1.equals(password2)) {
            pass2.requestFocus();
            pass2.setError("Passwords did not match");
            return false;
        } else if(!matcher.find()){
            email.requestFocus();
            email.setError("Invalid Email Id");
            return false;
        }
        return true;
    }
}

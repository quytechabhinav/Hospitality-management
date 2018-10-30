package cityscann.com.city_scann.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.GPSTracker;
import cityscann.com.city_scann.utils.PreferencesClass;

public class Licence_agreement extends AppCompatActivity {
    Button submit;
    ImageButton img1, img2, img3,img4;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    Button txtlicence;
    String deviceid,Hotel;
    String DATA_URL="request_login.php";
    EditText roomnumber,lastname;
    CheckBox aggrement;
    ImageView search;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    boolean isGPSPresent = false;
    GPSTracker gps;
    String verify,mPhoneNumber,DbMnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licence_agreement);
        defineviewforitem();
        roomnumber=(EditText) findViewById(R.id.roomnumber);
        lastname=(EditText) findViewById(R.id.lastname);
        aggrement=(CheckBox)findViewById(R.id.aggrement);
        txtlicence=(Button)findViewById(R.id.txtlicence);

        Intent intent = getIntent();
        Hotel = intent.getStringExtra("Hotel");
        txtlicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.parse("http://cityscann.in/agreemet.pdf"),
                        "application/pdf");

                startActivity(intent);

            }
        });
try {
    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
    deviceid = telephonyManager.getDeviceId();
    TelephonyManager tMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
    mPhoneNumber = tMgr.getLine1Number();
    if (mPhoneNumber.equals(null) || mPhoneNumber.equals("") || mPhoneNumber.equals(" ")) {
        DbMnum = "SeetingError";
    } else {
        DbMnum = mPhoneNumber;
    }
}
catch (Exception e){
    Toast.makeText(getApplicationContext(), "Sorry ,please contact with our technical team !!!"+e, Toast.LENGTH_SHORT).show();
}
            submit=(Button)findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(aggrement.isChecked()) {
                        getData();
                    }
                    else{

                        Toast.makeText(Licence_agreement.this, "You must agree With Terms and Conditions !", Toast.LENGTH_LONG).show();
                    }

                }
            });

    }

    private void getData(){

        stateName();

        final String room_no = roomnumber.getText().toString();
        final String last_name = lastname.getText().toString();

        AppController.getInstance().getRequestQueue().getCache().invalidate(getResources().getString(R.string.ip_address)+DATA_URL+"?room_no="+room_no+"&last_name="+last_name+"&h_id="+preferencesClass.getHid()+"&deviceId="+deviceid+"&propertycode="+preferencesClass.getStateName(), true);
        final ProgressDialog loading = ProgressDialog.show(this, " "," ",false,false);
       Log.d("Test",getResources().getString(R.string.ip_address)+DATA_URL+"?room_no="+room_no+"&last_name="+last_name+"&h_id="+preferencesClass.getHid()+"&deviceId="+deviceid+"&propertycode="+preferencesClass.getStateName()+"&phone="+DbMnum);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?room_no="+room_no+"&last_name="+last_name+"&h_id="+preferencesClass.getHid()+"&deviceId="+deviceid+"&propertycode="+preferencesClass.getStateName()+"&phone="+DbMnum,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                showGrid(response);
               Log.d("Hello",""+response);
            }

            private void showGrid(JSONArray jsonArray) {
                //Looping through all the elements of json array
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        preferencesClass.setusername(obj.getString("name"));
                        preferencesClass.setphone(obj.getString("id"));
                        preferencesClass.setUserToken(obj.getString("token"));
                        preferencesClass.setRoomNu(obj.getString("room_no"));
                        preferencesClass.setMobilenu(obj.getString("phone"));
                        preferencesClass.setuser_email("abhinav.vdoit@gmail.com");
                        verify=obj.getString("isverified");
                        Log.d("RoomNu",preferencesClass.getRoomNu());
                        Log.d("mobile",preferencesClass.getMobilenu());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(verify != null && verify.equals("1")) {
                        //Log.d("vdoit",""+verify);
                        if(Hotel.equals("1")) {
                            Intent intent = new Intent(Licence_agreement.this, selection_hotel.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(Licence_agreement.this, "success", Toast.LENGTH_LONG).show();
                        }
                        else if(Hotel.equals("2"))
                        {
                            Intent intent = new Intent(Licence_agreement.this, selection_hotel.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Log.d("vdoit",""+verify);
                        Toast.makeText(Licence_agreement.this,"Please contact our Reception for help !!!", Toast.LENGTH_LONG).show();

                    }
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    private void stateName() {
        gps = new GPSTracker(this);

        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            //Log.d("Licancelatitude", String.valueOf(+latitude));
            double longitude = gps.getLongitude();


        }else{

            gps.showSettingsAlert();
        }
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
                preferencesClass.setStateName(null);
                preferencesClass.setHid(null);
                Intent i = new Intent(Licence_agreement.this, PropertyCode.class);
                startActivity(i);
                finish();
            }
        });
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        toolbartitle=(TextView)findViewById(R.id.toolbar_title);

        toolbartitle.setText("");

       /* toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/


    }
}

package cityscann.com.city_scann.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class selection_hotel extends AppCompatActivity implements View.OnClickListener {
    PreferencesClass preferencesClass=new PreferencesClass(this);
    LinearLayout hotelimg,mycity;
    TextView hotelname,myphone;
    ImageView search;
    String DATA_URL="request_myhotel_mycity.php";
    String mPhoneNumber;
    android.support.v7.widget.Toolbar toolbar;
    ImageButton img1, img2, img3,img4;
    TextView toolbartitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_hotel);
        defineviewforitem();

        /*Intent intent = getIntent();
        propertyCode = intent.getStringExtra("PROPCODE");*/
        try {
            hotelimg = (LinearLayout) findViewById(R.id.hotelimg);
            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
            hotelimg.startAnimation(myAnim);
            hotelname = (TextView) findViewById(R.id.hotelname);
            mycity = (LinearLayout) findViewById(R.id.mycity);
            final Animation myAnim1 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            mycity.startAnimation(myAnim1);
        }
        catch (Exception e)
        {
            //Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        }
        myphone=(TextView)findViewById(R.id.myphone);
        try {
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber.equals(null) || mPhoneNumber.equals("") || mPhoneNumber.equals(" ")) {
                myphone.setText("9999999999");
            } else {
                Log.d("mPhoneNumber", mPhoneNumber);
                myphone.setText(mPhoneNumber);
            }
        }
        catch (Exception e)
        {
           // Toast.makeText(getApplicationContext(), "TELEPHONY_SERVICE error"+e, Toast.LENGTH_SHORT).show();
        }
        hotelimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preferencesClass.getUserToken()== null || preferencesClass.getHid()==null|| preferencesClass.getusername()==null||preferencesClass.getStateName()==null ||preferencesClass.getphone()==null){
                    Intent i =new Intent(selection_hotel.this, Licence_agreement.class);
                    i.putExtra("PROPCODE", preferencesClass.getStateName());
                    i.putExtra("Hotel", "1");
                    startActivity(i);
                }
                else {
                    Intent i =new Intent(selection_hotel.this, Home.class);
                    i.putExtra("PROPCODE", preferencesClass.getStateName());
                    i.putExtra("Hotel", "1");
                    startActivity(i);

                }
            }
        });

        HotelImage();

        mycity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preferencesClass.getUserToken()== null || preferencesClass.getHid()==null|| preferencesClass.getusername()==null||preferencesClass.getStateName()==null ||preferencesClass.getphone()==null){
                    Intent i =new Intent(selection_hotel.this, Licence_agreement.class);
                    i.putExtra("PROPCODE", preferencesClass.getStateName());
                    i.putExtra("Hotel", "2");
                    startActivity(i);
                }
                else {
                    Intent i =new Intent(selection_hotel.this, CategoryList.class);
                    i.putExtra("PROPCODE", preferencesClass.getStateName());
                    i.putExtra("Hotel", "1");
                    startActivity(i);

                }
            }
        });

        defineviewforitem();

    }

    private void defineviewforitem() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Dunstonchecks");
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
        img4 = (ImageButton) findViewById(R.id.img4);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);
        img4.setOnClickListener(this);
*/
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(selection_hotel.this, selection_hotel.class);
                startActivity(intent);
                break;

            case R.id.img2:
                Intent category = new Intent(this, CategoryList.class);
                startActivity(category);
                break;

            /*case R.id.img3:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                break;

            case R.id.img4:
                final Dialog dialog = new Dialog(selection_hotel.this);
                dialog.setContentView(R.layout.logout);
                dialog.show();

                break;*/
        }
    }

    private void HotelImage(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?hid="+preferencesClass.getHid(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                showGrid(response);
            }

            private void showGrid(JSONArray jsonArray) {
                //Looping through all the elements of json array
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        String banner =obj.getString("banner");
                        String name=obj.getString("hname");
                        hotelname.setText(name);
                        Picasso.with(selection_hotel.this).load("http://cityscann.in/media/"+banner).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                               // hotelimg.setBackground(new BitmapDrawable(bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Log.d("Error","Image");

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "please contact our Reception for help !!"+e, Toast.LENGTH_SHORT).show();
                       // e.printStackTrace();
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
}

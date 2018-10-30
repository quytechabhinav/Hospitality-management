package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.adapter.CartItemsAdapter;
import cityscann.com.city_scann.utils.AppController;
import cityscann.com.city_scann.utils.PreferencesClass;

public class RoomtoRoom extends Activity {
    public static final String ROOMNUmber = "room_no";
    public static final String MOBILENUmber = "phone";
    Spinner spinner;
    Button roomtoroomcall;
    ImageView closed;
    String callNumber;
    private ArrayList<String> ROOMNU;
    private ArrayList<String> MOBILENU;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    String DATA_URL="request_room_to_room_call.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.roomto_room);
            ROOMNU = new ArrayList<String>();
            MOBILENU = new ArrayList<String>();
            spinner= (Spinner) findViewById(R.id.spinner);
            roomtoroomcall=(Button)findViewById(R.id.roomtoroomcall);
            closed=(ImageView)findViewById(R.id.closed);
            getData();

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              // Toast.makeText(getApplicationContext(),"Please Select District"+MOBILENU.get(position), Toast.LENGTH_LONG).show();
               callNumber= MOBILENU.get(position);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        roomtoroomcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roomtoroom = new Intent(Intent.ACTION_CALL);
                roomtoroom.setData(Uri.parse("tel:" +callNumber));
                startActivity(roomtoroom);
            }
        });
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getData(){

       // AppController.getInstance().getRequestQueue().getCache().invalidate(getResources().getString(R.string.ip_address)+DATA_URL, true);
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
       // Log.d("CartUrl",getResources().getString(R.string.ip_address)+DATA_URL);
        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?token="+preferencesClass.getUserToken()+"&+hid="+preferencesClass.getHid(),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                showGrid(response);
            }

            private void showGrid(JSONArray jsonArray) {
                //Looping through all the elements of json array
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        //Log.d("RoomNUmber",""+obj);
                        ROOMNU.add(obj.getString(ROOMNUmber));
                        MOBILENU.add(obj.getString(MOBILENUmber));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> gameKindArray= new ArrayAdapter<String>(RoomtoRoom.this, R.layout.spinner_item, ROOMNU);
                gameKindArray.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(gameKindArray);

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

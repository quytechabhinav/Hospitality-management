package cityscann.com.city_scann.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
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

public class SubmitReview extends AppCompatActivity implements View.OnClickListener {

    PreferencesClass preferencesClass=new PreferencesClass(this);
    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private static String TAG = SubmitReview.class.getSimpleName();
    ProgressDialog pd;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    Button submit;
    EditText reviewtext, reviewhead;
    RatingBar ratingbar;
    String rating, review, sup_id, review_head;
    ImageButton img1, img2, img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_review);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();
        }
    }

    private void defineviewforitem() {
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText("Submit Review");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sup_id = getIntent().getExtras().getString("sup_id");

        Log.d("supplierid",""+ sup_id);
        reviewtext = (EditText)findViewById(R.id.review);
        reviewhead = (EditText) findViewById(R.id.review_head);
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        submit = (Button) findViewById(R.id.submit_rev);
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);
*/
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.submit_rev:
                    if (checkValidation())
                    {
                        downloadList();
                    }
                    break;
                case R.id.img1:
                    Intent home = new Intent(this, Home.class);
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

    private void downloadList() {
        review = reviewtext.getText().toString();
        review_head = reviewhead.getText().toString();
        rating = String.valueOf(ratingbar.getRating());

        String trimrating = rating.substring(0, Math.min(rating.length(), 1));

        pd = new ProgressDialog(this);
        pd.setMessage("Loading.....");
        pd.setCancelable(true);
        pd.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("", "");

        String revh = review_head;
        revh = revh.replace(" ","%20");
        revh = revh.replace("\n","%0A");

        String wrev = review;
        wrev = wrev.replace(" ","%20");
        wrev = wrev.replace("\n","%0A");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, getResources().getString(R.string.ip_address)+"insert_review.php?supplierid="+ sup_id +
                "&email=" + preferencesClass.getuser_email() + "&heading=" + revh +
                "&reviewtext=" + wrev + "&rating=" + trimrating + "&token=" + preferencesClass.getUserToken() + "&cityid=" + preferencesClass.getStateName(),
                new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG, response.toString());
                try {

                    if (response.getString("status").equals("0"))
                    {
                        Toast.makeText(SubmitReview.this, "Unable to Insert", Toast.LENGTH_LONG).show();
                    } else if (response.getString("status").equals("-1"))
                    {
                        Toast.makeText(SubmitReview.this, "Unable to Insert", Toast.LENGTH_LONG).show();
                    }
                    else if (response.getString("status").equals("1"))
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(SubmitReview.this).create();
                        alertDialog.setMessage("Success");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCancelable(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
        Log.d(">>>>>>>>>>>", jsonObjReq.toString());
        
    }

    private boolean checkValidation() {
        rating = String.valueOf(ratingbar.getRating());

        Log.d("rating",""+rating);
        if(rating.equals("0.0")){
            return false;
        }
        return true;
    }
}

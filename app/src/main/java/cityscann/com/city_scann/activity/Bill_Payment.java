package cityscann.com.city_scann.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cityscann.com.city_scann.R;
import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

public class Bill_Payment extends AppCompatActivity {
    protected static final String TAG = Bill_Payment.class.getSimpleName();
    private ImageView mResultImage;
    private ImageView mResultCardTypeImage;
    private int MY_SCAN_REQUEST_CODE =100;
    private TextView mResultLabel,showAmount;
    private EditText amount;
    private String setAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill__payment);

        mResultImage = (ImageView) findViewById(R.id.result_image);
        mResultCardTypeImage = (ImageView) findViewById(R.id.result_card_type_image);
        mResultLabel = (TextView) findViewById(R.id.result);
        showAmount = (TextView)findViewById(R.id.showAmount);
        amount = (EditText)findViewById(R.id.amount);
        amount.setEnabled(false);
        String PayAmount = getIntent().getExtras().getString("Sum");
        amount.setText(PayAmount);



    }


    public void onScanPress (View v){
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onStop() {
        super.onStop();
        showAmount.setText("");
        mResultLabel.setText("");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resultDisplayStr = new String();
        Bitmap cardTypeImage = null;
        if (requestCode == MY_SCAN_REQUEST_CODE) {

            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                Log.v(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";

                Log.d("card_number",resultDisplayStr);
                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );
                CardType cardType = scanResult.getCardType();
                cardTypeImage = cardType.imageBitmap(this);

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
        Bitmap card = CardIOActivity.getCapturedCardImage(data);
        mResultImage.setImageBitmap(card);
        mResultCardTypeImage.setImageBitmap(cardTypeImage);

        Log.i(TAG, "Set result: " + resultDisplayStr);

        mResultLabel.setText(resultDisplayStr);
        setAmount = (amount).getText().toString().trim();
        showAmount.setText("Your amount is: " + setAmount);

    }


}

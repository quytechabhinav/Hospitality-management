package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import cityscann.com.city_scann.R;

public class ShowMenu {

    private final Dialog dialog;
    private Activity act;
    private ImageButton closeButton;

    public ShowMenu(final Activity act) {
        this.act=act;
        dialog =new Dialog(act,R.style.FullHeightDialog);
        dialog.setContentView(R.layout.show_menu);
        closeButton = (ImageButton)dialog.findViewById(R.id.close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}

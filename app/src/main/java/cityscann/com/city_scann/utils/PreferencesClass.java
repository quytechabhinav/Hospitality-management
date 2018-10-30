package cityscann.com.city_scann.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesClass {
    public static final String TAG = PreferencesClass.class.getSimpleName();
    public static final String MyPREFERENCES = "MyPrefs";
    Context context;
    private String userToken;
    private String hid;
    private String useremail;
    private String stateName;
    private String username;
    private String phone;
    private String roomnu;
    private String mobilenu;
    public PreferencesClass(Context context) {
        this.context = context;

    }

    public String getMobilenu() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobilenu = sharedpreferences.getString("mobilenu", null);

        if (mobilenu != null) {
            return mobilenu;
        }
        return null;
    }

    public void setMobilenu(String mobilenu) {
        this.mobilenu = mobilenu;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mobilenu", mobilenu);
        editor.apply();
    }

    public String getRoomNu() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        roomnu = sharedpreferences.getString("roomnu", null);

        if (roomnu != null) {
            return roomnu;
        }
        return null;
    }

    public void setRoomNu(String roomnu) {
        this.roomnu = roomnu;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("roomnu", roomnu);
        editor.apply();
    }




    public String getUserToken() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        userToken = sharedpreferences.getString("userToken", null);

        if (userToken != null) {
            return userToken;
        }
        return null;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("userToken", userToken);
        editor.apply();
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("stateName", stateName);
        editor.apply();
    }

    public String getStateName() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        stateName = sharedpreferences.getString("stateName", null);

        if (stateName != null) {
            return stateName;
        }
        return null;
    }

    public String getuser_email() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        useremail = sharedpreferences.getString("useremail", null);

        if (useremail != null) {
            return useremail;
        }
        return null;
    }

    public void setuser_email(String useremail) {
        this.useremail = useremail;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("useremail", useremail);
        editor.apply();
    }

    public String getHid() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        hid = sharedpreferences.getString("hid", null);

        if (hid != null) {
            return hid;
        }
        return null;
    }

    public void setHid(String hid) {
        this.hid = hid;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("hid", hid);
        editor.apply();
    }

    public String getusername() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", null);

        if (username != null) {
            return username;
        }
        return null;
    }



    public void setusername(String username) {
        this.username = username;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public String getphone() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        phone = sharedpreferences.getString("phone", null);

        if (phone != null) {
            return phone;
        }
        return null;
    }

    public void setphone(String phone) {
        this.phone = phone;
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("phone", phone);
        editor.apply();
    }
}
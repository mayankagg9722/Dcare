package com.example.mayankaggarwal.dcare.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayankaggarwal on 25/02/17.
 */

public class Globals {

    public static String lat;
    public static String lng;
    public static int validatedShift = 0;
    public static String star_Rating = "";
    public static String reason_id = "";
    public static String reason_text = "";
    public static int state_code_return_or_cancel_or_delivered = 9;
    public static String customerCare = "+918076792025";
    public static int orderFetch = 0;

    public static String appVersion = "1.0.0";
    public static String appOS = "Android";

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String errorRes = "";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String getCountryFlag(String countryCode) {
        int flagOffset = 0x1F1E6;
        int asciiOffset = 0x41;

//        String country = "US";

        String country = countryCode;

        int firstChar = Character.codePointAt(country, 0) - asciiOffset + flagOffset;
        int secondChar = Character.codePointAt(country, 1) - asciiOffset + flagOffset;

        String flag = new String(Character.toChars(firstChar))
                + new String(Character.toChars(secondChar));
        return flag;
    }

    public static void showProgressDialog(ProgressDialog dialog, String title, String message) {
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void hideProgressDialog(ProgressDialog dialog) {
        dialog.hide();
    }

    public static void showFailAlert(Activity activity, String title) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setMessage(Globals.errorRes);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog;
        dialog = alert.create();
        dialog.show();
        if (activity.isDestroyed() || activity.isFinishing()) {
            dialog.dismiss();
        }
    }

    public static List<Integer> threeMedia = new ArrayList<>();

//    public static int ORDERSTATE_RECIEVED = 1;
//    public static int ORDERSTATE_ACCEPTED = 2;
    public static int ORDERSTATE_UNASSIGNED = 3;
    public static int ORDERSTATE_ASSIGNED = 4;
    public static int ORDERSTATE_CREW_AKNOLEDGED = 5;
    public static int ORDERSTATE_IN_TRANSIT = 7;
    public static int ORDER_END_STATE = 8;
    public static int ORDERSTATE_END_STATE_DELIVERED = 9;
    public static int ORDERSTATE_END_STATE_CANCELD = 10;
    public static int ORDERSTATE_END_STATE_RETURNED = 11;

    public static String google_address_string = null;
    public static String drop_address_string = null;
    public static String googleLat = null;
    public static String googleLng = null;
    public static String address_id = null;
    public static String place_id = null;


    public static String getDropAddress(JsonObject dropaddress) {

        String house_number = getNullAsEmptyString("house_number", dropaddress);
        String complex_name = getNullAsEmptyString("complex_name", dropaddress);
        String street_name = getNullAsEmptyString("street_name", dropaddress);
        String city = getNullAsEmptyString("city", dropaddress);
        String state = getNullAsEmptyString("state", dropaddress);
        String postal_code = getNullAsEmptyString("postal_code", dropaddress);
        String country_code = getNullAsEmptyString("country_code", dropaddress);
        google_address_string = complex_name + " " + street_name + " " + city + " " + state + " " + postal_code + " " + country_code;
        google_address_string = google_address_string.replace(" ", "+");
        drop_address_string = house_number + " " + complex_name + " " + street_name + " " + city + " " + state + " " + postal_code + " " + country_code;

        return google_address_string;
    }

    public static String getAddressForRoadMap(JsonArray orderTransit) {
        String dropAddress = getDropAddress(orderTransit);
        String pickUpAddress = getPickUpAddress(orderTransit);
        return "origin=" + pickUpAddress + "&destination=" + pickUpAddress + "&waypoints=optimize:true|" + dropAddress;
    }

    public static String getPickUpAddress(JsonArray orderTransit) {
        JsonObject ob = orderTransit.get(0).getAsJsonObject().get("pickup_address").getAsJsonObject();
        if (ob.get("add_location_long").isJsonNull() || ob.get("add_location_lat").isJsonNull()) {
            return getDropAddress(ob).replace(" ", "+");
        } else {
            return ob.get("add_location_lat").getAsString() + "," + ob.get("add_location_long").getAsString();
        }
    }

    public static String getDropAddress(JsonArray orderTransit) {
        String drop = "";
        for (int i = 0; i < orderTransit.size(); i++) {
            JsonObject ob = orderTransit.get(i).getAsJsonObject().get("drop_address").getAsJsonObject();
            if (ob.get("add_location_long").isJsonNull() || ob.get("add_location_lat").isJsonNull()) {
                drop = drop + getDropAddress(ob).replace(" ", "+");
            } else {
                drop = drop + ob.get("add_location_lat").getAsString() + "," + ob.get("add_location_long").getAsString();
            }
            drop = drop + "|";
        }
        return drop.substring(0,drop.length()-1);
    }

    private static String getNullAsEmptyString(String s, JsonObject drop) {
        return drop.get(s).isJsonNull() ? "" : drop.get(s).getAsString();
    }


    public  static PolylineOptions polylineOptions=null;

    public  static Boolean mapView=false;


}

package org.wepush.open_tour.services;


import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;


import org.wepush.open_tour.structures.Constants;
import org.wepush.open_tour.utils.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Antonio on 20/04/2015.
 */
public class LookUpIntentService extends IntentService {
    private int currentLatitude;
    private int currentLongitude;
    private Geocoder gc;
    private Location mLocation;
    private List<Address> list;
    public ResultReceiver mReceiver;

    public LookUpIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        mReceiver=intent.getParcelableExtra(Constants.RECEIVER);


        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);



        List<Address> addresses = null;
//TODO crash if no internet connection is available
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);


            Log.d("miotag","GEOCODING from Location");
            Repository.save(this, Constants.LATITUDE_STARTING_POINT, String.valueOf(location.getLatitude()));
            Repository.save(this, Constants.LONGITUDE_STARTING_POINT, String.valueOf(location.getLongitude()));




        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Unavailable: touch here";
            Log.e("miotag", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = " lat/lon aren't valid values";
            Log.e("miotag", errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Unavailable: touch here";
                Log.e("miotag", errorMessage);
            }
            deliverResultToReceiver(1, errorMessage);
        } else {

            //If address has something in it, is sent to SettingActivity through the receiver.
            //Since I just need the address without code or anything else, i just take the first element of "address"
//            20/07


//            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
//
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//
//            deliverResultToReceiver(Constants.SUCCESS_RESULT,
//                    TextUtils.join(System.getProperty("line.separator"),
//                            addressFragments));
            Address address = addresses.get(0);
            addressFragments.add(address.getAddressLine(0));
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addressFragments.get(0) );

        }
    }
private void deliverResultToReceiver(int resultCode, String message) {

    Bundle bundle = new Bundle();

    bundle.putString(Constants.RESULT_DATA_KEY, message);


    mReceiver.send(resultCode, bundle);
}
}
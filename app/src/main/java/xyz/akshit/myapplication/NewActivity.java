package xyz.akshit.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity implements GPScoordinates{

    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    TextView textView;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        textView = (TextView)findViewById(R.id.textView);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        NewActivity.MY_PERMISSIONS_REQUEST_FINE_LOCATION
                );
            }
        }else {
            xyz.akshit.myapplication.GPSTracker gps = new xyz.akshit.myapplication.GPSTracker(this);

            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                textView.setText("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                //TODO Send coordinates to nearby volunteer
            } else {
                gps.showSettingsAlert();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    GPSTracker gps = new GPSTracker(this);

                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        gps.showSettingsAlert();
                    }

                } else {

                    Log.d("PermissionWanted","WTFast");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void sendCoordinates(double myLat, double myLong) {
        latitude = myLat;
        longitude = myLong;
        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
    }
}

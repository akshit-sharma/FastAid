package xyz.akshit.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class VolunteerActivity extends FragmentActivity implements OnMapReadyCallback, GPScoordinates {


    private GoogleMap mMap;
    double myLat, myLong;
    MarkerOptions current;

    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    private final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void MakeMarker(String name, double myLat, double myLong){
        MarkerOptions doc = new MarkerOptions();
        LatLng myLoc = new LatLng(myLat, myLong);
        doc.position(myLoc).title(name);
        mMap.addMarker(doc);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
    }

    public void PopulateDoctors(){
        MakeMarker("Geet Mittal",28.7974,77.1405);
        MakeMarker("Akshit Sharma",28.64044,77.2005);
        MakeMarker("Pratiksha Aggarwal",28.66974,77.2805);
        MakeMarker("Adish Jain",28.528,77.10805);
        MakeMarker("Garv Jasuja",28.733,77.14405);
        MakeMarker("Akshay Kumar Gupta",28.55574,77.1335);
//        MakeMarker("Vineet Malik",28.70974,77.14005);
//        MakeMarker("Vineet Malik",28.70974,77.14005);
//        MakeMarker("Vineet Malik",28.70974,77.14005);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        current = null;

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION
                );
            }
        }else {
            GPSTracker gps = new GPSTracker(this);

            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                gps.showSettingsAlert();
            }
        }

    }

    @Override
    public void sendCoordinates(double myLat, double myLong) {
        this.myLat = myLat;
        this.myLong = myLong;

        Log.d(TAG, "Lat:"+myLat+" Long"+myLong);

        if(mMap!=null) {
            if(current==null){
                current = new MarkerOptions();
                BackgroundFirstAid bfa = new BackgroundFirstAid();
                bfa.execute();
            }
            mMap.clear();
            PopulateDoctors();
            LatLng myLoc = new LatLng(myLat, myLong);
            current.position(myLoc).title("Me");
            mMap.addMarker(current);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));

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

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void startFirstAid(){
        Intent myIntent = new Intent(this, FirstAidActivity.class);
        startActivity(myIntent);
    }

    class BackgroundFirstAid extends AsyncTask<Void, Void, Void>{

        int i;

        BackgroundFirstAid(){
            i = 0;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startFirstAid();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(i=0;i<3;i++){
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}

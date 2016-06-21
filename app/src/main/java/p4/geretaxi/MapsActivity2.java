package p4.geretaxi;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.RoadsApi;
import com.google.maps.model.SnappedPoint;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private GeoApiContext mContext;

    private static final int PAGE_SIZE_LIMIT = 100;

    private static final int PAGINATION_OVERLAP = 5;

    private ProgressBar mProgressBar;

    List<com.google.maps.model.LatLng>  mCapturedLocations;
    List<SnappedPoint> mSnappedPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));

        mCapturedLocations = (List<com.google.maps.model.LatLng>) getIntent().getSerializableExtra("pontos capturados");

        mTaskSnapToRoads.execute();
    }

    AsyncTask<Void, Void, List<SnappedPoint>> mTaskSnapToRoads =
            new AsyncTask<Void, Void, List<SnappedPoint>>() {
                @Override
                protected void onPreExecute() {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setIndeterminate(true);
                }

                @Override
                protected List<SnappedPoint> doInBackground(Void... params) {
                    try {
                        return snapToRoads(mContext);
                    } catch (final Exception ex) {

                        ex.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(List<SnappedPoint> snappedPoints) {

                    mSnappedPoints = snappedPoints;
                    mProgressBar.setVisibility(View.INVISIBLE);

                    com.google.android.gms.maps.model.LatLng[] mapPoints =
                            new com.google.android.gms.maps.model.LatLng[mSnappedPoints.size()];
                    int i = 0;
                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    for (SnappedPoint point : mSnappedPoints) {
                        mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.location.lat,
                                point.location.lng);
                        bounds.include(mapPoints[i]);
                        i += 1;
                    }

                    mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
                }

            };

    private List<SnappedPoint> snapToRoads(GeoApiContext context) throws Exception {
        List<SnappedPoint> snappedPoints = new ArrayList<>();

        int offset = 0;
        while (offset < mCapturedLocations.size()) {
            // Calculate which points to include in this request. We can't exceed the APIs
            // maximum and we want to ensure some overlap so the API can infer a good location for
            // the first few points in each request.
            if (offset > 0) {
                offset -= PAGINATION_OVERLAP;   // Rewind to include some previous points
            }
            int lowerBound = offset;
            int upperBound = Math.min(offset + PAGE_SIZE_LIMIT, mCapturedLocations.size());

            // Grab the data we need for this page.
            com.google.maps.model.LatLng[] page = mCapturedLocations
                    .subList(lowerBound, upperBound)
                    .toArray(new com.google.maps.model.LatLng[upperBound - lowerBound]);

            // Perform the request. Because we have interpolate=true, we will get extra data points
            // between our originally requested path. To ensure we can concatenate these points, we
            // only start adding once we've hit the first new point (i.e. skip the overlap).
            SnappedPoint[] points = RoadsApi.snapToRoads(context, true, page).await();
            boolean passedOverlap = false;
            for (SnappedPoint point : points) {
                if (offset == 0 || point.originalIndex >= PAGINATION_OVERLAP) {
                    passedOverlap = true;
                }
                if (passedOverlap) {
                    snappedPoints.add(point);
                }
            }

            offset = upperBound;
        }

        return snappedPoints;
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}

package views.maps.mapripples;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.newproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Sudip on 4/12/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private MapWrapperLayout mrl;

    public CustomInfoWindowGoogleMap(Context ctx, MapWrapperLayout mrl) {
        context = ctx;
        this.mrl = mrl;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.marker_info, null);
        MarkerinfoModel markerinfoModel = (MarkerinfoModel) marker.getTag();

/*
        view.findViewById(R.id.call).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(context, "df", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8906424375"));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                context.startActivity(intent);
                return false;
            }
        });
*/
        mrl.setMarkerWithInfoWindow(marker, view);

        return view;
    }
}


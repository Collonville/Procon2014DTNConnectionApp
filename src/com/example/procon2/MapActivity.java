package com.example.procon2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;

public class MapActivity extends Fragment implements OnClickListener {
	private static AnnotationView annotation;
	private static MapView map;
	private static Drawable icon;

	private static DefaultItemizedOverlay poiOverlay;
	private static boolean classInstanceRead = false;

	private static Context context;

	public MapActivity(Context context) {
		MapActivity.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map_activity, container, false);

		map = (MapView) v.findViewById(R.id.map);
		map.getController().setZoom(9);
		map.getController().setCenter(new GeoPoint(35.3917514, 139.44405020)); // 日本標準基準にセット
		map.setBuiltInZoomControls(true);

		annotation = new AnnotationView(map);

		icon = getResources().getDrawable(R.drawable.location);
		poiOverlay = new DefaultItemizedOverlay(icon);

		// add a tap listener for the POI overlay
		poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
			@Override
			public void onTap(GeoPoint pt, MapView mapView) {
				// when tapped, show the annotation for the overlayItem
				int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
				if (lastTouchedIndex > -1) {
					annotation.clearFocus();
					OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
					annotation.showAnnotationView(tapped);

				}
			}
		});
		map.getOverlays().add(poiOverlay);

		if (!DTNMessageCollection.getHash().isEmpty()) {
			for (int i = 0; i < DTNMessageCollection.getHash().size(); i++) {
				addMapPoint(Double.valueOf(DTNMessageCollection.getLatitude()
						.get(i)), Double.valueOf(DTNMessageCollection
						.getLongitude().get(i)), DTNMessageCollection
						.getDeviceName().get(i), DTNMessageCollection.getTime()
						.get(i));
			}
		}

		classInstanceRead = true;

		return v;
	}

	@Override
	public void onClick(View v) {

	}

	public static void addMapPoint(double lati, double longi,
			String deviceName, String dat) {
		if (classInstanceRead) {
			OverlayItem poi = new OverlayItem(new GeoPoint(lati, longi),
					deviceName, dat);
			poiOverlay.addItem(poi);
			map.getOverlays().add(poiOverlay);
		}
	}

}

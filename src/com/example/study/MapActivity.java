package com.example.study;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.example.study.utils.Constants;
import com.example.study.utils.Storage;
import com.example.study.utils.Utilities;

/***
 * 
 * @author Sergey Prilutsky
 * 
 */
public class MapActivity extends Activity {

	private MapView mMapView;
	private ArcGISTiledMapServiceLayer mTileLayer;
	private ArcGISTiledMapServiceLayer mSatLayer;
	private Button mZoomInBtn;
	private Button mZoomOutBtn;
	private Button mSateliteModeBtn;
	private Button mStreetModeBtnb;
	private Button mCurrentLocation;
	public Callout mCallout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		initViews();
		setListener();
		init();
	}

	@Override
	protected void onPause() {
		Storage.getStorage().setmCurrentPoint(mMapView.getCenter());
		Storage.getStorage().setmScale(mMapView.getScale());		
		super.onPause();
		mMapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}

	private void init() {		
		if (Storage.getStorage().getmTopoLayer() != null) {
			mTileLayer = Storage.getStorage().getmTopoLayer();
			mSatLayer = Storage.getStorage().getmSatelliteLayer();
		} else {
			mTileLayer = new ArcGISTiledMapServiceLayer(
					Constants.STREET_MODE_MAP);
			mSatLayer = new ArcGISTiledMapServiceLayer(
					Constants.SATELITE_MODE_MAP);
		}
		
		mMapView.addLayer(mTileLayer);
		mMapView.addLayer(mSatLayer);
		if (Storage.getStorage().getmCurrentLayer() != null) {
			if (Storage.getStorage().getmCurrentLayer().getUrl().equalsIgnoreCase(mTileLayer.getUrl())) {
				mTileLayer.setVisible(true);
				mSatLayer.setVisible(false);
			} else {
				mTileLayer.setVisible(false);
				mSatLayer.setVisible(true);
			}
		} else {
			mTileLayer.setVisible(true);
			mSatLayer.setVisible(false);
		}
		mMapView.invalidate();
		mCallout = mMapView.getCallout();
		mMapView.setOnStatusChangedListener(new StatusChangedListener());
	}

	private void showMyLocation() {
		LocationService locationService = mMapView.getLocationService();
		locationService.setAccuracyCircleOn(true);
		locationService.setAllowNetworkLocation(true);
		locationService.setAutoPan(false);
		locationService.start();

		if (mMapView.isLoaded()) {
			
			mMapView.zoomToResolution(locationService.getPoint(), 1);
			mMapView.invalidate();
		}
		
	}

	private void initViews() {
		mMapView = (MapView) findViewById(R.id.map);
		mSateliteModeBtn = (Button) findViewById(R.id.satelite_btn);
		mStreetModeBtnb = (Button) findViewById(R.id.street_btn);
		mZoomInBtn = (Button) findViewById(R.id.zoom_in);
		mZoomOutBtn = (Button) findViewById(R.id.zoom_out);
		mCurrentLocation = (Button) findViewById(R.id.current_location);
	}

	private void setListener() {
		mSateliteModeBtn.setOnClickListener(new SateliteClickListener());
		mStreetModeBtnb.setOnClickListener(new StreetClickListener());
		mZoomInBtn.setOnClickListener(new ZoomInClickListener());
		mZoomOutBtn.setOnClickListener(new ZoomOuitClickListener());
		mMapView.setOnLongPressListener(new LongPressListener());
		mMapView.setOnSingleTapListener(new SingleTouchListener());
		mCurrentLocation.setOnClickListener(new CurrentLocation());
	}

	private final class StreetClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mTileLayer.setVisible(true);
			mSatLayer.setVisible(false);
			Storage.getStorage().setmCurrentLayer(mTileLayer);
			mMapView.invalidate();						
		}
	}

	private final class SateliteClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mTileLayer.setVisible(false);
			mSatLayer.setVisible(true);
			Storage.getStorage().setmCurrentLayer(mSatLayer);
			mMapView.invalidate();						
		}
	}

	private final class ZoomOuitClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mMapView.zoomout();
			mMapView.invalidate();			
		}
	}

	private final class ZoomInClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mMapView.zoomin();
			mMapView.invalidate();
		}
	}

	private final class SingleTouchListener implements OnSingleTapListener {

		private static final long serialVersionUID = 8930543883030741715L;

		@Override
		public void onSingleTap(float arg0, float arg1) {
			if (mCallout.isShowing()) {
				mCallout.hide();
			}
		}
	}

	private final class CurrentLocation implements OnClickListener {

		@Override
		public void onClick(View v) {
			showMyLocation();			
		}
	}

	private final class LongPressListener implements OnLongPressListener {

		private static final long serialVersionUID = -5403450392897136369L;

		@Override
		public void onLongPress(float arg0, float arg1) {
			Point point = mMapView.toMapPoint(arg0, arg1);
			Utilities.getLongitude(MapActivity.this, mMapView, point);
			LinearLayout view = new LinearLayout(MapActivity.this);
			view.setBackgroundColor(Color.BLACK);
			view.setOrientation(LinearLayout.VERTICAL);
			view.setLayoutParams(new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			String longitudeStr = new StringBuilder(getResources().getString(
					R.string.longitude))
					.append(": ")
					.append(Utilities.getNormalizeLongitude(Utilities
							.getLongitude(MapActivity.this, mMapView, point)
							.getY())).toString();
			String latitudeStr = new StringBuilder(getResources().getString(
					R.string.latitude))
					.append(": ")
					.append(Utilities.getNormalizeLatitude(Utilities
							.getLongitude(MapActivity.this, mMapView, point)
							.getX())).toString();
			view.addView(getTextView(latitudeStr));
			view.addView(getTextView(longitudeStr));
			mCallout.show(point, view);
		}
	}

	private View getTextView(String text) {
		TextView tv = new TextView(MapActivity.this);
		tv.setLayoutParams(new LayoutParams(
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
		tv.setText(text);
		tv.setGravity(Gravity.LEFT);
		tv.setTextColor(Color.WHITE);
		return tv;
	}

	public static Intent getIntent(Context ctx) {
		return new Intent(ctx, MapActivity.class);
	}
	
	private final class StatusChangedListener implements
			OnStatusChangedListener {

		private static final long serialVersionUID = 5951063249663005187L;

		@Override
		public void onStatusChanged(Object arg0, STATUS status) {
			if (status == STATUS.INITIALIZED) {
				if (Storage.getStorage().getmCurrentLayer() != null) {
					if(Storage.getStorage().getmScale()>0){
						mMapView.zoomToResolution(Storage.getStorage().getmCurrentPoint(),
								1);
					}
				} else {
					showMyLocation();
				}
				mMapView.invalidate();
			}
		}
	}
}
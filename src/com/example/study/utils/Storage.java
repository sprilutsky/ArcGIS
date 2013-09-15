package com.example.study.utils;

import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Point;

/***
 * 
 * @author Sergey Prilutsky
 * 
 */

public class Storage {
	
	private static Storage mStorage;
	
	private Storage(){}
	
	public static final Storage getStorage(){
		if(mStorage==null){
			mStorage = new Storage();
		}
		return mStorage;		
	}
	
	private Point mCurrentPoint;
	private double mScale;
	private double mScaleSecondLayer;
	private ArcGISTiledMapServiceLayer mCurrentLayer;
	private ArcGISTiledMapServiceLayer mTopoLayer;
	private ArcGISTiledMapServiceLayer mSatelliteLayer;
	
	
	public ArcGISTiledMapServiceLayer getmCurrentLayer() {
		return mCurrentLayer;
	}

	public void setmCurrentLayer(ArcGISTiledMapServiceLayer mCurrentLayer) {
		this.mCurrentLayer = mCurrentLayer;
	}

	public double getmScale() {
		return mScale;
	}

	public void setmScale(double mScale) {
		this.mScale = mScale;
	}

	public Point getmCurrentPoint() {
		return mCurrentPoint;
	}

	public void setmCurrentPoint(Point mCurrentPoint) {
		this.mCurrentPoint = mCurrentPoint;
	}

	public ArcGISTiledMapServiceLayer getmTopoLayer() {
		return mTopoLayer;
	}

	public void setmTopoLayer(ArcGISTiledMapServiceLayer mTopoLayer) {
		this.mTopoLayer = mTopoLayer;
	}

	public ArcGISTiledMapServiceLayer getmSatelliteLayer() {
		return mSatelliteLayer;
	}

	public void setmSatelliteLayer(ArcGISTiledMapServiceLayer mSatelliteLayer) {
		this.mSatelliteLayer = mSatelliteLayer;
	}
	
	public double getmScaleSecondLayer() {
		return mScaleSecondLayer;
	}

	public void setmScaleSecondLayer(double mScaleSecondLayer) {
		this.mScaleSecondLayer = mScaleSecondLayer;
	}
}
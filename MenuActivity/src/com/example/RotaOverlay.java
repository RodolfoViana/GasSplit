package com.example;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RotaOverlay extends Overlay {
	private Paint paint;
	private List<GeoPoint> geoPoints;
	
	public RotaOverlay(List<GeoPoint> geoPoints) {
		this.geoPoints = geoPoints;
		this.paint = new Paint();
		this.paint.setColor(Color.BLUE);
		this.paint.setStrokeWidth(4);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		Projection proj = mapView.getProjection();
		//Point ponto1, ponto2;
		Point ponto1 = new Point();
		Point ponto2 = new Point();
		for (int i = 0; i < geoPoints.size() - 1; i++) {
			//ponto1 = proj.toPixels(geoPoints.get(i), null);
			//ponto2 = proj.toPixels(geoPoints.get(i+1), null);
			proj.toPixels(geoPoints.get(i), ponto1);
			proj.toPixels(geoPoints.get(i+1), ponto2);
			
			canvas.drawLine(ponto1.x, ponto1.y, ponto2.x, ponto2.y, paint);
		}
	}
}

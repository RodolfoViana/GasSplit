package com.example;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	private ArrayList<GeoPoint> geopointsList;
	private double lat;
	private double lon;
	private boolean rotaCalculada = false;
	String distancia;

	private Context context;

	public AddItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public AddItemizedOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
		// this.lat = 48.85827758964043;
		// this.lon = 2.294543981552124;
		this.lat = -7.230556;
		this.lon = -35.881111;
		this.geopointsList = new ArrayList<GeoPoint>();
	}

	// public AddItemizedOverlay(MotionEvent) {
	//
	// }

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lon;
	}

	public ArrayList<GeoPoint> getGeopointList() {
		return geopointsList;
	}

	public void apagarPontos() {
		 mapOverlays = new ArrayList<OverlayItem>();
	//	geopointsList = new ArrayList<GeoPoint>();
		//this.populate();
		// geopointsList = new ArrayList<GeoPoint>();
	}

//	@Override
//	public boolean onTap(int index) {
//		super.onTap(4);
	//	if(index >= 0 || mapOverlays.size()  < index ){
			
		//	OverlayItem itemClicked = mapOverlays.get(index);
//			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//			dialog.setTitle(itemClicked.getTitle());
//			dialog.setMessage(itemClicked.getSnippet());
//			dialog.show();
	//	}
//		return false;
//		super.on
//		final OverlayItem item = mapOverlays.remove(index);
//		mapOverlays.
//		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//
//		dialog.setTitle("apagar");
//		dialog.setMessage("Marcador removido com sucesso !!!");
//
//		dialog.show();
//		return true;
	//}
	
//	@Override
//	public boolean onTap (final GeoPoint p, final MapView mapView){
//	    boolean tapped = super.onTap(p, mapView);
//	    if (tapped){            
//   
//			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//	    	
//	    			dialog.setTitle("apagar");
//	    			dialog.setMessage("Marcador removido com sucesso !!!");
//	    }           
//	    else{
//	        //do what you want to do when you DON'T hit an item
//	        }                   
//	    return true;
//	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		Local localCasa;

		if (event.getAction() == 1) {
			GeoPoint geopoint = mapView.getProjection().fromPixels(
					(int) event.getX(), (int) event.getY());
			this.lat = geopoint.getLatitudeE6() / 1E6;
			this.lon = geopoint.getLongitudeE6() / 1E6;
			geopointsList.add(geopoint);

			if (geopointsList.size() <= 2) {
				localCasa = new Local(geopoint, Color.RED);
				OverlayItem overlayitem = new OverlayItem(geopoint, "Hello",
						"Sample Overlay item");
				this.addOverlay(overlayitem);
				// mapView.getOverlays().add(localCasa);
			}

			if (geopointsList.size() == 2) {
				// String teste =
				// distancia((geopointsList.get(0).getLatitudeE6()*1.0) * 1E6,
				// (geopointsList.get(1).getLatitudeE6()*1.0) * 1E6,
				// (geopointsList.get(1).getLongitudeE6()*1.0) * 1E6,
				// (geopointsList.get(1).getLongitudeE6()*1.0) *
				// 1E6).toString();

				GeoPoint prev = geopointsList.get(0), current = geopointsList
						.get(1);
				float[] results = new float[3];
				Location.distanceBetween(prev.getLatitudeE6() / 1E6,
						prev.getLongitudeE6() / 1E6,
						current.getLatitudeE6() / 1E6,
						current.getLongitudeE6() / 1E6, results);

				String distancia = Float.toString(results[0]);

				Toast.makeText(context, distancia, Toast.LENGTH_SHORT).show();
			}

		}
		return false;
	}*/
	
	public void removerItem(MapView v){
		for(int i = 0; i < mapOverlays.size();i++){
			//if (super.onTap(mapOverlays.get(i).getPoint(),v)){
				
				mapOverlays.remove(i);
			//	this.populate();
				
				

			//}
			
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		Local localCasa;

		
		if (event.getAction() == 1) {
			GeoPoint geopoint = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

			this.lat = geopoint.getLatitudeE6() / 1E6;
			this.lon = geopoint.getLongitudeE6() / 1E6;
			geopointsList.add(geopoint);
				

			//OverlayItem overlayitem = new OverlayItem(geopoint, "Hello", "Sample Overlay item");
			//this.addOverlay(overlayitem);
			
//			Toast.makeText(context, "Lat: " + lat + ", Lon: " + lon,
//					Toast.LENGTH_SHORT).show();
//			if (geopointsList.size() > 2){
//				localCasa = new Local(geopoint, Color.BLUE);
//				mapView.getOverlays().add(localCasa);
//			
//			} else {
//				localCasa = new Local(geopoint, Color.RED);
//				mapView.getOverlays().add(localCasa);
//			}
			
		
			
			
			if (mapOverlays.size() < 2) {
				OverlayItem overlayitem = new OverlayItem(geopoint, "Hello", "Sample Overlay item");
				this.addOverlay(overlayitem);
				rotaCalculada = false;
			}
			
			if (mapOverlays.size() == 2){
				GeoPoint prev = geopointsList.get(0), current = geopointsList
						.get(1);
				float[] results = new float[3];
				Location.distanceBetween(prev.getLatitudeE6() / 1E6,
						prev.getLongitudeE6() / 1E6,
						current.getLatitudeE6() / 1E6,
						current.getLongitudeE6() / 1E6, results);

				distancia = Float.toString(results[0]);

				Toast.makeText(context, distancia + " metros", Toast.LENGTH_SHORT).show();
				if(!rotaCalculada){
					new RotaAsyncTask(mapView).execute(
							// Latitude, Logintude de Origem
							prev.getLatitudeE6() / 1E6,prev.getLongitudeE6() / 1E6,  
							// Latitude, Longitude de Destino
							current.getLatitudeE6() / 1E6,current.getLongitudeE6() / 1E6);
					rotaCalculada = true;
					
				}

			}
			
			
//			removerItem(mapView);
		}
		return false;
	}
	
	public String getDistancia() {
		return distancia;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

//	@Override
//	protected boolean onTap(int index) {
//		Log.e("Tap", "Tap Performed");
//		return true;
//	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

	// Calculo da distancia
	public static Double distancia(Double lat1, Double lat2, Double lon1,
			Double lon2) {
		double raio = 3958.75;
		double dlat = ToRadians(lat2 - lat1);
		double dlon = ToRadians(lon2 - lon1);

		double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
				+ Math.cos(ToRadians(lat1)) * Math.cos(ToRadians(lat2))
				* Math.sin(dlon / 2) * Math.sin(dlon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = raio * c;

		double meterConversion = 1609.00;
		System.out.println("Teste: " + d * meterConversion);
		return d * meterConversion;
	}

	private static double ToRadians(double degrees) {
		double radians = degrees * 3.1415926535897932385 / 180;
		return radians;
	}

	public void removerOverlay() {
		this.mapOverlays = new ArrayList<OverlayItem>();
		this.populate();
		
	}

}

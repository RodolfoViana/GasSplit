package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GMapsActivity extends MapActivity {

	private MapView mapView;
	private int contMarcador = 0;
	private String combustivelPorKm;
	private Vibrator vibe;
	private AddItemizedOverlay itemizedOverlay;
	TextView txtLatitude;
	TextView txtLongitude;
	String pontosSalvos = "";
	private ArrayList<GeoPoint> geopointsList = new ArrayList<GeoPoint>();
	HashMap<String, GeoPoint> mapa;
	private EditText nome;
//	String nome;
	//MapController mc = mapView.getController();
	
	//GPS
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latitudeGPS,longitudeGPS;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		vibe = (Vibrator) GMapsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

		final Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_launcher);
		
		combustivelPorKm = (String) getIntent().getSerializableExtra("combustivelPorKm");

		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				telaMapa(drawable);
			}
		});
		
		Button salvarPontos = (Button) findViewById(R.id.salvaButton);
		salvarPontos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				geopointsList.addAll(itemizedOverlay.getGeopointList());
				showDialog(0);
//				Toast.makeText(GMapsActivity.this, "Ponto salvo com sucesso",Toast.LENGTH_SHORT).show();
				telaMapa(drawable);
			}
		});
		
		Button infoButton = (Button) findViewById(R.id.infoButton);
		infoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				info();
			}
		});
		
		Button mostarPontosButton = (Button) findViewById(R.id.mostrarPontos);
		mostarPontosButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//mostrarPontos(pontosSalvos);
			}
		});
		
		telaMapa(drawable);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public boolean onKeyDown() {
		return true;
	}
	
	private void info() {
		Intent info = new Intent(this, InformacoesActivity.class);
		info.putExtra("distancia", itemizedOverlay.getDistancia());
		info.putExtra("combustivelPorKm", combustivelPorKm);
		startActivity(info);
	}
	
	private void mostrarPontos(String pontosSalvos) {
		Intent ponto = new Intent(this, PontosActivity.class);
		if (geopointsList.size() != 0){
//			
//			for (int i = 0; i < geopointsList.size(); i++){
//				pontosSalvos += geopointsList.get(i).toString() + "\t";
//			}
			
			Iterator<String> it = mapa.keySet().iterator();
			while(it.hasNext()){
				pontosSalvos += it.next() + "\t";
			}
			
		}
		this.pontosSalvos = pontosSalvos;
		ponto.putExtra("mostrarPontos", pontosSalvos);
		startActivity(ponto);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_mapa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{

		case R.id.normal:

			Toast.makeText(GMapsActivity.this, "Vista Normal ",Toast.LENGTH_SHORT).show();
			if (mapView.isSatellite() == true) {
				mapView.setSatellite(false);
			}
			return true;

		case R.id.satelite:
			Toast.makeText(GMapsActivity.this, "Vista Sat�lite",Toast.LENGTH_SHORT).show();

			if (mapView.isSatellite() == false) {
				mapView.setSatellite(true);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
    protected Dialog onCreateDialog(int id) {

         switch (id) {
         case 0:

              LayoutInflater factory = LayoutInflater.from(this);
              final View textEntryView = factory.inflate(R.layout.addnomeponto,null);
             final EditText x = ((EditText) findViewById(R.id.xxx));
                           return new AlertDialog.Builder(GMapsActivity.this)
                   .setTitle("Digite o Nome do Ponto").setView(textEntryView)
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	
                        	nome = (EditText) textEntryView.findViewById(R.id.xxx);

                        	mapa = new HashMap<String, GeoPoint>();
                        	if(x==null){
                        		 dialog.cancel();
                        		Log.v("edittext","sdfksdkfsdkfj");
                        	}
                        	if(nome.getText().toString().equals("")){
                        		Toast.makeText(GMapsActivity.this, "Nome Invalido",Toast.LENGTH_SHORT).show();
                        	}
                        	else{                        		
                        		mapa.put(nome.getText().toString(), geopointsList.get(0));
                        		Toast.makeText(GMapsActivity.this, "Ponto salvo com sucesso",Toast.LENGTH_SHORT).show();
                        	}
                        	
                   }
              }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                	  dialog.cancel();
                   }
              }).create(); 

         }
         return null;
    }



	public void telaMapa(Drawable drawable) {
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.getOverlays().clear();
		mapView.invalidate();
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true); // Satellite View
		mapView.setStreetView(true); // Street View
		mapView.setTraffic(true); // Traffic View

		//itemizedOverlay = new AddItemizedOverlay(drawable, this);
		itemizedOverlay = new AddItemizedOverlay(drawable, this, vibe);

		List<Overlay> mapOverlays = mapView.getOverlays();

		MapController mc = mapView.getController();
		IniciarServico();
		double lat = itemizedOverlay.getLatitude();
		double lon = itemizedOverlay.getLongitude();
		//GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		GeoPoint geoPoint = new GeoPoint((int) getGPSLatitude(), (int) getGPSLongitude());
		mc.animateTo(geoPoint);
		mc.setZoom(15);
		OverlayItem overlayitem = new OverlayItem(geoPoint,"Hello", "Sample Overlay item");
		itemizedOverlay.addOverlay(overlayitem);
		// mapView.invalidate();
		mapView.setSatellite(true);
		
		
		//GPS
//		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		locationListener = new NovaLocalizacao();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		mapOverlays.add(itemizedOverlay);
		if(contMarcador<3){			
			contMarcador += 1;
		}
	}
	
	 public void IniciarServico()
	    {
	    	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	
	    	LocationListener locationListener = new NovaLocalizacao()  {
	    	    public void onLocationChanged(Location location) {
	    	      Atualizar(location);
	    	    }

	    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    	    public void onProviderEnabled(String provider) {}

	    	    public void onProviderDisabled(String provider) {}
	    	  };

	    	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	    }
	    
	    public void Atualizar(Location location)
	    {
	    	Double latPoint = location.getLatitude();
	    	Double lngPoint = location.getLongitude();
	    	latitudeGPS = location.getLatitude();
	    	longitudeGPS = location.getLongitude();
	    	
//	    	OverlayItem overlayitem = new OverlayItem(new GeoPoint(latPoint.intValue(), lngPoint.intValue()),"Hello", "Sample Overlay item");
//			itemizedOverlay.addOverlay(overlayitem);
	        
	       
	    }
	
	class NovaLocalizacao implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			Log.i("onLocationChaged Rodolfo", "OnLoationChanged GPS");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private double getGPSLatitude(){
		
		return latitudeGPS;
	}
	
	private double getGPSLongitude(){
		
		return longitudeGPS;
	}
	
	
}

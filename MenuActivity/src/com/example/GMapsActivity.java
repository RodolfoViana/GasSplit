package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import android.app.Activity;
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
import android.support.v4.app.FragmentActivity;
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

import com.example.AddItemizedOverlay;
import com.example.GMapsActivity;
import com.example.R;
import com.example.GMapsActivity.NovaLocalizacao;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.google.android.gms.maps.MapFragment;

//public class GMapsActivity extends FragmentActivity {
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
	HashMap<String, GeoPoint> mapa = new HashMap<String, GeoPoint>();;
	private EditText nome;
//	String nome;
	//MapController mc = mapView.getController();
	
	//GPS
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latitudeGPS, longitudeGPS;
	private boolean gpsAtivado = false;
	GeoPoint geoPoint;
	MapController mc;
	
	private GoogleMap map;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		vibe = (Vibrator) GMapsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);		

		final Drawable drawable = this.getResources().getDrawable(
				R.drawable.marker_icon);
		
		combustivelPorKm = (String) getIntent().getSerializableExtra("combustivelPorKm");
		itemizedOverlay = new AddItemizedOverlay(drawable);
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				telaMapa(drawable);
				itemizedOverlay.apagarPontos();
			}
		});
		
		
		Button salvarPontos = (Button) findViewById(R.id.salvaButtonMapa);
		salvarPontos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				geopointsList.addAll(itemizedOverlay.getGeopointList());
				showDialog(0);
				telaMapa(drawable);
			}
		});
		
		telaMapa(drawable);
		
		//Come�o do ponto escolhido na outra tela
		String string = (String) getIntent().getSerializableExtra("pontoEscolhido");
		if (string != null){
		String stringAux = string.split("\t")[string.split("\t").length-1].split(":")[0];
		if (stringAux.equalsIgnoreCase("1")){
			String aux2 = string.split("\t")[string.split("\t").length-1].split(":")[1].split("/")[1];
			Toast.makeText(GMapsActivity.this, aux2,Toast.LENGTH_SHORT).show();
			String a = aux2.split(",")[0]; //Latitude do ponto escolhido
			String b = aux2.split(",")[1]; //Longitude do ponto escolhido
			
			//tentativa fail =\ rodolfo
//			Toast.makeText(GMapsActivity.this, a,Toast.LENGTH_SHORT).show();
			geoPoint = new GeoPoint((int)  Integer.parseInt(a),(int) Integer.parseInt(b));
//
			itemizedOverlay = new AddItemizedOverlay(drawable, this, vibe);
//			
			mapView = (MapView) findViewById(R.id.map_view);
			mapView.getOverlays().clear();
			mapView.invalidate();
			mapView.setBuiltInZoomControls(true);
			mapView.setSatellite(true); // Satellite View
			mapView.setStreetView(true); // Street View
			mapView.setTraffic(true); // Traffic View
//
			List<Overlay> mapOverlays = mapView.getOverlays();
//			mapView.setSatellite(true);
//			
			OverlayItem overlayitem = new OverlayItem(geoPoint, "Hello",
					"Sample Overlay item");
//			itemizedOverlay.addPonto(Integer.parseInt(a), Integer.parseInt(b));
			itemizedOverlay.addGeoPointList(geoPoint);
//			overlayitem.
			itemizedOverlay.addOverlay(overlayitem);
//
			 mapView.getOverlays().add(itemizedOverlay);
//			 mapView.getO
			}
		
		}
		//Fim do ponto escolhido na outra tela
		
		
		Button infoButton = (Button) findViewById(R.id.infoButtonMapa);
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
					mostrarPontos(pontosSalvos);
//					finish();
				
			}
		});
		
		Button btUsarGPS = (Button) findViewById(R.id.btUsarGPS);
		btUsarGPS.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				IniciarServico();
			}
		});
		
//		telaMapa(drawable);
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
		if (geopointsList.size() != 0) {
			String aux, aux2 = "";
			Intent ponto = new Intent(this, PontosActivity.class);
			if (geopointsList.size() != 0) {
				Iterator<String> it = mapa.keySet().iterator();
				while (it.hasNext()) {
					aux = it.next();
					aux2 += aux + "/" + mapa.get(aux) + "\t";
				}

			}
			this.pontosSalvos = aux2;
			ponto.putExtra("mostrarPontos", this.pontosSalvos);
			startActivity(ponto);
			finish();
		} else {
			Toast.makeText(GMapsActivity.this, "N�o existe pontos favoritos",
					Toast.LENGTH_SHORT).show();
		}
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
			if (geopointsList.size() != 0) {
				LayoutInflater factory = LayoutInflater.from(this);
				final View textEntryView = factory.inflate(
						R.layout.addnomeponto, null);
				return new AlertDialog.Builder(GMapsActivity.this)
						.setTitle("Digite o Nome do Ponto")
						.setView(textEntryView)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

										nome = (EditText) textEntryView
												.findViewById(R.id.xxx);
										// mapa = new HashMap<String,
										// GeoPoint>();

										if (nome.getText().toString()
												.equals("")) {
											Toast.makeText(GMapsActivity.this,
													"Nome Invalido",
													Toast.LENGTH_SHORT).show();
										} else {
											mapa.put(nome.getText().toString(),
													geopointsList.get(0));
											Toast.makeText(GMapsActivity.this,
													"Ponto salvo com sucesso",
													Toast.LENGTH_SHORT).show();
										}

									}
								})
						.setNegativeButton("Cancelar",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.cancel();
									}
								}).create();
			} else {
				Toast.makeText(GMapsActivity.this,
						"Nenhum Ponto foi marcado",
						Toast.LENGTH_SHORT).show();
			}

         }
         return null;
    }



	public void telaMapa(Drawable drawable) {
		
		//map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

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
		mapView.setSatellite(true);
		
		
		//GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new NovaLocalizacao();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		mapOverlays.add(itemizedOverlay);
		if(contMarcador<3){			
			contMarcador += 1;
		}
		
	}
	
	public void IniciarServico() {
		
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			
			Toast.makeText(GMapsActivity.this, "GPS nao estar ativado",Toast.LENGTH_SHORT).show();
			buildAlertMessageNoGps();
			
	    }else{
	    	Toast.makeText(GMapsActivity.this, "GSP est� ativado!",Toast.LENGTH_SHORT).show();
	    	Log.i("Verif - LAT:" + getGPSLatitude()+"-"+"LON:" +getGPSLongitude() , "OnLoationChanged GPS");
	    	
	    }
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		locationListener = new NovaLocalizacao();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//				0, locationListener);
//		Log.i("Entrou no iniciar servico", "OnLoationChanged GPS");
		

		locationListener = new NovaLocalizacao() {
			public void onLocationChanged(Location location) {
				Atualizar(location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
	}

	public void Atualizar(Location location) {
		latitudeGPS = location.getLatitude();
		longitudeGPS = location.getLongitude();
		
		geoPoint = new GeoPoint((int) getGPSLatitude(),(int) getGPSLongitude());
		Log.i("LAT:" + geoPoint.getLatitudeE6()+"-"+"LON:" +geoPoint.getLongitudeE6() , "OnLoationChanged GPS");
//		mc.animateTo(geoPoint);
//		mc.setZoom(15);
				
				
		OverlayItem overlayitem = new OverlayItem(geoPoint, "Hello",
				"Sample Overlay item");
		itemizedOverlay.addOverlay(overlayitem);
		itemizedOverlay.addGeoPointList(geoPoint);


		
	}

	class NovaLocalizacao implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			Log.i("MUDOU A LOCALIZACAO", "OnLoationChanged GPS");
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

	private double getGPSLatitude() {

		return latitudeGPS;
	}

	private double getGPSLongitude() {

		return longitudeGPS;
	}
	
	public boolean isGpsAtivado() {
		return gpsAtivado;
	}

	public void setGpsAtivado(boolean gpsAtivado) {
		this.gpsAtivado = gpsAtivado;
	}
	
	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
}

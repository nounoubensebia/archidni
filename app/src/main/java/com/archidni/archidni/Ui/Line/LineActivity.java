package com.archidni.archidni.Ui.Line;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.Model.Transport.TransportUtils;
import com.archidni.archidni.Model.TransportMean;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.StationInsideLineAdapter;
import com.archidni.archidni.Ui.BusTarifsActivity;
import com.archidni.archidni.Ui.LineNotifications.LineNotificationsActivity;
import com.archidni.archidni.Ui.MetroTarifsActivity;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.archidni.archidni.Ui.TelephericTarifsActivity;
import com.archidni.archidni.Ui.TrainTarifsActivity;
import com.archidni.archidni.Ui.TramwayTarifsActivity;
import com.archidni.archidni.UiUtils.ArchidniGoogleMap;
import com.archidni.archidni.UiUtils.ArchidniMap;
import com.archidni.archidni.UiUtils.ViewUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineActivity extends AppCompatActivity implements LineContract.View {

    @BindView(R.id.text_add_to_favorites)
    TextView addToFavoritesText;
    @BindView(R.id.text_report)
    TextView signalDisturbanceText;
    @BindView(R.id.layout_map_loading)
    View mapLoadingLayout;
    @BindView(R.id.layout_map_container)
    View mapContainerLayout;
    MapFragment mapView;
    @BindView(R.id.list_station)
    ListView listView;
    @BindView(R.id.layout_outbound_inbound)
    View outboundInboudLayout;
    @BindView(R.id.layout_inbound)
    View inboundLayout;
    @BindView(R.id.layout_outbound)
    View outboundLayout;
    @BindView(R.id.text_inbound)
    TextView inboundTextView;
    @BindView(R.id.text_outbound)
    TextView outboundTextView;
    @BindView(R.id.text_news_and_notifications)
    TextView newsAndNotificationsText;
    @BindView(R.id.image_arrow1)
    ImageView arrow1Image;
    @BindView(R.id.layout_news_and_notifications)
    View newsAndNotificationsLayout;
    @BindView(R.id.layout_tarifs)
    View tarifsLayout;
    @BindView(R.id.text_tarifs)
    TextView tarifsText;
    @BindView(R.id.image_arrow2)
    ImageView arrow2Image;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private Menu mMenu;

    private boolean scrolled;

    ArchidniGoogleMap archidniMap;
    LineContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Line line = Line.fromJson(getIntent().getExtras().getString(IntentUtils.LINE_LINE));
        presenter = new LinePresenter(line,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        initViews(savedInstanceState);
        presenter.onCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        presenter.loadMenu();
        return super.onCreateOptionsMenu(this.mMenu);
    }

    private void initViews (Bundle bundle)
    {
        ButterKnife.bind(this);
        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.mapView);
        archidniMap = new ArchidniGoogleMap(this, mapView, new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                presenter.onMapReady();
                //archidniMap.moveCamera(new Coordinate(36.773479, 3.052176), 10);
            }
        }, new ArchidniGoogleMap.OnMapLoaded() {
            @Override
            public void onMapLoaded(Coordinate coordinate, LatLngBounds latLngBounds, double zoom) {
                presenter.onMapLoaded();
            }
        });
        addToFavoritesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAddDeleteFromFavoritesClicked();
            }
        });
        signalDisturbanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSignalDisturbanceClicked();
            }
        });
        outboundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onInboundOutboundClicked(true);
            }
        });
        inboundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onInboundOutboundClicked(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //mapView.onSaveInstanceState(outState);
    }

    @Override
    public void showLineOnMap(ArrayList<Coordinate> polyline,ArrayList<Station> stations) {
        archidniMap.clearMap();
        animateCameraToFitLine(polyline);
        archidniMap.preparePolyline(this, polyline
                ,stations.get(0).getTransportMean().getColor(),15);
        for (Station station:stations)
        {
            archidniMap.prepareMarker(station.getCoordinate(),station.getTransportMean()
                    .getMarkerInsideLineDrawable(),0.5f,0.5f);
        }
        archidniMap.addPreparedAnnotations();
    }

    private void animateCameraToFitLine (ArrayList<Coordinate> polyline)
    {
        float padding = ViewUtils.dpToPx(this,8);
        archidniMap.animateCameraToBounds(polyline,(int)padding,1000);
    }

    @Override
    public void showLineOnActivity(final Line line) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(line.getName());
        getSupportActionBar().setElevation(0);
        newsAndNotificationsText.setTextColor(ContextCompat.getColor(this,line.getTransportMean().getColor()));
        arrow1Image.setImageDrawable(ContextCompat.getDrawable(this,line.getTransportMean().getArriwIconDrawableId()));
        newsAndNotificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onShowNewsAndNotificationsClick();
            }
        });
        arrow2Image.setImageDrawable(ContextCompat.getDrawable(this,line.getTransportMean().getArriwIconDrawableId()));
        newsAndNotificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onShowNewsAndNotificationsClick();
            }
        });
        tarifsText.setTextColor(ContextCompat.getColor(this,line.getTransportMean().getColor()));
        tarifsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onTarifsClicked();
            }
        });
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!scrolled)
                {
                    scrollView.scrollTo(0, 0);
                    scrolled = true;
                }
            }
        });
    }

    @Override
    public void setTheme(Line line) {
        setTheme(line.getTransportMean().getTheme());
    }

    @Override
    public void showSelectedStation(Station station) {
        archidniMap.animateCamera(station.getCoordinate(),16,100);
        StationInsideLineAdapter stationInsideLineAdapter = (StationInsideLineAdapter) listView.getAdapter();
        stationInsideLineAdapter.selectStation(station);
        invalidateOptionsMenu();
    }

    @Override
    public void deselectStation(ArrayList<Station> stations,ArrayList<Coordinate> polyline) {
        animateCameraToFitLine(polyline);
        StationInsideLineAdapter stationInsideLineAdapter = (StationInsideLineAdapter) listView.getAdapter();
        stationInsideLineAdapter.selectStation(null);
        invalidateOptionsMenu();
    }

    @Override
    public void inflateTripMenu() {
        inflateMenu();
        MenuItem item = mMenu.findItem(R.id.item_info_station);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_info_station:
                presenter.onStationDetailsClicked();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void inflateStationMenu() {
        inflateMenu();
        MenuItem item = mMenu.findItem(R.id.item_info_station);
        item.setVisible(true);
    }

    @Override
    public void showFeatureNotYetAvailableMessage() {
        Toast.makeText(this,"Cette fonctionnalité n'est pas encore disponible",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteLineFromFavoritesText() {
        addToFavoritesText.setText("Supprimer cette ligne des favoris");
    }

    @Override
    public void showAddedToFavoritesMessage() {
        Toast.makeText(this,"Ligne ajoutée aux favoris",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDeletedFromFavoritesMessage() {
        Toast.makeText(this,"Ligne supprimée des favoris",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAddToFavoritesText() {
        addToFavoritesText.setText("Ajouter aux favoris");
    }

    @Override
    public void showStationsOnList(final ArrayList<Station> stations) {
        StationInsideLineAdapter stationInsideLineAdapter = new StationInsideLineAdapter(this,
                stations,null);
        listView.setAdapter(stationInsideLineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onStationClicked(stations.get(position));
            }
        });
        listView.setDividerHeight(0);
        //listView.setVisibility(View.VISIBLE);
        //ViewUtils.justifyListViewHeightBasedOnChildren(listView);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.VISIBLE);
                ViewUtils.justifyListViewHeightBasedOnChildren(listView);
            }
        },300);
    }

    @SuppressLint("NewApi")
    @Override
    public void updateInboundOutboundLayout(boolean outboundSelected) {
        if (outboundSelected)
        {
            outboundTextView.setTextColor(ContextCompat.getColor(this,R.color.white));
            outboundLayout.setBackground(ContextCompat.getDrawable(this,
                    R.drawable.shape_full_green_rounded_left));
            inboundTextView.setTextColor(ContextCompat.getColor(this,
                    R.color.color_transport_mean_selected_2));
            inboundLayout.setBackground(ContextCompat.getDrawable(this,
                    R.drawable.shape_green_empty_rect_right));
        }
        else
        {
            inboundTextView.setTextColor(ContextCompat.getColor(this,R.color.white));
            inboundLayout.setBackground(ContextCompat.getDrawable(this,
                    R.drawable.shape_full_green_rounded_right));
            outboundTextView.setTextColor(ContextCompat.getColor(this,
                    R.color.color_transport_mean_selected_2));
            outboundLayout.setBackground(ContextCompat.getDrawable(this,
                    R.drawable.shape_green_empty_rect_left));
        }
    }

    @Override
    public void showInboundOutboundLayout() {
        outboundInboudLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMapLoadingLayout() {
        mapLoadingLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapContainerLayout.setVisibility(View.VISIBLE);
            }
        },250);
    }

    @Override
    public void moveCamera(Coordinate coordinate, int zoom) {
        archidniMap.moveCamera(coordinate,zoom);
    }

    @Override
    public void startNewsAndNotificationsActivity(Line line) {
        Intent intent = new Intent(this, LineNotificationsActivity.class);
        intent.putExtra(IntentUtils.LINE_LINE,line.toJson());
        startActivity(intent);
    }

    @Override
    public void startTarifsActivity(Line line) {
        Intent intent = null;
        switch (line.getTransportMean().getId())
        {
            case TransportMean.ID_METRO :
                intent = new Intent(this, MetroTarifsActivity.class);
                break;
            case TransportMean.ID_TRAMWAY :
                intent = new Intent(this, TramwayTarifsActivity.class);
                break;
            case TransportMean.ID_BUS :
                intent = new Intent(this, BusTarifsActivity.class);
                break;
            case TransportMean.ID_TELEPHERIQUE :
                intent = new Intent(this, TelephericTarifsActivity.class);
                break;
            case TransportMean.ID_TRAIN :
                intent = new Intent(this, TrainTarifsActivity.class);
                break;

        }
        if (intent!=null)
            startActivity(intent);
    }


    private void inflateMenu ()
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trip, mMenu);
    }

    @Override
    public void startStationActivity(Station station) {
        Intent intent = new Intent(this, StationActivity.class);
        intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
        startActivity(intent);
    }
}

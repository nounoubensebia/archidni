package com.archidni.archidni.Ui.Search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Coordinate;
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.PlaceSuggestionsAdapter;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
import com.archidni.archidni.Ui.SetLocation.SetLocationActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    SearchContract.Presenter presenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.editText)
    EditText searchText;
    @BindView(R.id.progressBar_suggestions)
    ProgressBar suggestionsProgressBar;
    @BindView(R.id.progressBar_placeDetails)
    ProgressBar placeDetailsProgressBar;
    @BindView(R.id.layout_search)
    View searchLayout;
    @BindView(R.id.text_reset)
    TextView resetText;
    @BindView(R.id.image_close)
    View closeImage;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        Bundle extras = getIntent().getExtras();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (extras.containsKey(IntentUtils.LOCATION)) {
            presenter = new SearchPresenter(this,
                    extras.getInt(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE),
                    new Gson().fromJson(extras.getString(IntentUtils.LOCATION),
                            Place.class));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        }
        else
        {
            presenter = new SearchPresenter(this,
                    extras.getInt(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE));
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                presenter.onUserLocationCaptured(new Coordinate(location.getLatitude(),
                        location.getLongitude()));
            }
        });
    }

    @Override
    protected void onStop() {
        presenter.onStop(getApplicationContext());
        super.onStop();
    }

    private void initViews()
    {
        ButterKnife.bind(this);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchText.getText().toString();
                if (text.length()>0)
                {
                    presenter.loadSearchResults(getApplicationContext(),text);
                    resetText.setVisibility(View.VISIBLE);
                }
                else
                {
                    presenter.loadSearchResults(getApplicationContext(),text);
                    resetText.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                hideKeyboard();
            }
        });
        resetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IntentUtils.RESULT_OK)
        {
            Coordinate coordinate = Coordinate.fromJson(data.getExtras().
                    getString(IntentUtils.SET_LOCATION_COORDINATES));
            presenter.onSetMarkerResult(coordinate);
        }
    }

    private void populateListView (ArrayList<PlaceSuggestion> placeSuggestions)
    {
        PlaceSuggestionsAdapter placeSuggestionsAdapter = new PlaceSuggestionsAdapter(placeSuggestions,
                new PlaceSuggestionsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(PlaceSuggestion placeSuggestion) {
                        hideKeyboard();
                        presenter.loadPlaceDetails(getApplicationContext(),placeSuggestion);
                    }
                },this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placeSuggestionsAdapter);
    }


    @Override
    public void showSearchResults(ArrayList<PlaceSuggestion> placeSuggestions) {
        suggestionsProgressBar.setVisibility(View.GONE);
        populateListView(placeSuggestions);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this,R.string.error_retry,Toast.LENGTH_LONG).show();
        suggestionsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPlaceDetailsLoadingBar() {
        placeDetailsProgressBar.setVisibility(View.VISIBLE);
        searchLayout.setVisibility(View.GONE);
    }

    @Override
    public void startAskingActivity(int requestType,Place place) {
        Intent intent = new Intent();
        intent.putExtra(IntentUtils.LOCATION,place.toJson());
        setResult(1,intent);
        finish();
    }

    @Override
    public void startSetLocationActivity() {
        startActivityForResult(new Intent(this,SetLocationActivity.class),
                0);
    }

    @Override
    public void startPathSearchActivity(Place origin, Place destination) {
        Intent intent = new Intent(this,PathSearchActivity.class);
        if (origin!=null)
            intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,origin.toJson());
        else
            intent.putExtra(IntentUtils.PATH_SEARCH_ORIGIN,new Gson().toJson(null));
        intent.putExtra(IntentUtils.PATH_SEARCH_DESTINATION,destination.toJson());
        startActivity(intent);
        finish();
    }

    @Override
    public void showPlacesSearchLoadingBar() {
        suggestionsProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showHintMessage(String message) {
        searchText.setHint(message);
    }

    @Override
    public void showMyPositionErrorMsg() {
        Toast.makeText(this,"Impossible de trouver la position de l'utilisateur." +
                        " Peut-être que vous n'avez pas activer les services de localisation GPS. Veuillez activer les services de localisation GPS et relancer l'application.",
                Toast.LENGTH_LONG).show();
    }

    private void hideKeyboard ()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().
                        getWindowToken(),
                0);
    }
}

package com.archidni.archidni.Ui.Search;

import android.app.Activity;
import android.content.Intent;
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
import com.archidni.archidni.Model.Place;
import com.archidni.archidni.Model.PlaceSuggestion.PlaceSuggestion;
import com.archidni.archidni.Model.PlaceSuggestion.TextQuerySuggestion;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.PlaceSuggestionsAdapter;
import com.archidni.archidni.Ui.PathSearch.PathSearchActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(IntentUtils.LOCATION))
        {
            presenter = new SearchPresenter(this,
                    extras.getInt(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE),
                    new Gson().fromJson(extras.getString(IntentUtils.LOCATION),
                            Place.class));
        }
        else
        {
            presenter = new SearchPresenter(this,
                    extras.getInt(IntentUtils.SearchIntents.EXTRA_REQUEST_TYPE));
        }
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
                if (text.length()>1)
                {
                    presenter.loadSearchResults(text);
                    resetText.setVisibility(View.VISIBLE);
                }
                else
                {
                    presenter.loadSearchResults(text);
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

    private void populateListView (ArrayList<PlaceSuggestion> placeSuggestions)
    {
        PlaceSuggestionsAdapter placeSuggestionsAdapter = new PlaceSuggestionsAdapter(placeSuggestions,
                new PlaceSuggestionsAdapter.OnItemClickedListener() {
                    @Override
                    public void onItemClick(PlaceSuggestion placeSuggestion) {
                        hideKeyboard();
                        presenter.loadPlaceDetails(placeSuggestion);
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
    public void startPathSearchActivity(Place origin, Place destination) {
        Intent intent = new Intent(this,PathSearchActivity.class);
        if (origin!=null)
            intent.putExtra(IntentUtils.ORIGIN,origin.toJson());
        else
            intent.putExtra(IntentUtils.ORIGIN,new Gson().toJson(null));
        intent.putExtra(IntentUtils.DESTINATION,destination.toJson());
        startActivity(intent);
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

    private void hideKeyboard ()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().
                        getWindowToken(),
                0);
    }
}

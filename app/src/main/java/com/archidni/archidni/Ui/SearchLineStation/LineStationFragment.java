package com.archidni.archidni.Ui.SearchLineStation;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.archidni.archidni.Data.LineStationSuggestions.LineStationSuggestionsRepository;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Search.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nouno on 15/02/2018.
 */

public abstract class LineStationFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.text_no_results)
    TextView noResultsText;
    @BindView(R.id.layout_error)
    View errorLayout;
    @BindView(R.id.button_retry)
    Button retryButton;
    LineStationSuggestionsRepository lineStationSuggestionsRepository;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lineStationSuggestionsRepository = new LineStationSuggestionsRepository();

        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);
        ButterKnife.bind(this,rootView);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                hideKeyboard();
            }
        });
        return rootView;
    }

    private void hideKeyboard ()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().
                        getWindowToken(),
                0);
    }
    abstract void updateQueryText (String text);
}

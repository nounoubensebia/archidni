package com.archidni.archidni.Ui.SearchLineStation;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.archidni.archidni.Data.LineStationSuggestions.LineStationSuggestionsRepository;
import com.archidni.archidni.Data.LinesAndPlaces.LinesAndPlacesRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineStationSuggestionAdapter;
import com.archidni.archidni.Ui.Line.LineActivity;
import com.archidni.archidni.UiUtils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by nouno on 15/02/2018.
 */

public class LineFragment extends LineStationFragment {

    LinesAndPlacesRepository linesAndPlacesRepository;
    boolean emptyString = true;


    @Override
    void updateQueryText(final String text) {
        if (text.length()>1)
        {
            searchLines(text);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchLines(text);
                }
            });
        }
        else
        {
            if (linesAndPlacesRepository!=null)
                linesAndPlacesRepository.cancelAllRequests(getActivity());
            populateList(new ArrayList<LineStationSuggestion>());
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            emptyString = true;
        }
    }

    private void searchLines(String text)
    {
        emptyString = false;
        noResultsText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        lineStationSuggestionsRepository.getLineSuggestions(getActivity(), text, new LineStationSuggestionsRepository.OnSearchComplete() {
            @Override
            public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                if (!emptyString)
                {
                    errorLayout.setVisibility(View.GONE);
                    populateList(lineStationSuggestions);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (lineStationSuggestions.size()==0)
                        noResultsText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError() {
                if (!emptyString)
                {
                    errorLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    populateList(new ArrayList<LineStationSuggestion>());
                }
            }
        });
    }

    private void populateList (ArrayList<LineStationSuggestion> lineStationSuggestions)
    {
        LineStationSuggestionAdapter stationSuggestionAdapter = new LineStationSuggestionAdapter(
                getActivity(), lineStationSuggestions, new LineStationSuggestionAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(LineStationSuggestion lineSuggestion) {
                final Dialog dialog = DialogUtils.buildProgressDialog("Veuillez patientez",getActivity());
                dialog.show();
                if (linesAndPlacesRepository == null)
                {
                    linesAndPlacesRepository = new LinesAndPlacesRepository();
                }
                linesAndPlacesRepository.getLine(getActivity(), lineSuggestion,
                        new LinesAndPlacesRepository.OnLineSearchCompleted() {
                    @Override
                    public void onLineFound(Line line) {
                        dialog.hide();
                        Intent intent = new Intent(getActivity(), LineActivity.class);
                        intent.putExtra(IntentUtils.LINE_LINE,line.toJson());
                        startActivity(intent);
                    }

                    @Override
                    public void onError() {
                        dialog.hide();
                        Toast.makeText(getActivity(),R.string.error_happened,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity()
                .getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(stationSuggestionAdapter);
    }
}

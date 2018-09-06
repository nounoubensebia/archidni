package com.archidni.archidni.Ui.SearchLineStation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.archidni.archidni.Data.LineStationSuggestions.LineStationSuggestionsRepository;
import com.archidni.archidni.Data.Station.StationDataRepository;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.LineStationSuggestion;
import com.archidni.archidni.Model.Transport.Station;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineStationSuggestionAdapter;
import com.archidni.archidni.Ui.Station.StationActivity;
import com.archidni.archidni.UiUtils.DialogUtils;

import java.util.ArrayList;

/**
 * Created by nouno on 15/02/2018.
 */

public class StationFragment extends LineStationFragment {
    StationDataRepository stationDataRepository;
    boolean emptyString = true;

    @Override
    void updateQueryText(final String text) {
        if (text.length()>1)
        {
            emptyString = false;
            noResultsText.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            lineStationSuggestionsRepository.getStationSuggestions(getActivity(), text, new LineStationSuggestionsRepository.OnSearchComplete() {
                @Override
                public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                    if (!emptyString)
                    {
                        populateList(lineStationSuggestions);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (lineStationSuggestions.size()==0)
                            noResultsText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError() {
                    Toast.makeText(getActivity(), R.string.error_happened,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }
        else
        {
            if (stationDataRepository!=null)
                stationDataRepository.cancelAllRequests(getActivity());
            populateList(new ArrayList<LineStationSuggestion>());
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            emptyString = true;
        }
    }

    private void populateList (ArrayList<LineStationSuggestion> lineStationSuggestions)
    {
        LineStationSuggestionAdapter stationSuggestionAdapter = new LineStationSuggestionAdapter(
                getActivity(), lineStationSuggestions, new LineStationSuggestionAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(LineStationSuggestion lineSuggestion) {
                final Dialog dialog = DialogUtils.buildProgressDialog("Veuillez patientez",
                        getActivity());
                dialog.show();
                stationDataRepository = new StationDataRepository();
                stationDataRepository.getStation(getActivity(), lineSuggestion,
                        new StationDataRepository.OnSearchComplete() {
                    @Override
                    public void onSearchComplete(Station station) {
                        dialog.hide();
                        Intent intent = new Intent(getActivity(), StationActivity.class);
                        intent.putExtra(IntentUtils.STATION_STATION,station.toJson());
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

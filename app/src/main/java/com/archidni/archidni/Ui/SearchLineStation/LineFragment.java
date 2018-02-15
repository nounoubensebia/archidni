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
import com.archidni.archidni.Data.Lines.LinesOnlineDataStore;
import com.archidni.archidni.Data.Lines.LinesRepository;
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

    LinesRepository linesRepository;

    @Override
    void updateQueryText(String text) {
        if (text.length()>1)
        {
            noResultsText.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            lineStationSuggestionsRepository.getLineSuggestions(getActivity(), text, new LineStationSuggestionsRepository.OnSearchComplete() {
                @Override
                public void onSearchComplete(ArrayList<LineStationSuggestion> lineStationSuggestions) {
                    populateList(lineStationSuggestions);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (lineStationSuggestions.size()==0)
                        noResultsText.setVisibility(View.VISIBLE);
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
            populateList(new ArrayList<LineStationSuggestion>());
        }
    }

    private void populateList (ArrayList<LineStationSuggestion> lineStationSuggestions)
    {
        LineStationSuggestionAdapter stationSuggestionAdapter = new LineStationSuggestionAdapter(
                getActivity(), lineStationSuggestions, new LineStationSuggestionAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(LineStationSuggestion lineSuggestion) {
                final Dialog dialog = DialogUtils.buildProgressDialog("Veuillez patientez",getActivity());
                dialog.show();
                if (linesRepository == null)
                {
                    linesRepository = new LinesRepository();
                }
                linesRepository.getLine(getActivity(), lineSuggestion,
                        new LinesRepository.OnLineSearchCompleted() {
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

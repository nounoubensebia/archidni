package com.archidni.archidni.Ui.Favorites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.archidni.archidni.Data.Favorites.FavoritesRepository;
import com.archidni.archidni.Data.SharedPrefsUtils;
import com.archidni.archidni.IntentUtils;
import com.archidni.archidni.Model.Favorites;
import com.archidni.archidni.Model.Transport.Line;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineAdapter;
import com.archidni.archidni.Ui.Line.LineActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.text_no_favorites)
    TextView noFavoritesText;
    FavoritesRepository favoritesRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        favoritesRepository = new FavoritesRepository();
        Favorites favorites = favoritesRepository.getFavorites(this);
        if (favorites!=null)
        {
            if (favorites.getLines().size()==0)
            {
                noFavoritesText.setVisibility(View.VISIBLE);
            }
            else
            {
                LineAdapter lineAdapter = new LineAdapter(this, favorites.getLines(),
                        new LineAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Line line) {
                                startLineActivity(line);
                            }
                        });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(lineAdapter);
            }
        }
        else
        {
            noFavoritesText.setVisibility(View.VISIBLE);
        }
    }

    private void startLineActivity(Line line)
    {
        Intent intent = new Intent(this, LineActivity.class);
        intent.putExtra(IntentUtils.LINE_LINE,line.toJson());
        startActivity(intent);
    }
}

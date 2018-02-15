package com.archidni.archidni.Ui.SearchLineStation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.archidni.archidni.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchLineStationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.editText)
    TextView editText;

    LineFragment linesFragment;
    StationFragment stationsFragment;


    PgAdapter mPgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_line_station);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);
        mPgAdapter =
                new PgAdapter(
                        getSupportFragmentManager());
        viewPager.setAdapter(mPgAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                linesFragment.updateQueryText(editable.toString());
                stationsFragment.updateQueryText(editable.toString());
            }
        });
    }

    public class PgAdapter extends FragmentPagerAdapter {

        public PgAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0)
            {
                linesFragment = new LineFragment();
                return linesFragment;
            }
            else
            {
                stationsFragment = new StationFragment();
                return stationsFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)
            {
                return "LIGNES";
            }
            else
            {
                return "STATIONS";
            }
        }
    }





}

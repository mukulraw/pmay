package com.ddf.pmay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class Beneficiary extends Fragment {

    TabLayout tabs;
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beneficiary, container, false);

        tabs = view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);

        tabs.addTab(tabs.newTab().setText("Pending Visit"));
        tabs.addTab(tabs.newTab().setText("Visited"));

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabs.setupWithViewPager(pager);

        Objects.requireNonNull(tabs.getTabAt(0)).setText("Pending Visit");
        Objects.requireNonNull(tabs.getTabAt(1)).setText("Visited");

        setTabBG(R.drawable.left_select, R.drawable.right_unselect);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    setTabBG(R.drawable.left_select, R.drawable.right_unselect);
                } else {
                    setTabBG(R.drawable.left_unselect, R.drawable.right_select);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {


        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0)
            {
                return new PendingVisit();
            }
            else if (i == 1)
            {
                return new Visited();
            }
            else
            {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void setTabBG(int tab1, int tab2) {
        ViewGroup tabStrip = (ViewGroup) tabs.getChildAt(0);
        View tabView1 = tabStrip.getChildAt(0);
        View tabView2 = tabStrip.getChildAt(1);
        if (tabView1 != null) {
            int paddingStart = tabView1.getPaddingStart();
            int paddingTop = tabView1.getPaddingTop();
            int paddingEnd = tabView1.getPaddingEnd();
            int paddingBottom = tabView1.getPaddingBottom();
            ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), tab1));
            ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
        }

        if (tabView2 != null) {
            int paddingStart = tabView2.getPaddingStart();
            int paddingTop = tabView2.getPaddingTop();
            int paddingEnd = tabView2.getPaddingEnd();
            int paddingBottom = tabView2.getPaddingBottom();
            ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), tab2));
            ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
        }
    }

}

package com.mejdaoui.servicesdemaintenance.Fragement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mejdaoui.servicesdemaintenance.Adapter.TabAdapter;
import com.mejdaoui.servicesdemaintenance.R;

public class MainFragmentTab extends Fragment {

    private TabAdapter adapter;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);

        viewPager = view.findViewById(R.id.request_orders_view_pager);
        tableLayout = view.findViewById(R.id.request_orders_tabs);

        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new FonctionnaireRecycler(), "Offres");
        adapter.addFragment(new Fonct_fav(), "Pour vous");

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        return view;
    }
}

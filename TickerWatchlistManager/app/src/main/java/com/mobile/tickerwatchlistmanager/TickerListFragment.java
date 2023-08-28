package com.mobile.tickerwatchlistmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile.tickerwatchlistmanager.model.Ticker;

import java.util.LinkedList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TickerListFragment extends Fragment {

    ListView listView;
    TickerViewModel model;

    public TickerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = new ViewModelProvider(getActivity()).get(TickerViewModel.class);
        model.getTickers().observe(getViewLifecycleOwner(), new Observer<LinkedList<Ticker>>() {
            @Override
            public void onChanged(LinkedList<Ticker> tickers) {
                ArrayAdapter<Ticker> adapter =  new ArrayAdapter<Ticker>(getActivity(), android.R.layout.simple_list_item_1, model.getTickers().getValue());
                listView.setAdapter(adapter);
            }
        });

//        if (((MainActivity) getActivity()).ticker != null && !((MainActivity) getActivity()).ticker.equals("")) {
//            model.setTickerData(((MainActivity) getActivity())).ticker);
//        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model.setClicked(i);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentRoot = inflater.inflate(R.layout.fragment_ticker_list, container, false);

        listView = fragmentRoot.findViewById(R.id.ticker_list);

        return fragmentRoot;
    }
}
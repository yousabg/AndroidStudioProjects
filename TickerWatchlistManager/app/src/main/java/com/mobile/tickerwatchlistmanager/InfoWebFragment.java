package com.mobile.tickerwatchlistmanager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import com.mobile.tickerwatchlistmanager.model.Ticker;

import java.util.LinkedList;

public class InfoWebFragment extends Fragment {
    WebView webView;
    WebSettings websettings;
    TickerViewModel model;

    public InfoWebFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_web, container, false);

        model = new ViewModelProvider(requireActivity()).get(TickerViewModel.class);

        webView = view.findViewById(R.id.webView1);
        webView.loadUrl("https://seekingalpha.com");

        websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());



//        if (tickers.size() == 6) {
//            webView.loadUrl("https://seekingalpha.com/symbol/" + tickers.get(5));
//        } else if (tickers.size() == 5) {
//            webView.loadUrl("https://seekincgalpha.com/symbol/" + tickers.get(4));
//        } else if (tickers.size() == 4) {
//            webView.loadUrl("https://seekingalpha.com/symbol/" + tickers.get(3));
//        }


        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(TickerViewModel.class);


            model.getClicked().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer clicked) {
                    if (model.getClicked() != null) {
                        LinkedList<Ticker> tickers = model.getTickers().getValue();
                        int pos = model.getClicked().getValue();
                        webView.loadUrl("https://seekingalpha.com/symbol/" + tickers.get(pos));
                    }
                }
            });


    }


}
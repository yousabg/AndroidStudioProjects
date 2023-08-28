package com.mobile.tickerwatchlistmanager;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.tickerwatchlistmanager.model.Ticker;

import java.util.LinkedList;

public class TickerViewModel extends ViewModel {

    MutableLiveData<LinkedList<Ticker>> tickers;
    private MutableLiveData<Ticker> tickerData;
    LinkedList<Ticker> history = new LinkedList<>();
    MutableLiveData<Integer> clicked;

    public LiveData<LinkedList<Ticker>> getTickers() {
        if (tickers == null) {
            tickers = new MutableLiveData<>();
            loadTickers();
        }
        return tickers;
    }

    private void loadTickers() {
        LinkedList<Ticker> lTickers = new LinkedList<Ticker>();
        lTickers.add(new Ticker("BAC"));
        lTickers.add(new Ticker("AAPL"));
        lTickers.add(new Ticker("DIS"));
//        for (int i = 0; i < history.size(); i++) {
//            if (history.get(i) != null) {
//                lTickers.add(history.get(i));
//            }
//        }

        tickers.setValue(lTickers);
    }

    public void setTickerData(String name) {

        if (tickerData == null) {
            tickerData = new MutableLiveData<>();
            loadTickers();
        }
        Ticker t = new Ticker(name);
        LinkedList<Ticker> newList = tickers.getValue();
        this.tickerData.setValue(t);
        if (newList.size() == 6) {
            newList.set(5,t);
            history.add(t);
        } else {
            newList.add(t);
            history.add(t);
        }


        tickers.setValue(newList);
        setClicked(tickers.getValue().size()-1);
    }


    public MutableLiveData<Integer> getClicked() {
        if (clicked == null) {
            clicked = new MutableLiveData<>();
        }
        return  clicked;
    }

    public void setClicked(int pos) {
        if (clicked == null) {
            clicked = new MutableLiveData<>();
        }
            clicked.setValue(pos);
        Log.i("TES", "" + pos);
        }
    }






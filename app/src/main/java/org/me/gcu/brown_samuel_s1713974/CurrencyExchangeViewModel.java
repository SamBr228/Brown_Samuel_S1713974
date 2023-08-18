//Samuel Brown - S1713974
package org.me.gcu.brown_samuel_s1713974;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class CurrencyExchangeViewModel extends AndroidViewModel {
    private final CurrencyExchangeRepository repository;
    private final MutableLiveData<ArrayList<CurrencyExchangeRate>> currencyExchangeRates = new MutableLiveData<>();

    public CurrencyExchangeViewModel(@NonNull Application application) {
        super(application);
        repository = new CurrencyExchangeRepository();
    }

    public LiveData<ArrayList<CurrencyExchangeRate>> getCurrencyExchangeRates() {
        return currencyExchangeRates;
    }

    public void fetchCurrencyExchangeRates() {
        new Thread(() -> {
            ArrayList<CurrencyExchangeRate> data = repository.fetchCurrencyExchangeRates();
            currencyExchangeRates.postValue(data);
        }).start();
    }
}

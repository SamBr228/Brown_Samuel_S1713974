//Samuel Brown - S1713974

package org.me.gcu.brown_samuel_s1713974;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class MainCurrenciesFragment extends Fragment {

    private CurrencyExchangeRepository currencyExchangeRepository;

    public static MainCurrenciesFragment newInstance() {
        return new MainCurrenciesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyExchangeRepository = new CurrencyExchangeRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_currencies, container, false);
        View backgroundView = view.findViewById(R.id.background_view_main_currencies);
        View cardView = view.findViewById(R.id.card_view_main_currencies);


        backgroundView.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        cardView.setOnClickListener(v -> {});


        populateData(view);

        return view;
    }

    private void populateData(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                double usdExchangeRate = currencyExchangeRepository.getExchangeRateForCurrency("USD");
                double eurExchangeRate = currencyExchangeRepository.getExchangeRateForCurrency("EUR");
                double jpyExchangeRate = currencyExchangeRepository.getExchangeRateForCurrency("JPY");


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setExchangeRateRow(view, R.id.usd_row, "USD", usdExchangeRate);
                        setExchangeRateRow(view, R.id.eur_row, "EUR", eurExchangeRate);
                        setExchangeRateRow(view, R.id.jpy_row, "JPY", jpyExchangeRate);
                    }
                });
            }
        }).start();
    }

    private void setExchangeRateRow(View view, int rowId, String targetCurrencyCode, double exchangeRate) {
        View rowView = view.findViewById(rowId);
        if (rowView == null) {
            Log.e("MainCurrenciesFragment", "Row view not found for ID: " + rowId);
            return;
        }

        ImageView sourceFlag = rowView.findViewById(R.id.source_flag_image_view);
        if (sourceFlag == null) {
            Log.e("MainCurrenciesFragment", "Source flag ImageView not found in row view.");
            return;
        }
        sourceFlag.setImageResource(R.drawable.gb);

        // Set the target flag
        ImageView targetFlag = rowView.findViewById(R.id.target_flag_image_view);
        int targetResourceId = getResources().getIdentifier(targetCurrencyCode.toLowerCase().substring(0, 2), "drawable", requireContext().getPackageName());
        targetFlag.setImageResource(targetResourceId);

        // Set up the EditTexts
        EditText sourceAmount = rowView.findViewById(R.id.et_source_currency);
        EditText targetAmount = rowView.findViewById(R.id.et_target_currency);

        TextView viewSourceCurrencyCode = rowView.findViewById(R.id.source_currency_code);
        viewSourceCurrencyCode.setText("GBP");

        TextView viewTargetCurrencyCode = rowView.findViewById(R.id.target_currency_code);
        viewTargetCurrencyCode.setText(targetCurrencyCode);

        boolean[] isUpdating = {false, false}; // Two flags for each pair of EditTexts

        sourceAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating[0]) {
                    isUpdating[0] = true;
                    try {
                        double sourceValue = Double.parseDouble(s.toString());
                        double targetValue = sourceValue * exchangeRate;
                        targetAmount.setText(String.format(Locale.getDefault(), "%.2f", targetValue));
                    } catch (NumberFormatException e) {
                    }
                    isUpdating[0] = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        targetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!isUpdating[1]) {
                    isUpdating[1] = true;
                    try {
                        double targetValue = Double.parseDouble(s.toString());
                        double sourceValue = targetValue / exchangeRate;
                        sourceAmount.setText(String.format(Locale.getDefault(), "%.2f", sourceValue));
                    } catch (NumberFormatException e) {
                    }
                    isUpdating[1] = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
}

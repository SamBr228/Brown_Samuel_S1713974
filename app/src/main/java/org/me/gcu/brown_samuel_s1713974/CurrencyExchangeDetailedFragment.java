//Samuel Brown - S1713974

package org.me.gcu.brown_samuel_s1713974;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CurrencyExchangeDetailedFragment extends Fragment {

    private CurrencyExchangeRate currencyExchangeRate;
    private TextView tvCountryCodesDetailed, tvTargetCurrencyName, tvExchangeRate, tvDateDetailed;
    private EditText etSourceAmount, etTargetAmount;
    private ImageView sourceFlagImageView, targetFlagImageView;

    public static CurrencyExchangeDetailedFragment newInstance(CurrencyExchangeRate currencyExchangeRate) {
        CurrencyExchangeDetailedFragment fragment = new CurrencyExchangeDetailedFragment();
        Bundle args = new Bundle();
        args.putSerializable("currencyExchangeRate", currencyExchangeRate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currencyExchangeRate = (CurrencyExchangeRate) getArguments().getSerializable("currencyExchangeRate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_currency_exchange_detailed, container, false);
        View backgroundView = view.findViewById(R.id.background_view);
        View cardView = view.findViewById(R.id.card_view);

        // Clicking on the background will close the fragment
        backgroundView.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // Clicking on the card will do nothing (this will consume the click events)
        cardView.setOnClickListener(v -> {});

        initializeViews(view);
        populateData();
        setUpListeners();
        return view;
    }


    private void initializeViews(View view) {
        tvCountryCodesDetailed = view.findViewById(R.id.tv_currency_codes);
        tvTargetCurrencyName = view.findViewById(R.id.tv_currency_name);
        tvExchangeRate = view.findViewById(R.id.tv_exchange_rate);
        tvDateDetailed = view.findViewById(R.id.tv_date);
        etSourceAmount = view.findViewById(R.id.et_source_currency);
        etTargetAmount = view.findViewById(R.id.et_target_currency);
        sourceFlagImageView = view.findViewById(R.id.sourceFlagImageView);
        targetFlagImageView = view.findViewById(R.id.targetFlagImageView);
    }

    private void populateData() {
        String countries = currencyExchangeRate.getSourceCurrencyCode() + " → " + currencyExchangeRate.getTargetCurrencyCode();
        tvCountryCodesDetailed.setText(countries);

        String targetCurrencyName = getCurrencyFullName(currencyExchangeRate.getTargetCurrencyCode());
        tvTargetCurrencyName.setText(targetCurrencyName);

        String exchangeRate = String.valueOf(currencyExchangeRate.getExchangeRate());
        tvExchangeRate.setText(exchangeRate); // Set the exchange rate text
        tvDateDetailed.setText(currencyExchangeRate.getPubDate());

        int sourceResourceId = getResources().getIdentifier(currencyExchangeRate.getSourceCurrencyCode().substring(0, 2).toLowerCase(), "drawable", requireContext().getPackageName());
        sourceFlagImageView.setImageResource(sourceResourceId);

        int targetResourceId = getResources().getIdentifier(currencyExchangeRate.getTargetCurrencyCode().substring(0, 2).toLowerCase(), "drawable", requireContext().getPackageName());
        targetFlagImageView.setImageResource(targetResourceId);
    }

    private boolean isUpdating = false;

    private void setUpListeners() {
        etSourceAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;
                try {
                    isUpdating = true;
                    double sourceAmount = Double.parseDouble(s.toString());
                    double targetAmount = sourceAmount * currencyExchangeRate.getExchangeRate();
                    etTargetAmount.setText(String.valueOf(targetAmount));
                } catch (NumberFormatException e) {
                    // Handle invalid input
                } finally {
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etTargetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;
                try {
                    isUpdating = true;
                    double targetAmount = Double.parseDouble(s.toString());
                    double sourceAmount = targetAmount / currencyExchangeRate.getExchangeRate();
                    etSourceAmount.setText(String.valueOf(sourceAmount));
                } catch (NumberFormatException e) {
                    // Handle invalid input
                } finally {
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }


    private String getCurrencyFullName(String currencyCode) {
        // Return the full name of the currency based on its code.
        // You can add your logic here to map the currency code to its full name.
        // For now, returning the currency code as a placeholder.
        return currencyCode;
    }
}

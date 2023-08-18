//Samuel Brown - S1713974
package org.me.gcu.brown_samuel_s1713974;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrencyExchangeMainActivity extends AppCompatActivity {
    private CurrencyExchangeViewModel viewModel;
    private RecyclerView recyclerView;
    private FrameLayout fragmentContainer;

    private Handler updateHandler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            viewModel.fetchCurrencyExchangeRates();
            updateHandler.postDelayed(this, 600000); // 10 minutes
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        Button btnMainCurrencies = findViewById(R.id.btn_main_currencies);
        fragmentContainer = findViewById(R.id.fragment_container);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CurrencyExchangeViewModel.class);

        updateHandler.post(updateRunnable); // Start the auto-update

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
        dividerItemDecoration.setDrawable(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Observe changes in ViewModel
        viewModel.getCurrencyExchangeRates().observe(this, currencyExchangeRates -> {
            CurrencyExchangeRecyclerAdapter adapter = new CurrencyExchangeRecyclerAdapter(this, currencyExchangeRates, currencyExchangeRate -> {
                // Open detailed fragment
                CurrencyExchangeDetailedFragment fragment = CurrencyExchangeDetailedFragment.newInstance(currencyExchangeRate);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

                // Make the fragment container visible
                fragmentContainer.setVisibility(View.VISIBLE);
            });
            recyclerView.setAdapter(adapter);
        });

        // Handle button click
        btnMainCurrencies.setOnClickListener(v -> {
            MainCurrenciesFragment fragment = MainCurrenciesFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            // Make the fragment container visible
            fragmentContainer.setVisibility(View.VISIBLE);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacks(updateRunnable); // Stop the auto-update
    }

    @Override
    public void onBackPressed() {
        if (fragmentContainer.getVisibility() == View.VISIBLE) {
            // Hide the fragment container
            fragmentContainer.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

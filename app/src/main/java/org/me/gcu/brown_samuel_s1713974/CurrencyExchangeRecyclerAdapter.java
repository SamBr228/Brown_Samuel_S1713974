//Samuel Brown - S1713974
package org.me.gcu.brown_samuel_s1713974;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyExchangeRecyclerAdapter extends RecyclerView.Adapter<CurrencyExchangeRecyclerAdapter.ViewHolder> {
    private List<CurrencyExchangeRate> currencyExchangeRateList;
    private OnItemClickListener onItemClickListener;
    private Context context;


    // Constructor
    public CurrencyExchangeRecyclerAdapter(Context context, List<CurrencyExchangeRate> currencyExchangeRates, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.currencyExchangeRateList = currencyExchangeRates;
        this.onItemClickListener = onItemClickListener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountryCodes, tvExchangeRate, tvDate;
        ImageView sourceFlagImageView;
        ImageView targetFlagImageView;
        View colorTagView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCountryCodes = itemView.findViewById(R.id.tv_country_codes);
            tvExchangeRate = itemView.findViewById(R.id.tv_exchange_rate);
            sourceFlagImageView = itemView.findViewById(R.id.sourceFlagImageView);
            targetFlagImageView = itemView.findViewById(R.id.targetFlagImageView);
            colorTagView = itemView.findViewById(R.id.colorTagView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency_exchange, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("FlagDebug", "onBindViewHolder called for position: " + position);
        CurrencyExchangeRate currencyExchangeRate = currencyExchangeRateList.get(position);

        // Extract two-letter country codes from currency codes
        String sourceTwoLetterCode = currencyExchangeRate.getSourceCurrencyCode().substring(0, 2).toLowerCase();
        String targetTwoLetterCode = currencyExchangeRate.getTargetCurrencyCode().substring(0, 2).toLowerCase();

        String countries = currencyExchangeRate.getSourceCurrencyCode() + " → " + currencyExchangeRate.getTargetCurrencyCode();
        holder.tvCountryCodes.setText(countries);

        String exchangeRate = String.valueOf(currencyExchangeRate.getExchangeRate());
        holder.tvExchangeRate.setText(exchangeRate);



        // Set source country flag
        int sourceResourceId = context.getResources().getIdentifier(sourceTwoLetterCode, "drawable", context.getPackageName());
        holder.sourceFlagImageView.setImageResource(sourceResourceId);


        // Set target country flag
        int targetResourceId = context.getResources().getIdentifier(targetTwoLetterCode, "drawable", context.getPackageName());
        holder.targetFlagImageView.setImageResource(targetResourceId);

        double rate = currencyExchangeRate.getExchangeRate();
        int backgroundColor;
        if (rate < 1) {
            backgroundColor = Color.parseColor("#FF00FF00"); // Very Strong
        } else if (rate < 2) {
            backgroundColor = Color.parseColor("#FF90EE90"); // Strong
        } else if (rate < 7.5) {
            backgroundColor = Color.parseColor("#FFFFFF00"); // Average
        } else if (rate < 15) {
            backgroundColor = Color.parseColor("#FFFFA500"); // Weak
        } else {
            backgroundColor = Color.parseColor("#FFFF0000"); // Very Weak
        }
        holder.colorTagView.setBackgroundColor(backgroundColor);


        // Set up the click listener
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(currencyExchangeRate));
    }

    @Override
    public int getItemCount() {
        return currencyExchangeRateList.size();
    }

    public void updateData(List<CurrencyExchangeRate> newCurrencyExchangeRateList) {
        currencyExchangeRateList = newCurrencyExchangeRateList;
        notifyDataSetChanged();
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(CurrencyExchangeRate currencyExchangeRate);
    }
}

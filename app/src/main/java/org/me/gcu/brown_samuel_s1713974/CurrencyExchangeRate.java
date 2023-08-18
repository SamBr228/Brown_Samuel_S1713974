//Samuel Brown - S1713974

package org.me.gcu.brown_samuel_s1713974;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeRate implements Serializable{
    private String title;
    private String link;
    private String pubDate;
    private String description;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private String targetCountryName;
    private String currencyName;
    private double exchangeRate;



    public CurrencyExchangeRate(String title, String link, String pubDate, String description, String sourceCurrencyCode, String targetCurrencyCode, Double exchangeRate) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.sourceCurrencyCode = sourceCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.exchangeRate = exchangeRate;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }
    public void setTargetCurrencyCode(String targetCurrencyCode){
        this.targetCurrencyCode = targetCurrencyCode;
    }
    public void setExchangeRate(Double exchangeRate){
        this.exchangeRate = exchangeRate;
    }


}


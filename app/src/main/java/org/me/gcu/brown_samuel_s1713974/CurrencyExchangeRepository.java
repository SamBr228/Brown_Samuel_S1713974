//Samuel Brown - S1713974
package org.me.gcu.brown_samuel_s1713974;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyExchangeRepository {
    private static final String urlSource = "https://www.fx-exchange.com/gbp/rss.xml";

    public ArrayList<CurrencyExchangeRate> fetchCurrencyExchangeRates() {
        ArrayList<CurrencyExchangeRate> currencyExchangeRateList = new ArrayList<>();
        try {
            URL url = new URL(urlSource);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            String xmlStreamString = content.toString();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPP = factory.newPullParser();
            xmlPP.setInput(new StringReader(xmlStreamString));

            String title = "";
            String link = "";
            String pubDate = "";
            String description = "";
            String sourceCountryCode = "";
            String targetCountryCode = "";
            Double exchangeRate = 0.0;

            boolean insideItem = false;

            int eventType = xmlPP.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = xmlPP.getName();
                    if (tagName.equals("item")) {
                        insideItem = true;
                        title = "";
                        link = "";
                        pubDate = "";
                        description = "";
                        sourceCountryCode = "";
                        targetCountryCode = "";
                        exchangeRate = 0.0;
                    } else if (insideItem) {
                        if (tagName.equals("title")) {
                            title = xmlPP.nextText();
                            try {
                                sourceCountryCode = title.split("\\(")[1].split("\\)")[0];
                                targetCountryCode = title.split("\\(")[2].split("\\)")[0];
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        } else if (tagName.equals("link")) {
                            link = xmlPP.nextText();
                        } else if (tagName.equals("pubDate")) {
                            pubDate = xmlPP.nextText();
                        } else if (tagName.equals("description")) {
                            description = xmlPP.nextText();
                            exchangeRate = Double.parseDouble(description.split("=")[1].trim().split(" ")[0]);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xmlPP.getName().equals("item")) {
                        insideItem = false;
                        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate(title, link, pubDate, description, sourceCountryCode, targetCountryCode, exchangeRate);
                        currencyExchangeRateList.add(currencyExchangeRate);
                    }
                }
                eventType = xmlPP.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return currencyExchangeRateList;
    }

    public double getExchangeRateForCurrency(String targetCurrencyCode) {
        for (CurrencyExchangeRate rate : fetchCurrencyExchangeRates()) {
            if (rate.getTargetCurrencyCode().equals(targetCurrencyCode)) {
                return rate.getExchangeRate();
            }
        }
        return 0.0;
    }

}

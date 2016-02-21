package com.stocksanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author BogdanRakov
 */

public class YahooDataLoader {

    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;

    public YahooDataLoader() {

    }
    // YYYY-MM-DD is the format!!! 2015-11-17
    public static Stock getData(String name, String symbol, String startDate, String endDate, String period) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        String baseURL = "http://real-chart.finance.yahoo.com/table.csv?";
        String queryText = BuildHistoricalDataRequest(symbol, start, end, period);
        String url = String.format("%s%s", baseURL, queryText);

        System.out.println(url);

        String csvLine;
        ArrayList<Double> pricesList = new ArrayList<>();

        try(InputStreamReader is =
                    new InputStreamReader(new URL(url).openConnection().getInputStream());
            BufferedReader br = new BufferedReader(is);) {

            br.readLine(); //skip the header

            while ((csvLine = br.readLine()) != null) {
                String[] stockInfo = csvLine.split(",");
                pricesList.add(Double.parseDouble(stockInfo[4]));
            }

            System.out.println(pricesList.size());

        } catch(IOException ex) {
            System.out.println(ex);
        }

        double[] prices = new double[pricesList.size()];
        for(int i = 0; i < pricesList.size(); i++) prices[i] = pricesList.get(i);
        return new Stock(name, symbol, prices);
    }

    private static String BuildHistoricalDataRequest(String symbol, LocalDate startDate, LocalDate endDate, String period) {
        // period is d = daily, m = monlthy, y = yearly
        // We're subtracting 1 from the month because yahoo
        // counts the months from 0 to 11 not from 1 to 12.
        StringBuilder request = new StringBuilder();
        request.append(String.format("s=%s&", symbol));
        request.append(String.format("a=%d&", startDate.getMonthValue()-1));
        request.append(String.format("b=%d&", startDate.getDayOfMonth()));
        request.append(String.format("c=%d&", startDate.getYear()));
        request.append(String.format("d=%d&", endDate.getMonthValue()-1));
        request.append(String.format("e=%d&", endDate.getDayOfMonth()));
        request.append(String.format("f=%d&", endDate.getYear()));
        request.append(String.format("g=%s&", period));

        return request.toString();
    }

    /*
    public static List<String> getStockSymbol(String query) {

        String url = "https://s.yimg.com/aq/autoc?query="+query+"&region=US&lang=en-US&callback=YAHOO.util.UHScriptNodeDataSource.callbacks";

        String HTMLLine = "";
        String[] stocks;

        System.out.println(url);
        try(InputStreamReader is =
                    new InputStreamReader(new URL(url).openConnection().getInputStream());
            BufferedReader br = new BufferedReader(is);) {

            HTMLLine = br.readLine();

        } catch(IOException ex) {
            System.out.println(ex);
        }

        stocks = new String[]{HTMLLine};
        //HTMLLine.split(",");
        List<String> results = new ArrayList<>(Arrays.asList(stocks));


        return results;

    }
    */

    public static void main(String[] args) {
        Stock yahoo = YahooDataLoader.getData("yahoo", "YHOO", "2015-11-17", "2016-01-17", "d");
        System.out.println(yahoo.getPrices());
        Stock google = YahooDataLoader.getData("google", "GOOG", "2015-11-17", "2016-01-17", "d");
        System.out.println(google.getPrices());
        Stock pg = YahooDataLoader.getData("PG", "PG", "2015-11-17", "2016-01-17", "d");
        System.out.println(pg.getPrices());
        Stock sp500 = YahooDataLoader.getData("s&p500", "^GSPC", "2015-11-17", "2016-01-17", "d");
        System.out.println(sp500.getPrices());
        /*
        for(String s : getStockSymbol("procter")) {
            System.out.println(s);
        }
        */
    }
}
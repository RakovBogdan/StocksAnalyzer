package com.stocksanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author BogdanRakov
 */

public class YahooDataLoader {

    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;

    public YahooDataLoader(int startDay, int startMonth, int startYear,
                           int endDay, int endMonth, int endYear, String frequency) {
        this.startDate = LocalDate.of(startYear,startMonth,startDay);
        this.endDate = LocalDate.of(endYear,endMonth,endDay);
        this.frequency = frequency;
    }

    public Stock getData(String name, String symbol) {

        String baseURL = "http://real-chart.finance.yahoo.com/table.csv?";
        String queryText = BuildHistoricalDataRequest(symbol, startDate, endDate, frequency);
        String url = String.format("%s%s", baseURL, queryText);

        System.out.println(url);

        String csvLine;
        ArrayList<Double> pricesList = new ArrayList<>();

        try(InputStreamReader is =
                    new InputStreamReader(new URL(url).openConnection().getInputStream());
            BufferedReader br = new BufferedReader(is)) {

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
        YahooDataLoader dataLoader = new YahooDataLoader(17,11,2015,17,1,2016, "d");
        Stock yahoo = dataLoader.getData("yahoo", "YHOO");
        System.out.println(Arrays.toString(yahoo.getPrices()));
        Stock google = dataLoader.getData("google", "GOOG");
        System.out.println(Arrays.toString(google.getPrices()));
        Stock pg = dataLoader.getData("PG", "PG");
        System.out.println(Arrays.toString(pg.getPrices()));
        Stock sp500 = dataLoader.getData("s&p500", "^GSPC");
        System.out.println(Arrays.toString(sp500.getPrices()));
        /*
        for(String s : getStockSymbol("procter")) {
            System.out.println(s);
        }
        */
    }
}
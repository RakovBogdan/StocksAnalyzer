package com.stocksanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author BogdanRakov
 */

public class YahooDataLoader {

    private LocalDate startDate;
    private LocalDate endDate;
    private DataFrequency dataFrequency;

    public YahooDataLoader(int startDay, int startMonth, int startYear,
                           int endDay, int endMonth, int endYear, DataFrequency dataFrequency) {
        this.startDate = LocalDate.of(startYear,startMonth,startDay);
        this.endDate = LocalDate.of(endYear,endMonth,endDay);
        this.dataFrequency = dataFrequency;
    }

    public Stock getData(String name, String symbol) {

        String baseURL = "http://real-chart.finance.yahoo.com/table.csv?";
        String queryText = BuildHistoricalDataRequest(symbol, startDate, endDate, dataFrequency.getCodeForYahooDataLoader());
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
                pricesList.add(Double.parseDouble(stockInfo[6]));
            }

        } catch(IOException ex) {
            System.out.println(ex);
        }

        Collections.reverse(pricesList);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public DataFrequency getDataFrequency() {
        return dataFrequency;
    }

    public void setDataFrequency(DataFrequency dataFrequency) {
        this.dataFrequency = dataFrequency;
    }

}
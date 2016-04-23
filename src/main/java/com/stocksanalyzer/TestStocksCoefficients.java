package com.stocksanalyzer;

import java.util.Arrays;

/**
 * Created by Патука on 24.03.2016.
 */
public class TestStocksCoefficients {
    public static void main(String[] args) {
        StocksAnalyzer analyzer = new StocksAnalyzer();

        double[] pricesMCD = {96.90, 97.41, 97.16, 97.00, 96.37,
                95.13, 95.00, 96.19, 97.30, 97.54};
        Stock mcDonalds = new Stock("McDonalds", "MCD", pricesMCD);
        analyzer.addStock(mcDonalds);

        double[] pricesKO = {22.28, 22.47, 22.39, 22.42, 22.27,
                22.43, 22.58, 22.86, 22.55, 22.56};
        Stock coca_cola = new Stock("Coca-Cola", "KO", pricesKO);
        analyzer.addStock(coca_cola);

        double[] pricesPG = {37.25, 37.36, 37.32, 37.27, 37.04,
                36.54, 36.56, 37.23, 37.53, 37.56};
        Stock pg = new Stock("P&G", "PG", pricesPG);
        analyzer.addStock(pg);

        analyzer.calculateStocksCoefficients(true);
        analyzer.getStocks().stream().forEach(stock -> {
            System.out.println(stock.getName());
            System.out.println(stock.getStatistics());
            System.out.println("--------------------");
        });

        analyzer.createMarkovitzPortfolio();
        analyzer.getMarkovitzPortfolio().minimumRisk();
        System.out.println("Mimimum Risk Portfolio:");
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getAnnualProfitPercantage() + "%");
        System.out.println("--------------------");

        System.out.println("Portfolio with given minimum profit:");
        analyzer.getMarkovitzPortfolio().minimizeRisk(0.13);
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getAnnualProfitPercantage() + "%");
        System.out.println("--------------------");

        System.out.println("Maximum Profit Portfolio:");
        analyzer.getMarkovitzPortfolio().maximumProfit();
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk());
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getAnnualProfitPercantage());
        System.out.println("--------------------");

        System.out.println("Portfolio with given maximum risk:");
        analyzer.getMarkovitzPortfolio().maximizeProfit(0.1419);
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk());
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getAnnualProfitPercantage());
        System.out.println("--------------------");
    }
}

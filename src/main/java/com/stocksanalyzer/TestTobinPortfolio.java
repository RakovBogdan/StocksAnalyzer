package com.stocksanalyzer;

import java.util.Arrays;

/**
 * Created by RakovBogdan.
 */
public class TestTobinPortfolio {
    public static void main(String[] args) {
        StocksAnalyzer analyzer = new StocksAnalyzer();
        analyzer.setYahooDataLoader(17, 11, 2015, 17, 1, 2016, "m");
        //Creation of new stock and adding it to analyzer:
        //Prices array
        double[] pricesGazprom = {139.20, 135.50, 128.77,141.70, 148.96, 132.00, 131.95, 137.90,
                141.50, 142.86, 130.31, 143.82, 152.95};
        //Creation of new Stock, with inputs of its name, symbol and price array
        Stock gazProm = new Stock("gazProm", "GZPM", pricesGazprom);
        //Add the stock to analyzer
        analyzer.addStock(gazProm);

        //Doing the same for another 2 Stocks:
        double[] pricesGMKNikel = {5980.00,5865.00, 6405.00, 6656.00, 6719.00, 7060.00,7230.00,
                7320.00, 8033.00, 8820.00, 8080.00, 11610.00, 11182.00};
        Stock gMKNikel = new Stock("GMKNorNikel", "GMKNN", pricesGMKNikel);
        analyzer.addStock(gMKNikel);

        double[] pricesMMK = {6.244, 5.621, 5.823, 6.703, 6.693, 6.78, 7.347, 7.802,
                9.417, 12.481, 10.821, 13.134, 14.6};
        Stock mmk = new Stock("MMK", "MMK", pricesMMK);
        analyzer.addStock(mmk);

        analyzer.calculateStocksCoefficients(true);

        analyzer.createTobinPortfolio();
        analyzer.setNonRiskSecurity(15);

        analyzer.getTobinPortfolio().minimumRisk();
        System.out.println(analyzer.getTobinPortfolio());

        analyzer.getTobinPortfolio().minimizeRisk(48);
        System.out.println(analyzer.getTobinPortfolio());

    }
}

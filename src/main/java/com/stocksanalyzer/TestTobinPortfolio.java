package com.stocksanalyzer;

import java.util.Arrays;

/**
 * Created by RakovBogdan.
 */
public class TestTobinPortfolio {
    public static void main(String[] args) {
        StocksAnalyzer analyzer = new StocksAnalyzer();
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

        double[] pricesMechel = {39.90, 38.40, 37.50, 47.50, 52.40, 38.50,32.90, 24.60,
                21.59, 22.60, 24.71, 44.85, 82.27};
        Stock mechel = new Stock("Mechel", "MCHL", pricesMechel);
        analyzer.addStock(mechel);

        analyzer.calculateStocksCoefficients(true);

        analyzer.createMarkovitzPortfolio();
        analyzer.getMarkovitzPortfolio().minimizeRisk(0.04d);
        //TODO: U will need to display portfolio, also its risk and profit rates
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk());
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getProfit());
    }
}

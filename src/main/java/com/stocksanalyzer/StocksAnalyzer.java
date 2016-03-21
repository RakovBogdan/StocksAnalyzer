package com.stocksanalyzer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by BogdanRakov
 */
public class StocksAnalyzer {

    private List<Stock> allStocks = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;
    private MarkovitzPortfolio markovitzPortfolio = new MarkovitzPortfolio(allStocks);
    private Map<Stock, Double> Markovitz = new HashMap<> (); // ArrayList всіх просортованих акцій

    public StocksAnalyzer(String startDate, String endDate, String frequency) {
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.frequency = frequency;
    }

    /*
     * Calculates and sets allStocks normProfits and coefficients
     */
    public void calculateStocksCoefficients() {
        allStocks.stream().forEach(stock -> {
            MathStatistics stats = new MathStatistics(stock.getPrices());
            stats.evaluateStatistics();
            stock.setStatistics(stats);
        });
    }


    public void addStock(Stock stock) {
        allStocks.add(stock);
    }

    public List<Stock> getStocks() {
        return allStocks;
    }

    public MarkovitzPortfolio getMarkovitzPortfolio() {
        return markovitzPortfolio;
    }

    public void createMarkovitzPortfolio(List<Stock> allStocks) {
        this.markovitzPortfolio = new MarkovitzPortfolio(allStocks);
    }


    public static void main(String[] args) {
        //Each HTTP session should create new StockAnalyzer
        StocksAnalyzer analyzer = new StocksAnalyzer("2015-11-17", "2015-12-17", "d");

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

        //This is the first part. Every Stock will get its MathStatistics object calculated
        analyzer.calculateStocksCoefficients();
        //After calculations, u can get coefficients of mean and skewness for the first Stock like this:
        System.out.println("mean: " + analyzer.allStocks.get(0).getStatistics().getMean());
        System.out.println("skewness" + analyzer.allStocks.get(0).getStatistics().getSkewness());
        System.out.println();
        //TODO: U will need to display all coefficients from every Stock in allStocks

        //This is 2nd part. There are 2 kinds of portfolio: Markovitz and Tobin
        //Markovitz portfolio with given minimum profit level of 4%, where me minimize risk level:
        analyzer.createMarkovitzPortfolio(analyzer.allStocks);
        analyzer.getMarkovitzPortfolio().minimizeRisk(0.04d);
        //TODO: U will need to display portfolio, also its risk and profit rates
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk());
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getProfit());
        /*
        //Портфель Марковіца із заданим рівнем максимального ризику 10% (ми максимызуэмо прибутки)
        analyzer.getMarkovitzPortfolio().maximizeProfit(0.1d);
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        */
    }
}

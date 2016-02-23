package com.stocksanalyzer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BogdanRakov
 */
public class StocksAnalyzer {

    private List<Stock> allStocks = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;

    public StocksAnalyzer(String startDate, String endDate, String frequency) {
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.frequency = frequency;
    }



    private List<Stock> sortedStocks = new ArrayList<> (); // ArrayList всіх просортованих акцій

    private List<Double> indexPrices = new ArrayList<>(); // фондові індекси S&P 500(чи інше), ВВОДИМО З КЛАВІАТУРИ
    private List<Double> normProfitIndexes = new ArrayList<>(); //Норми прибутку для індексу S&P 500

    private double beta; // коефіцієнт бета, для 3 лаби потрібний (чутливість прибутковості цінного паперу до змін прибутковості ринку в цілому)
    private double ObligationUSA; //ВВОДИТЬСЯ З КЛАВІАТУРИ ставка по державних облігаціях США (можна іншу країну)
    private double marketIndex; // ВВОДИТЬСЯ З КЛАВІАУТРИ фондовий індекс S&P 500(чи інше)

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


    public List<Double> getIndexPrices() {
        return indexPrices;
    }

    public void setIndexPrices(List<Double> indexPrices) {
        this.indexPrices = indexPrices;
    }

    public double getObligationUSA() {
        return ObligationUSA;
    }

    public void setObligationUSA(double obligationUSA) {
        ObligationUSA = obligationUSA;
    }

    public double getMarketIndex() {
        return marketIndex;
    }

    public void setMarketIndex(double marketIndex) {
        this.marketIndex = marketIndex;
    }


    public static void main(String[] args) {
        StocksAnalyzer analyzer = new StocksAnalyzer("2015-11-17", "2015-12-17", "d");

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

        analyzer.calculateStocksCoefficients();
        analyzer.allStocks.stream().forEach(stock -> {
            System.out.println(stock.getName());
            System.out.println(stock.getStatistics());
            System.out.println("--------------------");
        });

    }
}

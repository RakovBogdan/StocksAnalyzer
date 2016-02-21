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

            stock.setNormProfit(MathStatistics.calculateNormProfit(stock.getPrices()));
            stock.setExpectedValue(MathStatistics.mean(stock.getNormProfit()));
            stock.setStandardDeviation(MathStatistics.calculateStandardDeviation(
                    stock.getNormProfit(), stock.getExpectedValue()));
            stock.setVariationCoefficient(MathStatistics.calculateVarianceCoefficient(
                    stock.getStandardDeviation(), stock.getExpectedValue()));
            double skewness = MathStatistics.calculateSkewness(
                    stock.getNormProfit(), stock.getStandardDeviation(), stock.getExpectedValue());
            stock.setNormSkewness(MathStatistics.calculateNormSkewness(stock.getNormProfit(), skewness));
            double kurtosis = MathStatistics.calculateKurtosis(
                    stock.getNormProfit(), stock.getStandardDeviation(), stock.getExpectedValue());
            stock.setNormKurtosis(MathStatistics.calculateNormKurtosis(stock.getNormProfit(), kurtosis));
            stock.setStandardSemiVariance(MathStatistics.calculateStandardSemiVariance(
                    stock.getNormProfit(), stock.getExpectedValue()));
            stock.setSemiVariationCoefficient(MathStatistics.calculateSemiVariationCoefficient(
                    stock.getNormProfit(), stock.getStandardSemiVariance(), stock.getExpectedValue()));

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
        double[] prices = {96.90, 97.41, 97.16, 97.00, 96.37,
                95.13, 95.00, 96.19, 97.30, 97.54};
        Stock mcDonalds = new Stock("McDonalds", "MCD", prices);
        analyzer.addStock(mcDonalds);
        analyzer.calculateStocksCoefficients();


        analyzer.getStocks().stream().forEach(stock -> {
            System.out.println(stock.getName()+":");
            System.out.println("Excpected value M(x):" + stock.getExpectedValue());
            System.out.println("Standard Deviation Sigma(x):" + stock.getStandardDeviation());
            System.out.println("Variation Coefficient CV(x):" + stock.getVariationCoefficient());
            System.out.println("Normalized Skewnewss IAs(x):" + stock.getNormSkewness());
            System.out.println("Normalized Kurtosis IEs(x):" + stock.getNormKurtosis());
            System.out.println("Standard SemiVariance SSV(x):" + stock.getStandardSemiVariance());
            System.out.println("SemiVariation Coefficient CSV(x):" + stock.getSemiVariationCoefficient());
        });
    }
}

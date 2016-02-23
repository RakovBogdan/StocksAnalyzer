package com.stocksanalyzer;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by RakovBogdan
 * The stock contains fields:
 * name
 * symbol, also know as ticker
 * MathStatics, which contains of statistics coefficients
 */
public class Stock {

    private String name;
    private final String symbol;
    private double[] prices;
    
    private MathStatistics statistics;

    private int score; // змінна, в яку буде записуватися кількість "очків" акції
    private double futureNormProfit; // прогнозоване значення очікуваного прибутку

    public Stock(String name, String symbol, double[] prices) {
        this.name = name;
        this.symbol = symbol;
        this.prices = prices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrices(double[] prices) {
        this.prices = prices;
    }

    public void setStatistics(MathStatistics statistics) {
        this.statistics = statistics;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setFutureNormProfit(double futureNormProfit) {
        this.futureNormProfit = futureNormProfit;
    }

    public String getName() {
        return this.name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double[] getPrices() {
        return this.prices;
    }


    public int getScore() {
        return score;
    }

    public double getFutureNormProfit() {
        return futureNormProfit;
    }

    public MathStatistics getStatistics() {
        return this.statistics;
    }

}

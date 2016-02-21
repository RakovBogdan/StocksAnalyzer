package com.stocksanalyzer;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by BogdanRakov
 */
public class Stock {

    private String name;
    private final String symbol;
    private double[] prices;
    
    private MathStatistics statistics;

    private double[] normProfit; //����� �������� ��� ����� �����
    private double expectedValue; //����������� ��������� M(x)
    private double standardDeviation; //������������������� ��������� ѳ���(x)
    private double variationCoefficient; // ���������� ������� CV(x)
    private double normSkewness; // ���������� ���������� ������� IAs(x)
    private double normKurtosis; // ���������� ���������� ������� IEs(x)
    private double standardSemiVariance; // �������������� ��������� SSV(x)
    private double semiVariationCoefficient; // ���������� ���������� CSV(x)
    private int score; // �����, � ��� ���� ������������ ������� "����" �����
    private double futureNormProfit; // ������������ �������� ����������� ��������

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

    public void setNormProfit(double[] normProfit) {
        this.normProfit = normProfit;
    }

    public void setExpectedValue(double expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public void setVariationCoefficient(double variationCoefficient) {
        this.variationCoefficient = variationCoefficient;
    }


    public void setNormKurtosis(double normKurtosis) {
        this.normKurtosis = normKurtosis;
    }

    public void setSemiVariationCoefficient(double semiVariationCoefficient) {
        this.semiVariationCoefficient = semiVariationCoefficient;
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

    public double[] getNormProfit() {
        return normProfit;
    }

    public double getExpectedValue() {
        return expectedValue;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public double getVariationCoefficient() {
        return variationCoefficient;
    }

    public double getNormKurtosis() {
        return normKurtosis;
    }

    public double getSemiVariationCoefficient() {
        return semiVariationCoefficient;
    }

    public int getScore() {
        return score;
    }

    public double getFutureNormProfit() {
        return futureNormProfit;
    }

    public double getNormSkewness() {
        return normSkewness;
    }

    public void setNormSkewness(double normSkewness) {
        this.normSkewness = normSkewness;
    }

    public double getStandardSemiVariance() {
        return standardSemiVariance;
    }

    public void setStandardSemiVariance(double standardSemiVariance) {
        this.standardSemiVariance = standardSemiVariance;
    }


}

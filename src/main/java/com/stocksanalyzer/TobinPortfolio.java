package com.stocksanalyzer;

import java.util.*;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;
/**
/**
 * Created by RakovBogdan
 */
public class TobinPortfolio {

    private Map<Stock, Double> portfolio = new LinkedHashMap<> (); //  Markovitz portfolio
    private double risk;
    private double annualProfitPercantage;
    private double dateFrequencyMultiplier;
    private Stock nonRiskProfitSecurity; // non-Risk security
    private List<Stock> allStocks = new ArrayList<>(); // ArrayList of all stocks in portfolio

    public TobinPortfolio(List<Stock> allStocks) {
        for(Stock stock: allStocks){
            if(stock.getStatistics().getMean() >= 0 )
                this.allStocks.add(stock);
            else
                System.out.println("Stock " + stock.getName() +
                        " was removed due to its mean being < 0");
        }
    }

    public String toString() {
        return Arrays.asList(portfolio) + "\n" +
                "AnnualProfit: " + this.annualProfitPercantage + "%\n" +
                "Risk: " + this.risk + "%\n";
    }

    public void minimumRisk(){
        portfolio.clear();
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");

        double profit = 0.0d;
        int stocksCount = allStocks.size();
        for (int i=0; i<stocksCount; i++) {
            if(i != stocksCount -1) {
                portfolio.put(allStocks.get(i), sol.get(i));
                profit += sol.get(i) * MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));
            } else {
                portfolio.put(allStocks.get(i), sol.get(i));
                profit += sol.get(i) * allStocks.get(i).getStatistics().getMean();
            }
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0))*100;
        this.annualProfitPercantage = profit * 100 * dateFrequencyMultiplier;

    }


    public void maximumProfit(){
        portfolio.clear();
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("means", new DoubleMatrixND(new int[]{allStocks.size(), 1}, means));
        op.setObjectiveFunction("maximize", "x * means");
        op.addConstraint(" sum(x,2) == 1");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");

        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
        }

        this.annualProfitPercantage = op.getObjectiveFunction().evaluate("x", sol).get(0) * 100 * dateFrequencyMultiplier;
        this.risk = 0;
    }

    // profits is minimum required profits
    public void minimizeRisk(double profitPercantage) {
        portfolio.clear();
        this.annualProfitPercantage = profitPercantage;
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length-1; i++) {
            means[i] = MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));

        }
        means[means.length-1] = allStocks.get(means.length-1).getStatistics().getMean();

        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("profit", profitPercantage / (100 * dateFrequencyMultiplier));
        op.setInputParameter("mean", new DoubleMatrixND(new int[]{1, allStocks.size()}, means));
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.addConstraint(" sum(x .* mean,2) >= profit", "portfolioProfit");
        op.solve("ipopt");

        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0))*100;
    }

    // risk is maximum risk that investor wants
    public void maximizeProfit(double risk) {
        portfolio.clear();
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = allStocks.get(i).getStatistics().getMean();
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[] {1, allStocks.size()} , 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("risk", risk);
        op.setInputParameter("mean", new DoubleMatrixND(new int[]{1, allStocks.size()}, means));
        op.setObjectiveFunction("maximize", "sum(x .* mean,2)");
        op.addConstraint(" sum(x,2) == 1");
        op.addConstraint(" sqrt(x*cov*x') <= risk");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++)
            portfolio.put(allStocks.get(i), sol.get(i));
        portfolio.put(nonRiskProfitSecurity, sol.get(allStocks.size()));

    }

    public Map<Stock, Double> getPortfolio() {
        return portfolio;
    }

    public double getRisk() {
        return risk;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }

    public double getAnnualProfitPercantage() {
        return annualProfitPercantage;
    }

    public void setAnnualProfitPercantage(double annualProfitPercantage) {
        this.annualProfitPercantage = annualProfitPercantage;
    }

    public List<Stock> getAllStocks() {
        return allStocks;
    }

    public void setAllStocks(List<Stock> allStocks) {
        this.allStocks = allStocks;
    }

    public Stock getNonRiskProfit() {
        return nonRiskProfitSecurity;
    }

    public double getDateFrequencyMultiplier() {
        return dateFrequencyMultiplier;
    }

    public void setDateFrequencyMultiplier(double dateFrequencyMultiplier) {
        this.dateFrequencyMultiplier = dateFrequencyMultiplier;
    }

    public void setNonRiskProfit(double nonRiskProfit) {
        double[] pricesSizeNormalized = new double[allStocks.get(0).getPrices().length];
        Arrays.fill(pricesSizeNormalized, nonRiskProfit);
        this.nonRiskProfitSecurity = new Stock("nonRiskSecurity", "NORISK", pricesSizeNormalized);

        double[] profitSizeNormalized = new double[allStocks.get(0).getPrices().length - 1];
        Arrays.fill(profitSizeNormalized, nonRiskProfit);

        MathStatistics stats = new MathStatistics(nonRiskProfitSecurity.getPrices(), true);
        stats.setNormProfit(profitSizeNormalized);
        stats.setMean(pricesSizeNormalized[0]);
        nonRiskProfitSecurity.setStatistics(stats);
        System.out.println(nonRiskProfitSecurity.getStatistics());
        allStocks.add(nonRiskProfitSecurity);

    }
}

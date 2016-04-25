package com.stocksanalyzer;

import java.util.*;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;
/**
 * Created by RakovBogdan
 */
public class MarkovitzPortfolio {

    private Map<Stock, Double> portfolio = new LinkedHashMap<> (); //  Markovitz portfolio
    private double risk; // portfolio risk
    private double annualProfitPercantage; // portfolio profit
    private List<Stock> allStocks = new ArrayList<>(); // ArrayList of all stocks in portfolio
    private double dateFrequencyMultiplier;

    public MarkovitzPortfolio(List<Stock> allStocks) {
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

    //returns minimum possible
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
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
            profit += sol.get(i)* MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0))*100;
        this.annualProfitPercantage = profit * 100 * dateFrequencyMultiplier;

    }

    //returns maximum profit possible
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


    // profit is minimum required profit
    // Profit in Percentage! so if u want minimum 10% of profit, u just pass 10
    public void minimizeRisk(double profitPercantage) {
        portfolio.clear();
        this.annualProfitPercantage = profitPercantage;
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));
        }
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

    // new int[] {rows, columns}
    // risk is maximum risk that investor accept
    // doesnt work ;(
    public void maximizeProfit(double riskInPercantage) {
        portfolio.clear();
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = MathStatistics.mean(MathStatistics.calculateNormProfit(allStocks.get(i).getPrices()));
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("means", new DoubleMatrixND(new int[]{allStocks.size(), 1}, means));
        op.setInputParameter("risk", riskInPercantage/100);
        op.setObjectiveFunction("maximize", "x * means");
        op.addConstraint(" sum(x,2) == 1");
        //op.addConstraint("(x*cov*x')<=risk");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");

        double profit = op.getObjectiveFunction().evaluate("x", sol).get(0);
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
        }
        this.risk = 10000;
        this.annualProfitPercantage = profit;
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

    public void setAnnualProfitPercantage(double profit) {
        this.annualProfitPercantage = profit;
    }

    public List<Stock> getAllStocks() {
        return allStocks;
    }

    public void setAllStocks(List<Stock> allStocks) {
        this.allStocks = allStocks;
    }

    public double getDateFrequencyMultiplier() {
        return dateFrequencyMultiplier;
    }

    public void setDateFrequencyMultiplier(double dateFrequencyMultiplier) {
        this.dateFrequencyMultiplier = dateFrequencyMultiplier;
    }

}

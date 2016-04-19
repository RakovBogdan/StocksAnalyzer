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
    private double risk; // portfolio risk
    private double profit; // portfolio profit
    private Stock nonRiskProfitSecurity; // non-Risk security
    private List<Stock> allStocks = new ArrayList<>(); // ArrayList of all stocks in portfolio

    public TobinPortfolio(List<Stock> allStocks) {
        this.allStocks = allStocks;
    }

    // returns covarianceMatrix of allStocks
    public double[][] covarianceMatrix (){
        double [][] covMatrix = new double[allStocks.size()][allStocks.size()];
        for (int i=0;i<allStocks.size();i++){
            for (int j=0; j<allStocks.size();j++){
                if(i == j)
                    covMatrix [i][j] = MathStatistics.calculateVariance(allStocks.get(i).getStatistics().getNormProfit());
                else
                    covMatrix [i][j] = MathStatistics.covariance(allStocks.get(i).getStatistics().getNormProfit(),
                            allStocks.get(j).getStatistics().getNormProfit());
            }
        }
        System.out.println(Arrays.deepToString(covMatrix));
        return covMatrix;
    }

    public void maximumProfit(){
        portfolio.clear();
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", covarianceMatrix());
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2)== 1");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");

        double profit = 0.0d;
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
            profit += sol.get(i)*allStocks.get(i).getStatistics().getMean();
        }

        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0));
        this.profit = profit;

    }

    // profit is minimum required profit
    //obligation year propfit comes from assets, which has no risk(it could be not obligations only, but depozits instead)
    public void minimizeRisk(double profit, double obligationYearProfit) {
        portfolio.clear();
        this.profit = profit;
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = allStocks.get(i).getStatistics().getMean();
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", covarianceMatrix());
        op.setInputParameter("profit", profit);
        op.setInputParameter("mean", new DoubleMatrixND(new int[]{1, allStocks.size()}, means));
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.addConstraint(" sum(x .* mean,2) >= profit", "portfolioProfit");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0));
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
        op.setInputParameter("cov", covarianceMatrix());
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

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
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

    public void setNonRiskProfit(double nonRiskProfit) {
        double[] pricesSizeNormalized = new double[allStocks.get(0).getPrices().length];
        Arrays.fill(pricesSizeNormalized, nonRiskProfit);
        this.nonRiskProfitSecurity = new Stock("nonRiskSecurity", "NORISK", pricesSizeNormalized);

        double[] profitSizeNormalized = new double[allStocks.get(0).getPrices().length-1];
        Arrays.fill(pricesSizeNormalized, nonRiskProfit);

        MathStatistics stats = new MathStatistics(nonRiskProfitSecurity.getPrices(), true);
        stats.setNormProfit(profitSizeNormalized);
        stats.setMean(pricesSizeNormalized[0]);
        nonRiskProfitSecurity.setStatistics(stats);
        System.out.println(nonRiskProfitSecurity.getStatistics());
        allStocks.add(nonRiskProfitSecurity);

    }
}

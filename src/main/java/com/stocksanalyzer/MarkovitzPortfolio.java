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
    private double profit; // portfolio profit
    private List<Stock> allStocks = new ArrayList<>(); // ArrayList of all stocks in portfolio

    public MarkovitzPortfolio(List<Stock> allStocks) {
        for(Stock stock: allStocks){
            if(stock.getStatistics().getMean() >= 0 )
                this.allStocks.add(stock);
            else
                System.out.println("Stock " + stock.getName() +
                    "was removed due to its mean being < 0");
        }
    }

    //returns maximum profit possible
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
            profit += sol.get(i)* MathStatistics.mean(allStocks.get(i).getStatistics().getNormProfit());
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0))*100;
        this.profit = profit;

    }

    //returns maximum profit possible
    public void maximumProfit(){
        portfolio.clear();
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setObjectiveFunction("maximize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");

        double profit = 0.0d;
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
            profit += sol.get(i)* MathStatistics.mean(allStocks.get(i).getStatistics().getNormProfit());
        }
        this.risk = Math.sqrt(op.getObjectiveFunction().evaluate("x", sol).get(0))*100;
        this.profit = profit;
    }

    // profit is minimum required profit
    // Profit in Percentage! so if u want minimum 10% of profit, u just pass 10
    public void minimizeRisk(double profitPercantage) {
        portfolio.clear();
        this.profit = profitPercantage;
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = allStocks.get(i).getStatistics().getMean();
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("profit", profitPercantage/100);
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
    public void maximizeProfit(double risk) {
        portfolio.clear();
        this.risk = risk;
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = allStocks.get(i).getStatistics().getMean();
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", MathStatistics.stocksCovarianceMatrix(allStocks));
        op.setInputParameter("risk", risk);
        op.setInputParameter("mean", new DoubleMatrixND(new int[]{1, allStocks.size()}, means));
        op.setObjectiveFunction("maximize", "sum(x .* mean,2)");
        op.addConstraint(" sum(x,2) == 1");
        op.addConstraint(" sqrt(x*cov*x') == risk", "portfolioRisk");
        op.solve("ipopt");

        double profit = 0.0d;
        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++) {
            portfolio.put(allStocks.get(i), sol.get(i));
            profit += sol.get(i)*allStocks.get(i).getStatistics().getMean();
        }
        this.profit = profit;
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

}

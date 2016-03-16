package com.stocksanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;
/**
 * Created by RakovBogdan
 */
public class MarkovitzPortfolio {

    private Map<Stock, Double> portfolio = new HashMap<> (); //  �������� ������ ������ ��������
    private double risk; // ����� ��������
    private double profit; // �������� ��������
    private List<Stock> allStocks = new ArrayList<>(); // ������� Arraylist �����

    public MarkovitzPortfolio(List<Stock> allStocks) {
        this.allStocks = allStocks;
    }

    public double[][] covarianceMatrix (){
        double [][] covMatrix = new double[allStocks.size()][allStocks.size()];
        for (int i=0;i<allStocks.size();i++){
            for (int j=0; j<allStocks.size();j++){
                if(i == j)
                    covMatrix [i][j] = MathStatistics.calculateVariance(allStocks.get(i).getStatistics().getNormProfit())/10000;
                else
                    covMatrix [i][j] = MathStatistics.covariance(allStocks.get(i).getStatistics().getNormProfit(),
                            allStocks.get(j).getStatistics().getNormProfit())/10000;
            }
        }
        return covMatrix;
    }

    public void solver(){
        portfolio.clear();
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[]{1, allStocks.size()}, 0.0d, 1.0d);
        op.setInputParameter("cov", covarianceMatrix());
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++)
            portfolio.put(allStocks.get(i), sol.get(i));
    }

    // profit is minimum required profit
    public void minimizeRisk(double profit) {
        portfolio.clear();
        double[] means = new double[allStocks.size()];
        for(int i=0; i < means.length; i++) {
            means[i] = allStocks.get(i).getStatistics().getMean();
        }
        OptimizationProblem op = new OptimizationProblem();
        op.addDecisionVariable("x", false, new int[] {1, allStocks.size()} , 0.0d, 1.0d);
        op.setInputParameter("cov", covarianceMatrix());
        op.setInputParameter("profit", profit);
        op.setInputParameter("mean", new DoubleMatrixND(new int[]{1, allStocks.size()}, means));
        op.setObjectiveFunction("minimize", "x*cov*x'");
        op.addConstraint(" sum(x,2) == 1");
        op.addConstraint(" sum(x .* mean,2) >= profit");
        op.solve("ipopt");
        DoubleMatrixND sol = op.getPrimalSolution("x");
        for (int i=0; i<allStocks.size();i++)
            portfolio.put(allStocks.get(i), sol.get(i));
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

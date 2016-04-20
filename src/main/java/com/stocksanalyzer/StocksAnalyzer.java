package com.stocksanalyzer;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Created by BogdanRakov
 */
public class StocksAnalyzer {

    private List<Stock> allStocks = new ArrayList<>();

    private MarkovitzPortfolio markovitzPortfolio;
    private TobinPortfolio tobinPortfolio;

    private YahooDataLoader yahooDataLoader;

    public void loadStock(String name, String symbol) {
        allStocks.add(yahooDataLoader.getData(name, symbol));
    }

    /*
     * Calculates and sets allStocks normProfits and coefficients
     */
    public void calculateStocksCoefficients(boolean inPercentage) {
        allStocks.stream().forEach(stock -> {
            MathStatistics stats = new MathStatistics(stock.getPrices(), inPercentage);
            stats.evaluateStatistics();
            stock.setStatistics(stats);
        });
    }

    public YahooDataLoader getYahooDataLoader() {
        return yahooDataLoader;
    }

    public void setYahooDataLoader(int startDay, int startMonth, int startYear,
                              int endDay, int endMonth, int endYear, String frequency) {
        this.yahooDataLoader = new YahooDataLoader(startDay, startMonth, startYear,
                                endDay, endMonth, endYear, frequency);
    }

    public void setNonRiskSecurity(double nonRiskSecurityYearProfit) {
        switch (this.yahooDataLoader.getFrequency()) {
            case "d" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit/365);
                break;
            case "m" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit/12);
                break;
            case "y" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit);
                break;
        }
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

    public void createMarkovitzPortfolio() {
        this.markovitzPortfolio = new MarkovitzPortfolio(allStocks);
    }

    public TobinPortfolio getTobinPortfolio() {
        return tobinPortfolio;
    }

    public void createTobinPortfolio() {
        this.tobinPortfolio = new TobinPortfolio(allStocks);
    }


    public static void main(String[] args) {
        //Each HTTP session should create new StockAnalyzer
        StocksAnalyzer analyzer = new StocksAnalyzer();

        //Loading data with using YahooDataLoader
        //two dates in integer format: day,month,year
        // last string is data frequency: "d" -daily, "m" - monthly, "y" - yearly
        //TODO: make Set Data range like here http://finance.yahoo.com/q/hp?s=GE
        analyzer.setYahooDataLoader(17, 11, 2010, 17, 1, 2016, "d");

        //adding stock to allStocks, symbol YHOO is very important
        //TODO: when client types in first letter "Y", 10 results must be shown(like here http://finance.yahoo.com/lookup),


        analyzer.loadStock("Coca-Cola", "KO");
        analyzer.loadStock("Ford", "F");
        analyzer.loadStock("Chevron", "CVX");

        //This is the first part. Every Stock will get its MathStatistics object calculated
        //U have to choose percentage or non percentage mode
        analyzer.calculateStocksCoefficients(false);
        //After calculations, u can get coefficients of mean and skewness for the first Stock like this:
        for(Stock stock : analyzer.getStocks()) {
            System.out.println(stock
                    + " mean: " + stock.getStatistics().getMean());
            System.out.println(stock
                    + " variance: " + stock.getStatistics().getStandardDeviation());
        }
        //TODO: U will need to display all coefficients from every Stock in allStocks

        //This is 2nd part. There are 2 kinds of portfolio: Markovitz and Tobin
        analyzer.createMarkovitzPortfolio();
        //Markovitz portfolio with minimum risk
        analyzer.getMarkovitzPortfolio().minimumRisk();
        System.out.println("Minimum risk portfolio");
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio year profit: " + analyzer.getMarkovitzPortfolio().getProfit() *365 + "%");

        analyzer.getMarkovitzPortfolio().maximumProfit();
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio year profit: " + analyzer.getMarkovitzPortfolio().getProfit() *365  + "%");

        //Markovitz portfolio with minimum given profit level of 0.00811%, where me minimize risk level:
        analyzer.getMarkovitzPortfolio().minimizeRisk(9/365);
        //TODO: U will need to display portfolio, also its risk and profit rates
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getProfit()  + "%");

        /* Just doesnt work
        //Markovitz portfolio with given risk level 0,5%, where we maximize profit
        analyzer.getMarkovitzPortfolio().maximizeProfit(0.005d);
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk());
        System.out.println("portfolio profit: " + analyzer.getMarkovitzPortfolio().getProfit());
        */


    }
}

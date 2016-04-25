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

    //inPercentage
    public void setNonRiskSecurity(double nonRiskSecurityYearProfit) {
        switch (this.yahooDataLoader.getFrequency()) {
            case "d" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit/(365*100));
                break;
            case "m" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit/(12*100));
                break;
            case "y" :
                this.tobinPortfolio.setNonRiskProfit(nonRiskSecurityYearProfit * 100);
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
        switch (this.yahooDataLoader.getFrequency()) {
            case "d" :
                this.markovitzPortfolio.setDateFrequencyMultiplier(365);
                break;
            case "m" :
                this.markovitzPortfolio.setDateFrequencyMultiplier(12);
                break;
            case "y" :
                this.markovitzPortfolio.setDateFrequencyMultiplier(1);
                break;
        }
    }

    public TobinPortfolio getTobinPortfolio() {
        return tobinPortfolio;
    }

    public void createTobinPortfolio() {
        this.tobinPortfolio = new TobinPortfolio(allStocks);
        switch (this.yahooDataLoader.getFrequency()) {
            case "d" :
                this.tobinPortfolio.setDateFrequencyMultiplier(365);
                break;
            case "m" :
                this.tobinPortfolio.setDateFrequencyMultiplier(12);
                break;
            case "y" :
                this.tobinPortfolio.setDateFrequencyMultiplier(1);
                break;
        }
    }


    public static void main(String[] args) {
        //Each HTTP session should create new StockAnalyzer
        StocksAnalyzer analyzer = new StocksAnalyzer();

        //Loading data with using YahooDataLoader
        //two dates in integer format: day,month,year
        // last string is data frequency: "d" -daily, "m" - monthly, "y" - yearly
        //TODO: make Set Data range like here http://finance.yahoo.com/q/hp?s=GE
        analyzer.setYahooDataLoader(1, 1, 2011, 1, 1, 2016, "d");

        //adding stock to allStocks, symbol YHOO is very important
        //TODO: when client types in first letter "Y", 10 results must be shown(like here http://finance.yahoo.com/lookup),


        analyzer.loadStock("Coca-Cola", "KO");
        analyzer.loadStock("Ford", "F");
        analyzer.loadStock("Chevron", "CVX");
        analyzer.loadStock("P&G", "PG");
        analyzer.loadStock("Apple", "AAPL");
        analyzer.loadStock("IBM", "IBM");

        //This is the first part. Every Stock will get its MathStatistics object calculated
        //U have to choose percentage or non percentage mode
        analyzer.calculateStocksCoefficients(false);
        //After calculations, u can get coefficients of mean and skewness for the first Stock like this:
        analyzer.calculateStocksCoefficients(true);
        analyzer.getStocks().stream().forEach(stock -> {
            System.out.println(stock.getName());
            System.out.println(stock.getStatistics());
            System.out.println("--------------------");
        });
        //TODO: U will need to display all coefficients from every Stock in allStocks

        //This is 2nd part. There are 2 kinds of portfolio: Markovitz and Tobin
        analyzer.createMarkovitzPortfolio();
        //Markovitz portfolio with minimum risk
        analyzer.getMarkovitzPortfolio().minimumRisk();
        System.out.println("Minimum risk portfolio");
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
        System.out.println("portfolio risk: " + analyzer.getMarkovitzPortfolio().getRisk() + "%");
        System.out.println("portfolio year profit: " + analyzer.getMarkovitzPortfolio().getAnnualProfitPercantage() + "%");

        analyzer.getMarkovitzPortfolio().maximumProfit();
        System.out.println("\n" + "Maximum Profit Portfolio:\n" + analyzer.getMarkovitzPortfolio());

        //Markovitz portfolio with minimum given annual profit level of 15%, where me minimize risk level:
        analyzer.getMarkovitzPortfolio().minimizeRisk(15);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getMarkovitzPortfolio());

        analyzer.getMarkovitzPortfolio().minimizeRisk(20);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getMarkovitzPortfolio());

        analyzer.getMarkovitzPortfolio().minimizeRisk(25);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getMarkovitzPortfolio());

        analyzer.getMarkovitzPortfolio().minimizeRisk(30.5869);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getMarkovitzPortfolio());



        analyzer.createTobinPortfolio();
        analyzer.setNonRiskSecurity(14);

        analyzer.getTobinPortfolio().maximumProfit();
        System.out.println("\n" + "Maximum Profit Portfolio:\n" + analyzer.getTobinPortfolio());

        analyzer.getTobinPortfolio().minimumRisk();
        System.out.println("\n" + "Minimum Risk Portfolio:\n" + analyzer.getTobinPortfolio());

        analyzer.getTobinPortfolio().minimizeRisk(15);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getTobinPortfolio());

        analyzer.getTobinPortfolio().minimizeRisk(20);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getTobinPortfolio());

        analyzer.getTobinPortfolio().minimizeRisk(25);
        System.out.println("\n" + "Minimize Risk Portfolio:\n" + analyzer.getTobinPortfolio());

    }
}

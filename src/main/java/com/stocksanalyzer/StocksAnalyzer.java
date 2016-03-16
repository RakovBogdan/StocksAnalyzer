package com.stocksanalyzer;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by BogdanRakov
 */
public class StocksAnalyzer {

    private List<Stock> allStocks = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private String frequency;
    private MarkovitzPortfolio markovitzPortfolio = new MarkovitzPortfolio(allStocks);
    private Map<Stock, Double> Markovitz = new HashMap<> (); // ArrayList всіх просортованих акцій

    public StocksAnalyzer(String startDate, String endDate, String frequency) {
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.frequency = frequency;
    }

    /*
     * Calculates and sets allStocks normProfits and coefficients
     */
    public void calculateStocksCoefficients() {
        allStocks.stream().forEach(stock -> {
            MathStatistics stats = new MathStatistics(stock.getPrices());
            stats.evaluateStatistics();
            stock.setStatistics(stats);
        });
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

    public void createMarkovitzPortfolio(List<Stock> allStocks) {
        this.markovitzPortfolio = new MarkovitzPortfolio(allStocks);
    }


    public static void main(String[] args) {
        StocksAnalyzer analyzer = new StocksAnalyzer("2015-11-17", "2015-12-17", "d");

        //Створюємо нову акцію та додаємо її до аналізатора:
        //Масив цін
        double[] pricesGazprom = {139.20, 135.50, 128.77,141.70, 148.96, 132.00, 131.95, 137.90,
                141.50, 142.86, 130.31, 143.82, 152.95};
        //СТворюємо акцію, вводимо назву, тікер та масив цін
        Stock gazProm = new Stock("gazProm", "GZPM", pricesGazprom);
        //Додаємо акцію до аналізатора
        analyzer.addStock(gazProm);

        //Теж саме, додаємо ще 2 акції
        double[] pricesGMKNikel = {5980.00,5865.00, 6405.00, 6656.00, 6719.00, 7060.00,7230.00,
                7320.00, 8033.00, 8820.00, 8080.00, 11610.00, 11182.00};
        Stock gMKNikel = new Stock("GMKNorNikel", "GMKNN", pricesGMKNikel);
        analyzer.addStock(gMKNikel);

        double[] pricesMechel = {39.90, 38.40, 37.50, 47.50, 52.40, 38.50,32.90, 24.60,
                21.59, 22.60, 24.71, 44.85, 82.27};
        Stock mechel = new Stock("Mechel", "MCHL", pricesMechel);
        analyzer.addStock(mechel);

        //Це 1 частина лаби. Для кожної акціїї Stock буде параховано коефіцієнти MathStatistics
        analyzer.calculateStocksCoefficients();
        //Приклад діставання коефіцієнту середнього значення mean та асиметрії skewness з акції газпрому
        System.out.println(analyzer.allStocks.get(0).getStatistics().getMean());
        System.out.println(analyzer.allStocks.get(0).getStatistics().getSkewness());
        //Потрібно буде для кожної акції відобразити всі коефіцієнти
        //Напиши мені, як тобі буде ЗРУЧНІШЕ їх діставати. Я можу написати зручний метод

        //Це 2 частина. Тут ми будуємо різні види портфелів, Марковіца та Тобіна
        //Портфель Марковіца із заданим рівнем мінімального приубутку (ми мінімізуємо ризики)
        analyzer.createMarkovitzPortfolio(analyzer.allStocks);
        analyzer.getMarkovitzPortfolio().minimizeRisk(0.04);
        System.out.println(Arrays.asList(analyzer.getMarkovitzPortfolio().getPortfolio()));
    }
}

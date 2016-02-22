package com.stocksanalyzer;

import java.util.stream.DoubleStream;

/**
 * Created by RakovBogdan
 * Class with static methods for statistics calculations
 * has all statistics coefficients as fields
 * has method evaluateStatistics which calculates ann coefficients at once
 * constructor requires double array of data
 */
public class MathStatistics {

    private double[] data;

    private double[] normProfit; //Норми прибутку для кожної акції
    private double mean; //Матиматичне сподівання M(x)
    private double standardDeviation; //Середньоквадратичне відхилення Сігма(x)
    private double skewness; // коефіцієнт асиметрії As(x)
    private double kurtosis; // коефіцієнт ексцесу Es(x)
    private double varianceCoefficient; // Коефіцієнт варіації CV(x)
    private double normSkewness; // нормований коефіцієнт асиметрії IAs(x)
    private double normKurtosis; // нормований Коефіцієнт Ексцесу IEs(x)
    private double standardSemiVariance; // семіквадратичне відхилення SSV(x)
    private double semiVarianceCoefficient; // Коефіцієнт семіваріації CSV(x)

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public double[] getNormProfit() {
        return normProfit;
    }

    public void setNormProfit(double[] normProfit) {
        this.normProfit = normProfit;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getSkewness() {
        return skewness;
    }

    public void setSkewness(double skewness) {
        this.skewness = skewness;
    }

    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public double getVarianceCoefficient() {
        return varianceCoefficient;
    }

    public void setVarianceCoefficient(double varianceCoefficient) {
        this.varianceCoefficient = varianceCoefficient;
    }

    public double getNormKurtosis() {
        return normKurtosis;
    }

    public void setNormKurtosis(double normKurtosis) {
        this.normKurtosis = normKurtosis;
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

    public double getSemiVarianceCoefficient() {
        return semiVarianceCoefficient;
    }

    public void setSemiVarianceCoefficient(double semiVarianceCoefficient) {
        this.semiVarianceCoefficient = semiVarianceCoefficient;
    }

    public MathStatistics(double[] data) {
        this.data = data;
    }
    /*
     * Returns the array of Price Changes in percentage
     * formula: j_i = ((x_(i+1) - x_i)/x_i) * 100
     * j_i is the element of resulting array, x_i is the element of given array
     */
    public static double[] calculateNormProfit(double data[]) {
        double[] normProfit = new double[data.length - 1];
        for (int i=0; i<normProfit.length; i++) {
            normProfit[i] = (data[i+1]-data[i])/data[i]*100;
        }
        return normProfit;
    }

    /*
     * Returns mean M(x) of given array
     * formula: mean = sum(x_i)/n
     * where n is the number of observations
     */
    public static double mean(double[] data) {
        return (DoubleStream.of(data).sum())/data.length;
    }

    /*
     * Returns standard deviation Sigma(x) of given array
     * formula: std = sum((x_i - mean)^2)/(n-1)
     * where n is the number of observations
     */
    public static double calculateStandardDeviation(double[] data) {
        Double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow(element - mean(data), 2));
        return Math.sqrt(sum/(data.length-1));
    }

    /*
     * Returns standard deviation Sigma(x) of given array and given mean
     * formula: std = sum((x_i - mean)^2)/(n-1)
     * where n is the number of observations
     */
    public static double calculateStandardDeviation(double[] data, double mean) {
        Double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow(element - mean, 2));
        return Math.sqrt(sum/(data.length-1));
    }

    /*
     * Returns variance coefficient VC(x) of given array
     * formula: mean = std/mean
     * where std is the standard deviation
     */
    public static double calculateVarianceCoefficient(double[] data) {
        return calculateStandardDeviation(data)/ mean(data);
    }

    /*
     * Returns variance coefficient VC(x) of given array, standart deviation and mean
     * formula: mean = std/mean
     * where std is the standard deviation
     */
    public static double calculateVarianceCoefficient(double standardDeviation,
                                                      double mean) {
        return standardDeviation/mean;
    }

    /*
     * Returns skewness As(x) of given array
     * formula: (n/(n-1)*(n+1))*sum(((x_i-mean)/std)^3)
     * where std is the standard deviation, n is the number of observations
     */
    public static double calculateSkewness(double[] data) {
        double n = data.length;
        double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow((element - mean(data))/ calculateStandardDeviation(data), 3));
        return (n/((n-1)*(n-2)))*sum;
    }

    /*
     * Returns skewness As(x) of given array, standard deviation and mean
     * formula: (n/(n-1)*(n+1))*sum(((x_i-mean)/std)^3)
     * where std is the standard deviation, n is the number of observations
     */
    public static double calculateSkewness(double[] data, double standardDeviation,
                                           double mean) {
        double n = data.length;
        double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow((element - mean)/ standardDeviation, 3));
        return (n/((n-1)*(n-2)))*sum;
    }

    /*
     * Returns kurtosis Es(x) of given array
     * formula: {(n*(n+1)/(n-1)*(n-2)*(n-3))*sum(((x_i-mean)/std)^4)} - [(3*(n-1)^2)/((n-2)*(n-3))]
     * where std is the standard deviation, n is the number of observations
     */
    public static double calculateKurtosis(double[] data) {
        double n = data.length;
        double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow((element - mean(data))/calculateStandardDeviation(data), 4));
        return (n*(n+1))/((n-1)*(n-2)*(n-3))* sum-
                3*Math.pow(n-1,2)/((n-2)*(n-3));
    }

    /*
     * Returns kurtosis Es(x) of given array, standard deviation and mean
     * formula: {(n*(n+1)/(n-1)*(n-2)*(n-3))*sum(((x_i-mean)/std)^4)} - [(3*(n-1)^2)/((n-2)*(n-3))]
     * where std is the standard deviation, n is the number of observations
     */
    public static double calculateKurtosis(double[] data, double standardDeviation,
                                           double mean) {
        double n = data.length;
        double sum = DoubleStream.of(data).reduce(0d, (acc, element) -> acc +
                Math.pow((element - mean)/standardDeviation, 4));
        return (n*(n+1))/((n-1)*(n-2)*(n-3))* sum-
                3*Math.pow(n-1,2)/((n-2)*(n-3));
    }

    /*
     * calculates normalized skewness IAs(x) for given array
     * Returns 1/(skewness+1) if skewness is greater than 0
     * returns 1-skewness otherwise
     */
    public static double calculateNormSkewness(double[] data) {
        double skewness = calculateSkewness(data);
        if (skewness>0)
            return  1/(skewness+1);
        else
            return 1-skewness;
    }

    /*
     * calculates normalized skewness IAs(x) for given array and skewness
     * Returns 1/(skewness+1) if skewness is greater than 0
     * returns 1-skewness otherwise
     */
    public static double calculateNormSkewness(double[] data, double skewness) {
        if (skewness>0)
            return  1/(skewness+1);
        else
            return 1-skewness;
    }

    /*
     * calculates normalized kurtosis IEs(x) for given array
     * Returns 1/(kurtosis+1) if kurtosis is greater than 0
     * returns 1-kurtosis otherwise
     */
    public static double calculateNormKurtosis(double[] data) {
        double kurtosis = calculateKurtosis(data);
        if (kurtosis>0)
            return  1/(kurtosis+1);
        else
            return 1-kurtosis;
    }

    /*
     * calculates normalized kurtosis IEs(x) for given array and kurtosis
     * Returns 1/(kurtosis+1) if kurtosis is greater than 0
     * returns 1-kurtosis otherwise
     */
    public static double calculateNormKurtosis(double[] data, double kurtosis) {
        if (kurtosis>0)
            return  1/(kurtosis+1);
        else
            return 1-kurtosis;
    }

    /*
     * Returns standard semi variance SSV(x) for given array
     * formula: (sum(sv(x_i))/m)^1/2
     * where sv(x_i) is semi variance, which equals to 0 when x_i is greater than mean
     * or equals to (x_i-mean)^2 otherwise
     * m is the number of elements x_i, which are equal or less than mean
     *
     */
    public static double calculateStandardSemiVariance(double[] data) {
        double[] sv = new double[data.length];
        double mean = mean(data);
        int counter = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i]>mean)
                sv[i] = 0;
            else{
                sv[i] = Math.pow(data[i]-mean,2);
                counter++;
            }
        }
        double sum = DoubleStream.of(sv).sum();
        return Math.sqrt(sum/counter);
    }

    /*
     * Returns standard semi variance SSV(x) for given array and mean
     * formula: (sum(sv(x_i))/m)^1/2
     * where sv(x_i) is semi variance, which equals to 0 when x_i is greater than mean
     * or equals to (x_i-mean)^2 otherwise
     * m is the number of elements x_i, which are equal or less than mean
     *
     */
    public static double calculateStandardSemiVariance(double[] data, double mean) {
        double[] sv = new double[data.length];
        int counter = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i]>mean)
                sv[i] = 0;
            else{
                sv[i] = Math.pow(data[i]-mean,2);
                counter++;
            }
        }
        double sum = DoubleStream.of(sv).sum();
        return Math.sqrt(sum/counter);
    }

    /*
     * Returns semi variance coefficient CSV(x) for given array
     * formula: csv = ssv/mean
     * where ssv is standard semi variance
     */
    public static double calculateSemiVariationCoefficient(double[] data) {
        double SSV = calculateStandardSemiVariance(data);
        return SSV/ mean(data);
    }

    /*
     * Returns semi variance coefficient CSV(x) for given array, standard semi variance and mean
     * formula: csv = ssv/mean
     * where ssv is standard semi variance
     */
    public static double calculateSemiVariationCoefficient(double[] data, double SSV,
                                                           double mean) {
        return SSV/mean;
    }

    /*
     * Returns covariance between two given arrays
     * formula: cov = sum((x_i-mean(x))*(y_i-mean(y)))/(n-1)
     * where n is number of observations
     */
    public static double covariance(double[] x, double[] y) {
        double xmean = mean(x);
        double ymean = mean(y);
        double sum = 0;
        for (int i = 0; i < x.length; i++)
            sum += (x[i] - xmean) * (y[i] - ymean);

        return sum/(x.length-1);
    }

    /*
     * Evaluates and sets all Statistics coefficients for MathStatistics object
     */
    public void evaluateStatistics () {
        normProfit = calculateNormProfit(data);
        mean = mean(normProfit);
        standardDeviation = calculateStandardDeviation(normProfit, mean);
        varianceCoefficient = calculateVarianceCoefficient(standardDeviation, mean);
        skewness = calculateSkewness(normProfit, standardDeviation, mean);
        kurtosis = calculateKurtosis(normProfit, standardDeviation, mean);
        normSkewness = calculateNormSkewness(normProfit, skewness);
        normKurtosis = calculateNormKurtosis(normProfit, kurtosis);
        standardSemiVariance = calculateStandardSemiVariance(normProfit, mean);
        semiVarianceCoefficient = calculateVarianceCoefficient(standardDeviation, mean);
    }

    //testing
    public static void main(String[] args) {
        double[] prices = {96.90, 97.41, 97.16, 97.00, 96.37,
                                95.13, 95.00, 96.19, 97.30, 97.54};
        MathStatistics stats = new MathStatistics(prices);
        stats.evaluateStatistics();
        System.out.println("Excpected value M(x):" + stats.mean);
        System.out.println("Standard Deviation Sigma(x):" + stats.standardDeviation);
        System.out.println("Variation Coefficient CV(x):" + stats.varianceCoefficient);
        System.out.println("Skewness As(x):" + stats.skewness);
        System.out.println("Kurtosis Es(x):" + stats.kurtosis);
        System.out.println("Normalized Skewnewss IAS(x):" + stats.normSkewness);
        System.out.println("Normalized Kurtosis IEs(x):" + stats.normKurtosis);
        System.out.println("Standard SemiVariance SSV(x):" + stats.standardSemiVariance);
        System.out.println("SemiVariation Coefficient CSV(x):" + stats.semiVarianceCoefficient);


    }
}

package com.stocksanalyzer;

/**
 * Created by RakovBogdan
 *
 */
public enum DataFrequency {
    DAILY("d", 365),
    MONTHLY("m", 12);

    private String codeForYahooDataLoader;
    private int timesInOneYear;

    DataFrequency(String codeForYahooDataLoader, int timesInOneYear) {
        this.codeForYahooDataLoader = codeForYahooDataLoader;
        this.timesInOneYear = timesInOneYear;
    }

    public String getCodeForYahooDataLoader() {
        return codeForYahooDataLoader;
    }

    public int getTimesInOneYear() {
        return timesInOneYear;
    }
}


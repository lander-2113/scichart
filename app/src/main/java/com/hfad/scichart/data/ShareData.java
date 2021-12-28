package com.hfad.scichart.data;

public class ShareData {
    private Double dailyShareValue;
    private Double todayLow;
    private Double todayHigh;
    private Double closingValue;

    public ShareData(Double dailyShareValue, Double todayLow, Double todayHigh, Double closingValue) {

        if(dailyShareValue == null) {
            this.dailyShareValue = -30d;
        } else {
            this.dailyShareValue = dailyShareValue;
        }

        if(todayLow == null) {
            this.todayLow = -30d;
        } else {
            this.todayLow = todayLow;
        }

        if(todayHigh == null) {
            this.todayHigh = 30d;
        } else {
            this.todayHigh = todayHigh;
        }

        if(closingValue == null) {
            this.closingValue = 30d;
        } else {
            this.closingValue = closingValue;
        }
    }

    public Double getDailyShareValue() {
        return dailyShareValue;
    }

    public Double getTodayLow() {
        return todayLow;
    }

    public Double getTodayHigh() {
        return todayHigh;
    }

    public Double getClosingValue() {
        return closingValue;
    }

    public void setDailyShareValue(Double dailyShareValue) {
        this.dailyShareValue = dailyShareValue;
    }

    public void setTodayLow(Double todayLow) {
        this.todayLow = todayLow;
    }

    public void setTodayHigh(Double todayHigh) {
        this.todayHigh = todayHigh;
    }

    public void setClosingValue(Double closingValue) {
        this.closingValue = closingValue;
    }
}

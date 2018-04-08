package com.msi.manning.weather.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean to represent a specific weather record as taken from Yahoo Weather API.
 * 
 * Units are: temperature="F" distance="mi" pressure="in" speed="mph"
 * 
 * @author charliecollins
 * 
 */
public class WeatherRecord {

    public static final String PRESSURE_RISING = "Rising"; // 1
    public static final String PRESSURE_STEADY = "Steady"; // 0
    public static final String PRESSURE_FALLING = "Falling"; // 2

    private static List<WeatherCondition> severeConditions;

    private boolean overrideSevere;
    private boolean severe;
    private String date;
    private int temp;
    private String city;
    private String region;
    private String country;
    private String windDirection;
    private int windSpeed;
    private String windChill;
    private int humidity;
    private int visibility;
    private double pressure;
    private String pressureState;
    private String sunrise;
    private String sunset;
    private String link;
    private WeatherCondition condition;
    private WeatherForecast[] forecasts;

    static {
        WeatherRecord.severeConditions = new ArrayList<WeatherCondition>();
        WeatherRecord.severeConditions.add(WeatherCondition.BLOWING_SNOW);
        WeatherRecord.severeConditions.add(WeatherCondition.FREEZING_RAIN);
        WeatherRecord.severeConditions.add(WeatherCondition.FREEZING_DRIZZLE);
        WeatherRecord.severeConditions.add(WeatherCondition.HAIL);
        WeatherRecord.severeConditions.add(WeatherCondition.HEAVY_SNOW);
        WeatherRecord.severeConditions.add(WeatherCondition.HEAVY_SNOW_WINDY);
        WeatherRecord.severeConditions.add(WeatherCondition.HURRICANE);
        WeatherRecord.severeConditions.add(WeatherCondition.SEVERE_THUNDERSTORMS);
        WeatherRecord.severeConditions.add(WeatherCondition.TORNADO);
        WeatherRecord.severeConditions.add(WeatherCondition.TROPICAL_STORM);
    }

    public WeatherRecord(boolean overrideSevere) {
        this.overrideSevere = overrideSevere;
        this.forecasts = new WeatherForecast[2];
    }

    public WeatherRecord() {
        this(false);
    }

    public boolean isOverrideSevere() {
        return this.overrideSevere;
    }

    public void setOverrideSevere(boolean overrideSevere) {
        this.overrideSevere = overrideSevere;
    }

    public boolean isSevere() {
        // if any FORECAST is one of "severeConditions" then it's severe (or if it's overridden for
        // dev)
        if (this.overrideSevere) {
            return true;
        }
        for (int i = 0; this.forecasts != null && i < this.forecasts.length; i++) {
            WeatherCondition cond = this.forecasts[i].getCondition();
            if (cond != null && WeatherRecord.severeConditions.contains(cond)) {
                this.severe = true;
                break;
            }
        }
        return this.severe;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTemp() {
        return this.temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindChill() {
        return this.windChill;
    }

    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return this.visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getPressureState() {
        return this.pressureState;
    }

    public void setPressureState(String pressureState) {
        this.pressureState = pressureState;
    }

    public String getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return this.sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public WeatherCondition getCondition() {
        return this.condition;
    }

    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }

    public WeatherForecast[] getForecasts() {
        return this.forecasts;
    }

    public void setForecasts(WeatherForecast[] forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WeatherRecord:");
        sb.append(" city-" + this.city);
        sb.append(" region-" + this.region);
        sb.append(" country-" + this.country);
        if (this.date != null) {
            sb.append(" date-" + this.date);
        } else {
            sb.append(" date-null");
        }
        sb.append(" temp-" + this.temp);
        sb.append(" windDirection-" + this.windDirection);
        sb.append(" windSpeed-" + this.windSpeed);
        sb.append(" windChill-" + this.windChill);
        sb.append(" humidity-" + this.humidity);
        sb.append(" visibility-" + this.visibility);
        sb.append(" pressure-" + this.pressure);
        sb.append(" pressureState-" + this.pressureState);
        sb.append(" sunrise-" + this.sunrise);
        sb.append(" sunset-" + this.sunset);
        sb.append(" link-" + this.link);
        if (this.condition != null) {
            sb.append(" condition-" + this.condition.getDisplay());
        } else {
            sb.append(" condition-null");
        }
        if (this.forecasts != null) {
            for (WeatherForecast forecast : this.forecasts) {
                if (forecast != null) {
                    sb.append(" " + forecast.toString());
                } else {
                    sb.append(" forecast-null");
                }
            }
        } else {
            sb.append(" forecasts-null");
        }
        return sb.toString();
    }
}

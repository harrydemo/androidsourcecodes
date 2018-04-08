package com.msi.manning.weather.data;

import android.util.Log;

import com.msi.manning.weather.Constants;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Invoke Yahoo! Weather API and parse into WeatherRecord.
 * 
 * @see YWeatherHandler
 * 
 * @author charliecollins
 * 
 */
public class YWeatherFetcher {

    private static final String CLASSTAG = YWeatherFetcher.class.getSimpleName();
    private static final String QBASE = "http://weather.yahooapis.com/forecastrss?p=";

    private String query;
    private String zip;

    public YWeatherFetcher(String zip, boolean overrideSevere) {

        // validate location is a zip
        if (zip == null || zip.length() != 5 || !isNumeric(zip)) {
            return;
        }

        this.zip = zip;

        // build query
        this.query = YWeatherFetcher.QBASE + this.zip;
        // /Log.v(Constants.LOGTAG, " " + CLASSTAG + " query - " + query);
    }

    public YWeatherFetcher(String zip) {
        this(zip, false);
    }

    public WeatherRecord getWeather() {
        // /long start = System.currentTimeMillis();
        WeatherRecord r = new WeatherRecord();

        try {
            URL url = new URL(this.query);
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            YWeatherHandler handler = new YWeatherHandler();
            xr.setContentHandler(handler);
            xr.parse(new InputSource(url.openStream()));
            // after parsed, get record
            r = handler.getWeatherRecord();
            r.setOverrideSevere(true); // override severe for dev/testing
        } catch (Exception e) {
            Log.e(Constants.LOGTAG, " " + YWeatherFetcher.CLASSTAG, e);
        }

        // /long duration = (System.currentTimeMillis() - start) / 1000;
        // /Log.v(Constants.LOGTAG, " " + CLASSTAG + " call duration - " + duration);
        // /Log.v(Constants.LOGTAG, " " + CLASSTAG + " WeatherReport = " + r);
        return r;
    }

    private boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public static WeatherRecord getMockRecord() {
        WeatherRecord r = new WeatherRecord();
        r.setCity("Crested Butte");
        r.setCondition(WeatherCondition.SUNNY);
        r.setCountry("US");
        r.setDate("03-08-2008");
        WeatherForecast[] forecasts = new WeatherForecast[2];
        WeatherForecast w1 = new WeatherForecast();
        w1.setCondition(WeatherCondition.HEAVY_SNOW_WINDY);
        w1.setDate("03-09-2008");
        w1.setDay("Sun");
        w1.setHigh(22);
        w1.setLow(3);
        WeatherForecast w2 = new WeatherForecast();
        w2.setCondition(WeatherCondition.FAIR_DAY);
        w2.setDate("03-10-2008");
        w2.setDay("Mon");
        w2.setHigh(28);
        w2.setLow(5);
        forecasts[0] = w1;
        forecasts[1] = w2;
        r.setForecasts(forecasts);
        r.setHumidity(100);
        r.setLink("link");
        r.setPressure(30.4);
        r.setPressureState(WeatherRecord.PRESSURE_RISING);
        r.setRegion("CO");
        r.setSunrise("6:27 am");
        r.setSunset("6:11 pm");
        r.setTemp(11);
        r.setVisibility(250);
        r.setWindChill("-12");
        r.setWindDirection("NNE");
        r.setWindSpeed(23);
        return r;
    }

}

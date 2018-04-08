package com.msi.manning.weather.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler impl for Yahoo! Weather API and WeatherRecord bean.
 * 
 * @author charliecollins
 * 
 */
public class YWeatherHandler extends DefaultHandler {

    // private static final String CLASSTAG = YWeatherHandler.class.getSimpleName();

    private static final String YLOC = "location";
    private static final String YWIND = "wind";
    private static final String YATMO = "atmosphere";
    private static final String YASTRO = "astronomy";
    private static final String YCOND = "condition";
    private static final String YFCAST = "forecast";

    private int forecastCount;
    private WeatherRecord weatherRecord;

    public YWeatherHandler() {
        this.weatherRecord = new WeatherRecord();
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(YWeatherHandler.YLOC)) {
            this.weatherRecord.setCity(getAttributeValue("city", atts));
            this.weatherRecord.setRegion(getAttributeValue("region", atts));
            this.weatherRecord.setCountry(getAttributeValue("country", atts));
        }

        if (localName.equals(YWeatherHandler.YWIND)) {
            this.weatherRecord.setWindChill(getAttributeValue("chill", atts));
            int windDirectionDegrees = Integer.parseInt(getAttributeValue("direction", atts));
            this.weatherRecord.setWindDirection(convertDirection(windDirectionDegrees));
            this.weatherRecord.setWindSpeed(Integer.parseInt(getAttributeValue("speed", atts)));
        }

        if (localName.equals(YWeatherHandler.YATMO)) {
            this.weatherRecord.setHumidity(Integer.parseInt(getAttributeValue("humidity", atts)));
            this.weatherRecord.setVisibility(Integer.parseInt(getAttributeValue("visibility", atts)));
            this.weatherRecord.setPressure(Double.parseDouble(getAttributeValue("pressure", atts)));
            String pressureState = getAttributeValue("rising", atts);
            if (pressureState.equals("0")) {
                this.weatherRecord.setPressureState(WeatherRecord.PRESSURE_STEADY);
            } else if (pressureState.equals("1")) {
                this.weatherRecord.setPressureState(WeatherRecord.PRESSURE_FALLING);
            } else if (pressureState.equals("2")) {
                this.weatherRecord.setPressureState(WeatherRecord.PRESSURE_RISING);
            }
        }

        if (localName.equals(YWeatherHandler.YASTRO)) {
            this.weatherRecord.setSunrise(getAttributeValue("sunrise", atts));
            this.weatherRecord.setSunset(getAttributeValue("sunset", atts));
        }

        if (localName.equals(YWeatherHandler.YCOND)) {
            this.weatherRecord.setTemp(Integer.parseInt(getAttributeValue("temp", atts)));
            int code = Integer.parseInt(getAttributeValue("code", atts));
            WeatherCondition cond = WeatherCondition.getWeatherCondition(code);
            this.weatherRecord.setCondition(cond);
            this.weatherRecord.setDate(getAttributeValue("date", atts));
        }

        if (localName.equals(YWeatherHandler.YFCAST)) {
            if (this.forecastCount < 2) {
                WeatherForecast forecast = new WeatherForecast();
                forecast.setDate(getAttributeValue("date", atts));
                forecast.setDay(getAttributeValue("day", atts));
                forecast.setHigh(Integer.parseInt(getAttributeValue("high", atts)));
                forecast.setLow(Integer.parseInt(getAttributeValue("low", atts)));
                int code = Integer.parseInt(getAttributeValue("code", atts));
                WeatherCondition cond = WeatherCondition.getWeatherCondition(code);
                forecast.setCondition(cond);
                this.weatherRecord.getForecasts()[this.forecastCount] = forecast;
            }
            this.forecastCount++;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    private String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }

    private String convertDirection(int d) {
        String result = "";
        if (d >= 348.75 && d < 11.25) {
            result = "N";
        } else if (d >= 11.25 && d < 33.75) {
            result = "NNE";
        } else if (d >= 33.75 && d < 56.25) {
            result = "NE";
        } else if (d >= 56.25 && d < 78.75) {
            result = "ENE";
        } else if (d >= 78.75 && d < 101.25) {
            result = "E";
        } else if (d >= 101.25 && d < 123.75) {
            result = "ESE";
        } else if (d >= 123.75 && d < 146.25) {
            result = "SE";
        } else if (d >= 146.25 && d < 168.75) {
            result = "SSE";
        } else if (d >= 168.75 && d < 191.25) {
            result = "S";
        } else if (d >= 191.25 && d < 213.75) {
            result = "SSW";
        } else if (d >= 213.75 && d < 236.25) {
            result = "SW";
        } else if (d >= 236.25 && d < 258.75) {
            result = "WSW";
        } else if (d >= 258.75 && d < 281.25) {
            result = "W";
        } else if (d >= 281.25 && d < 303.75) {
            result = "WNW";
        } else if (d >= 303.75 && d < 326.25) {
            result = "NW";
        } else if (d >= 326.25 && d < 348.75) {
            result = "NNW";
        }
        return result;
    }

    public WeatherRecord getWeatherRecord() {
        return this.weatherRecord;
    }

    public void setWeatherRecord(WeatherRecord weatherRecord) {
        this.weatherRecord = weatherRecord;
    }
}

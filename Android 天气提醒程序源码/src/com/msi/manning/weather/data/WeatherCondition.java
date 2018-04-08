package com.msi.manning.weather.data;

/**
 * Enum to represent weather condtion states coming from Yahoo Weather API.
 * 
 * Image: http://l.yimg.com/us.yimg.com/i/us/we/52/[CODE].gif"
 * 
 * @author charliecollins
 * 
 */
public enum WeatherCondition {
    BLOWING_SNOW("Blowing Snow", 15), BLUSTERY("Blustery", 23), CLEAR_NIGHT("Clear (night)", 31), CLOUDY("Cloudy", 26), COLD(
        "Cold", 25), DRIZZLE("Drizzle", 9), DUST("Dust", 19), FAIR_DAY("Fair (day)", 34), FAIR_NIGHT("Fair (night)", 33), FOGGY(
        "Foggy", 20), FREEZING_DRIZZLE("Freezing Drizzle", 8), FREEZING_RAIN("Freezing Rain", 10), HAIL("Hail", 17), HAZE(
        "Haze", 21), HEAVY_SNOW("Heavy Snow", 41), HEAVY_SNOW_WINDY("Heavy Snow (windy)", 43), HOT("Hot", 36), HURRICANE(
        "Hurricane", 2), ISOLATED_THUNDERSHOWERS("Isolated Thundershowers", 47), ISOLATED_THUNDERSTORMS(
        "Isolated Thunderstorms", 37), LIGHT_SNOW_SHOWERS("Light Snow Showers", 14), MIXED_RAIN_AND_HAIL(
        "Mixed Rain and Hail", 35), MIXED_RAIN_AND_SLEET("Mixed Rain and Sleet", 6), MIXED_RAIN_AND_SNOW(
        "Mixed Rain and Snow", 5), MIXED_SNOW_AND_SLEET("Mixed Snow and Sleet", 7), MOSTLY_CLOUDY_DAY(
        "Mostly Cloudy (day)", 28), MOSTLY_CLOUDY_NIGHT("Mostly Cloudy (night)", 27), NOT_AVAILABLE("Not Available",
        3200), PARTLY_CLOUDY("Partly Cloudy", 44), PARTLY_CLOUDY_DAY("Partly Cloudy (day)", 30), PARTLY_CLOUDY_NIGHT(
        "Partly Cloudy (night)", 29), SCATTERED_SHOWERS("Scattered Showers", 40), SCATTERED_SNOW_SHOWERS(
        "Scattered Snow Showers", 42), SCATTERED_THUNDERSTORMS_HEAVY("Scattered Thunderstorms (heavy)", 39), SCATTERED_THUNDERSTORMS_LIGHT(
        "Scattered Thunderstorms (light)", 38), SEVERE_THUNDERSTORMS("Severe Thunderstorms", 3), SNOW_SHOWERS(
        "Snow Showers", 46), SNOW_FLURRIES("Snow Flurries", 13), SHOWERS_HEAVY("Heavy Showers", 12), SHOWERS_LIGHT(
        "Light Showers", 11), SLEET("Sleet", 18), SMOKY("Smoky", 22), SNOW("Snow", 16), SUNNY("Sunny", 32), THUNDERSTORMS(
        "Thunderstorms", 4), THUNDERSHOWERS("Thundershowers", 45), TORNADO("Tornado", 0), TROPICAL_STORM(
        "Tropical Storm", 1), WINDY("Windy", 24);

    private static final String IMAGE_PREFIX = "http://l.yimg.com/us.yimg.com/i/us/we/52/";
    private String display;
    private int id;
    private String netImageLink;

    WeatherCondition(String display, int id) {
        this.display = display;
        this.id = id;
        this.netImageLink = WeatherCondition.IMAGE_PREFIX + this.id + ".gif";
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNetImageLink() {
        return this.netImageLink;
    }

    public void setNetImageLink(String imageLink) {
        this.netImageLink = imageLink;
    }

    public static WeatherCondition getWeatherCondition(int code) {

        WeatherCondition cond = WeatherCondition.NOT_AVAILABLE;
        switch (code) {
            case 0:
                cond = WeatherCondition.TORNADO;
                break;
            case 1:
                cond = WeatherCondition.TROPICAL_STORM;
                break;
            case 2:
                cond = WeatherCondition.HURRICANE;
                break;
            case 3:
                cond = WeatherCondition.SEVERE_THUNDERSTORMS;
                break;
            case 4:
                cond = WeatherCondition.THUNDERSTORMS;
                break;
            case 5:
                cond = WeatherCondition.MIXED_RAIN_AND_SNOW;
                break;
            case 6:
                cond = WeatherCondition.MIXED_RAIN_AND_SLEET;
                break;
            case 7:
                cond = WeatherCondition.MIXED_SNOW_AND_SLEET;
                break;
            case 8:
                cond = WeatherCondition.FREEZING_DRIZZLE;
                break;
            case 9:
                cond = WeatherCondition.DRIZZLE;
                break;
            case 10:
                cond = WeatherCondition.FREEZING_RAIN;
                break;
            case 11:
                cond = WeatherCondition.SHOWERS_LIGHT;
                break;
            case 12:
                cond = WeatherCondition.SHOWERS_HEAVY;
                break;
            case 13:
                cond = WeatherCondition.SNOW_FLURRIES;
                break;
            case 14:
                cond = WeatherCondition.LIGHT_SNOW_SHOWERS;
                break;
            case 15:
                cond = WeatherCondition.BLOWING_SNOW;
                break;
            case 16:
                cond = WeatherCondition.SNOW;
                break;
            case 17:
                cond = WeatherCondition.HAIL;
                break;
            case 18:
                cond = WeatherCondition.SLEET;
                break;
            case 19:
                cond = WeatherCondition.DUST;
                break;
            case 20:
                cond = WeatherCondition.FOGGY;
                break;
            case 21:
                cond = WeatherCondition.HAZE;
                break;
            case 22:
                cond = WeatherCondition.SMOKY;
                break;
            case 23:
                cond = WeatherCondition.BLUSTERY;
                break;
            case 24:
                cond = WeatherCondition.WINDY;
                break;
            case 25:
                cond = WeatherCondition.COLD;
                break;
            case 26:
                cond = WeatherCondition.CLOUDY;
                break;
            case 27:
                cond = WeatherCondition.MOSTLY_CLOUDY_NIGHT;
                break;
            case 28:
                cond = WeatherCondition.MOSTLY_CLOUDY_DAY;
                break;
            case 29:
                cond = WeatherCondition.PARTLY_CLOUDY_NIGHT;
                break;
            case 30:
                cond = WeatherCondition.PARTLY_CLOUDY_DAY;
                break;
            case 31:
                cond = WeatherCondition.CLEAR_NIGHT;
                break;
            case 32:
                cond = WeatherCondition.SUNNY;
                break;
            case 33:
                cond = WeatherCondition.FAIR_NIGHT;
                break;
            case 34:
                cond = WeatherCondition.FAIR_DAY;
                break;
            case 35:
                cond = WeatherCondition.MIXED_RAIN_AND_HAIL;
                break;
            case 36:
                cond = WeatherCondition.HOT;
                break;
            case 37:
                cond = WeatherCondition.ISOLATED_THUNDERSTORMS;
                break;
            case 38:
                cond = WeatherCondition.SCATTERED_THUNDERSTORMS_LIGHT;
                break;
            case 39:
                cond = WeatherCondition.SCATTERED_THUNDERSTORMS_HEAVY;
                break;
            case 40:
                cond = WeatherCondition.SCATTERED_SHOWERS;
                break;
            case 41:
                cond = WeatherCondition.HEAVY_SNOW;
                break;
            case 42:
                cond = WeatherCondition.SCATTERED_SNOW_SHOWERS;
                break;
            case 43:
                cond = WeatherCondition.HEAVY_SNOW_WINDY;
                break;
            case 44:
                cond = WeatherCondition.PARTLY_CLOUDY;
                break;
            case 45:
                cond = WeatherCondition.THUNDERSHOWERS;
                break;
            case 46:
                cond = WeatherCondition.SNOW_SHOWERS;
                break;
            case 47:
                cond = WeatherCondition.ISOLATED_THUNDERSHOWERS;
                break;
            default:
                cond = WeatherCondition.NOT_AVAILABLE;
                break;
        }
        return cond;
    }
}

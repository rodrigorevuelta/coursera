
package com.example.weatherserviceapp.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonWeather {

	protected static final String WEATHER_JSON = "weather"; 
	protected static final String MAIN_JSON = "main";
	protected static final String WIND_JSON = "wind";
	protected static final String SYS_JSON = "sys";
	
	protected static final String TEMP_JSON = "temp";
	protected static final String TEMPMIN_JSON = "temp_min";
	protected static final String TEMPMAX_JSON = "temp_max";
	protected static final String HUMIDITY_JSON = "humidity";
	
	protected static final String WEATHER_MAIN_JSON = "main";
	protected static final String WEATHER_DESC_JSON = "description";
	protected static final String WEATHER_ICON_JSON = "icon";
	
	protected static final String WIND_SPEED_JSON = "speed";
	protected static final String WIND_DEG_JSON = "deg";
	
	protected static final String SYS_SUNRISE_JSON = "sunrise";
	protected static final String SYS_SUNSET_JSON = "sunset";
	protected static final String SYS_COUNTRY_JSON = "country";
	
	protected static final String NAME_JSON = "name";
	
	
    private Coord coord;
    private Sys sys;
    private List<Weather> weather = new ArrayList<Weather>();
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Integer dt;
    private Integer id;
    private String name;
    private Integer cod;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The coord
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     * 
     * @param coord
     *     The coord
     */
    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public JsonWeather withCoord(Coord coord) {
        this.coord = coord;
        return this;
    }

    /**
     * 
     * @return
     *     The sys
     */
    public Sys getSys() {
        return sys;
    }

    /**
     * 
     * @param sys
     *     The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public JsonWeather withSys(Sys sys) {
        this.sys = sys;
        return this;
    }

    /**
     * 
     * @return
     *     The weather
     */
    public List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public JsonWeather withWeather(List<Weather> weather) {
        this.weather = weather;
        return this;
    }

    /**
     * 
     * @return
     *     The base
     */
    public String getBase() {
        return base;
    }

    /**
     * 
     * @param base
     *     The base
     */
    public void setBase(String base) {
        this.base = base;
    }

    public JsonWeather withBase(String base) {
        this.base = base;
        return this;
    }

    /**
     * 
     * @return
     *     The main
     */
    public Main getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    public JsonWeather withMain(Main main) {
        this.main = main;
        return this;
    }

    /**
     * 
     * @return
     *     The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * 
     * @param wind
     *     The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public JsonWeather withWind(Wind wind) {
        this.wind = wind;
        return this;
    }

    /**
     * 
     * @return
     *     The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     * 
     * @param clouds
     *     The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public JsonWeather withClouds(Clouds clouds) {
        this.clouds = clouds;
        return this;
    }

    /**
     * 
     * @return
     *     The dt
     */
    public Integer getDt() {
        return dt;
    }

    /**
     * 
     * @param dt
     *     The dt
     */
    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public JsonWeather withDt(Integer dt) {
        this.dt = dt;
        return this;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public JsonWeather withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public JsonWeather withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The cod
     */
    public Integer getCod() {
        return cod;
    }

    /**
     * 
     * @param cod
     *     The cod
     */
    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public JsonWeather withCod(Integer cod) {
        this.cod = cod;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public JsonWeather withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}

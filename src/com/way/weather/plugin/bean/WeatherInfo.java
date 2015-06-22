package com.way.weather.plugin.bean;


public class WeatherInfo {

	private int isNewDatas;
	private RealTime realTime;
	private Forecast forecast;
	private AQI aqi;
	private Index index;
	private Alerts alerts;

	public WeatherInfo() {
		super();
	}

	public WeatherInfo(RealTime realTime, Forecast forecast, AQI aqi,
			Index index, Alerts alerts) {
		super();
		this.realTime = realTime;
		this.forecast = forecast;
		this.aqi = aqi;
		this.index = index;
		this.alerts = alerts;
	}

	public RealTime getRealTime() {
		return realTime;
	}

	public void setRealTime(RealTime realTime) {
		this.realTime = realTime;
	}

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public AQI getAqi() {
		return aqi;
	}

	public void setAqi(AQI aqi) {
		this.aqi = aqi;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public Alerts getAlerts() {
		return alerts;
	}

	public void setAlerts(Alerts alerts) {
		this.alerts = alerts;
	}

	public boolean getIsNewDatas() {
		return isNewDatas == 0 ? false : true;
	}

	public void setIsNewDatas(int isNewDatas) {
		this.isNewDatas = isNewDatas;
	}

	@Override
	public String toString() {
		return "WeatherInfo [isNewDatas=" + isNewDatas + ", realTime="
				+ realTime + ", forecast=" + forecast + ", aqi=" + aqi
				+ ", index=" + index + ", alerts=" + alerts + "]";
	}

}

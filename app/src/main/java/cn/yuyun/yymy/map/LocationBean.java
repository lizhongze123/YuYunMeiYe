package cn.yuyun.yymy.map;

import java.io.Serializable;



public class LocationBean implements Serializable{

	private double lng;
	private double lat;
	private String title;
	private String content;

	public LocationBean(double lng, double lat,String title,String content){
		this.lng = lng;
		this.lat = lat;
		this.title = title;
		this.content = content;
	}

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}

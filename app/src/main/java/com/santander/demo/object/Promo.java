package com.santander.demo.object;

import java.math.BigDecimal;


public class Promo {
	private String url, title, description, longitud, latitud;

	public Promo() {

	}

	public Promo(String url, String title, String description, String longitud,
                 String latitud) {

		this.url = url;
		this.description = description;
		this.title = title;
		this.longitud = longitud;
		this.latitud = latitud;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}



}


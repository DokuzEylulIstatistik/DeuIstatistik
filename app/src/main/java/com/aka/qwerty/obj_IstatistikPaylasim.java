package com.aka.qwerty;

import java.io.Serializable;

public class obj_IstatistikPaylasim implements Serializable {

	private static final long serialVersionUID = 1L;

	private int ist_ID = 0;
	private String ist_konu = "ist_konu";
	private String ist_yazi = "ist_yazi";
	private String ist_tag = "ist_tag";
	private String ist_tarih = "ist_tarih";
	private int ist_okunmasayisi = 0;

	public int getIst_ID() {
		return ist_ID;
	}

	public void setIst_ID(int ist_ID) {
		this.ist_ID = ist_ID;
	}

	public String getIst_konu() {
		return ist_konu;
	}

	public void setIst_konu(String ist_konu) {
		this.ist_konu = ist_konu;
	}

	public String getIst_yazi() {
		return ist_yazi;
	}

	public void setIst_yazi(String ist_yazi) {
		this.ist_yazi = ist_yazi;
	}

	public String getIst_tag() {
		return ist_tag;
	}

	public void setIst_tag(String ist_tag) {
		this.ist_tag = ist_tag;
	}

	public String getIst_tarih() {
		return ist_tarih;
	}

	public void setIst_tarih(String ist_tarih) {
		this.ist_tarih = ist_tarih;
	}

	public int getIst_okunmasayisi() {
		return ist_okunmasayisi;
	}

	public void setIst_okunmasayisi(int ist_okunmasayisi) {
		this.ist_okunmasayisi = ist_okunmasayisi;
	}
}

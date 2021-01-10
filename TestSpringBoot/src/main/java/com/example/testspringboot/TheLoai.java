package com.example.testspringboot;

public class TheLoai {
	private int idTheLoai;
	private String tenTheLoai;
	
	public TheLoai() {
		super();
	}
	public TheLoai(int idTheLoai, String tenTheLoai) {
		super();
		this.idTheLoai = idTheLoai;
		this.tenTheLoai = tenTheLoai;
	}
	public int getIdTheLoai() {
		return idTheLoai;
	}
	public void setIdTheLoai(int idTheLoai) {
		this.idTheLoai = idTheLoai;
	}
	public String getTenTheLoai() {
		return tenTheLoai;
	}
	public void setTenTheLoai(String tenTheLoai) {
		this.tenTheLoai = tenTheLoai;
	}
	
	
}

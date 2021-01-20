package com.example.testspringboot;

import org.springframework.data.annotation.Transient;

public class Sach {
	private int idSach;
	private int idTheLoai;
	private String img;
	private String tenSach;
	private String tenTacGia;
	@Override
	public String toString() {
		return "Sach [id=" + idSach + ", idTheLoai=" + idTheLoai + ", img=" + img + ", tenSach=" + tenSach + ", tenTacGia="
				+ tenTacGia + ", fileEbook=" + fileEbook + "]";
	}


	private String fileEbook;
	
	
	public Sach() {
		super();
	}



	public Sach(int idSach, int idTheLoai, String img, String tenSach, String tenTacGia, String fileEbook) {
		super();
		this.idSach = idSach;
		this.idTheLoai = idTheLoai;
		this.img = img;
		this.tenSach = tenSach;
		this.tenTacGia = tenTacGia;
		this.fileEbook = fileEbook;
	}


	public int getIdTheLoai() {
		return idTheLoai;
	}



	public void setIdTheLoai(int idTheLoai) {
		this.idTheLoai = idTheLoai;
	}



	public String getTenSach() {
		return tenSach;
	}



	public void setTenSach(String tenSach) {
		this.tenSach = tenSach;
	}



	public String getTenTacGia() {
		return tenTacGia;
	}



	public void setTenTacGia(String tenTacGia) {
		this.tenTacGia = tenTacGia;
	}



	public String getFileEbook() {
		return fileEbook;
	}



	public void setFileEbook(String fileEbook) {
		this.fileEbook = fileEbook;
	}



	public int getIdSach() {
		return idSach;
	}



	public void setIdSach(int idSach) {
		this.idSach = idSach;
	}



	public String getImg() {
		return img;
	}



	public void setImg(String img) {
		this.img = img;
	}
	
	
	public String getImagePath() {
		if(img==null) return null;
		return "/images/"+img;
	}
	
	public String getEbookPath() {
		if(fileEbook==null) return null;
		return "/pdf/"+fileEbook;
	}
	
	
	public String getCheckEbook() {
		if(fileEbook==null) return "Chưa có";
		return "Đã tồn tại";
	}
}

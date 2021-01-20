package com.example.testspringboot.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.testspringboot.Sach;
import com.example.testspringboot.TheLoai;
import com.example.testspringboot.User;

@Repository // dùng để truy cập và thao tác csdl
public class ShareBookDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public String getUser(String username, String password)
	{
		try {
			String sql = "select username from account where username= ? and password = ? ";
			String user= (String)jdbcTemplate.queryForObject(sql
					, new Object[] {username,password}, String.class);
			return user;
		}catch(EmptyResultDataAccessException e)
		{
			return "Khong tim thay";
		}
	}
	
	public List<TheLoai> getAllTheLoai()
	{
		List<TheLoai> theLoais = new ArrayList<TheLoai>();
		String sql = "select * from theloai";
		theLoais = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TheLoai.class));
		return theLoais;
		
	}
	
	public int addSachh(Sach sach)
	{
		String sql = "insert into dssach(idtheloai, img, tensach, tentacgia, fileebook) values(?,?,?,?,?)";
		return jdbcTemplate.update(sql,sach.getIdTheLoai(),sach.getImg(),sach.getTenSach(),sach.getTenTacGia(),sach.getFileEbook());	
	}
	
	public int addTheLoai(String tenTheLoai)
	{
		String sql = "insert into theloai(tentheloai) values(?)";
		return jdbcTemplate.update(sql,tenTheLoai);	
	}
	
	public int suaTheLoai(TheLoai theLoai)
	{
		String sql= "update theloai set tentheloai=? where idtheloai=?";
		return jdbcTemplate.update(sql, theLoai.getTenTheLoai(), theLoai.getIdTheLoai());
	}
	
	public int xoaTheLoai(int idTheLoai)
	{
		String sql="delete from theloai where idtheloai=?";
		return jdbcTemplate.update(sql, idTheLoai);
	}
	
	public List<Sach> getAllSach(){
		List<Sach> sachs = new ArrayList<Sach>();
		String sql = "select * from dssach";
		sachs = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Sach.class));
		return sachs;
	}
	
	public TheLoai getTheLoai(int idTheLoai)
	{
		try {
//			System.out.println("hi");
//			Sach sachs = new Sach();
			TheLoai theLoai = new TheLoai();
			String sql = "select * from theloai where idtheloai=?";
//			return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Sach.class),idSach);
//			Map<String, Sach> sachs = (Sach)jdbcTemplate.queryForMap(sql, idSach);
			theLoai = (TheLoai) jdbcTemplate.queryForObject(sql, new Object[] {idTheLoai} ,new BeanPropertyRowMapper(TheLoai.class));
//			System.out.println("hi");
			return theLoai;
		}catch(EmptyResultDataAccessException e)
		{
			TheLoai theLoai = new TheLoai();
			return theLoai;
		}
	}

	
	public Sach getSach(int idSach){
		Sach sachs = new Sach();
		try {
			String sql = "select * from dssach where idsach=?";
//			return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Sach.class),idSach);
//			Map<String, Sach> sachs = (Sach)jdbcTemplate.queryForMap(sql, idSach);
			sachs = (Sach) jdbcTemplate.queryForObject(sql, new Object[] {idSach} ,new BeanPropertyRowMapper(Sach.class));
			return sachs;
		}catch(EmptyResultDataAccessException e)
		{
			return sachs =  null;
		}
	}
	
	public int suaSach(Sach sach)
	{
		String sql = "update dssach set idtheloai=?, tensach=?, img=?, tentacgia=?, fileebook=? where idsach=?";
		return jdbcTemplate.update(sql,sach.getIdTheLoai(),sach.getTenSach(),sach.getImg(),
				sach.getTenTacGia(),sach.getFileEbook(),sach.getIdSach());
	}
	
	public int xoaSach(int idSach) {
		String sql = "delete from dssach where idsach=?";
		return jdbcTemplate.update(sql,idSach);
	}
	
	public int getSoLuongSach() {
		String sql = "select count(*) from dssach";
		int soLuong= jdbcTemplate.queryForObject(sql
				, Integer.class);
		return soLuong;
	}
	
	public int getSoLuongTheLoai() {
		String sql = "select count(*) from theloai";
		int soLuong= jdbcTemplate.queryForObject(sql
				, Integer.class);
		return soLuong;
	}
	
	// tim kiem the loai
	public int getSoLuongTheLoai(String tenTheLoai)
	{
		String sql;
		if(tenTheLoai==null || tenTheLoai.equals("null"))
		{
			sql = "select count(*) from theloai";
		}
		else
		{
			sql = "select count(*) from theloai where tentheloai like '%"+tenTheLoai+"%'";
		}
		int soLuong= jdbcTemplate.queryForObject(sql, Integer.class);
		return soLuong;
	}
	
	public List<TheLoai> searchTheLoai(String tenTheLoai, int viTri, int pageSize)
	{
		String sql;
		if(tenTheLoai==null || tenTheLoai.equals("null"))
		{
			sql = "select * from theloai limit "+viTri+","+pageSize;
		}
		else
		{
			sql = "select * from theloai where tentheloai like '%"+tenTheLoai+"%' limit "+viTri+","+pageSize;
		}
		List<TheLoai> theLoai = new ArrayList<TheLoai>();
		theLoai = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TheLoai.class));
		return theLoai;
	}
	
	// tim kiem sach
	public int getSoLuongSach(String idTheLoai, String tenSach)
	{
		String sql;
		if(idTheLoai==null && tenSach == null || idTheLoai.equals("null") && tenSach.equals("null"))
		{
			sql = "select count(*) from dssach";
		}
		else
		{
			if(idTheLoai.equals("0"))
			{
				sql = "select count(*) from dssach where tensach like '%"+tenSach+"%'";
			}
			else
			{
				sql = "select count(*) from dssach where idtheloai like '%"+idTheLoai+"%' and tensach like '%"+tenSach+"%'";
			}
		}
		int soLuong= jdbcTemplate.queryForObject(sql, Integer.class);
		return soLuong;
	}
	
	public List<Sach> searchSach(String idTheLoai, String tenSach, int viTri, int pageSize){
		String sql;
		if(idTheLoai==null && tenSach == null || idTheLoai.equals("null") && tenSach.equals("null"))
		{
			sql = "select * from dssach limit "+viTri+","+pageSize;
		}
		else
		{
			if(idTheLoai.equals("0"))
			{
				sql = "select * from dssach where tensach like '%"+tenSach+"%' limit "+viTri+","+pageSize;
			}
			else
			{
				sql = "select * from dssach where idtheloai like '%"+idTheLoai+"%' and tensach like '%"+tenSach+"%' limit "+viTri+","+pageSize;
			}
		}
		List<Sach> sach = new ArrayList<Sach>();
		sach = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Sach.class));
		return sach;
	}
}

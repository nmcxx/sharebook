package com.example.testspringboot.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testspringboot.Sach;
import com.example.testspringboot.TheLoai;
import com.example.testspringboot.repository.ShareBookDAO;
import com.google.common.io.Files;

@Controller
public class AdminController {

	@Autowired
	ShareBookDAO userRepository; 
	
	@GetMapping(value="/dashboard")
	public String showDashBoard(HttpSession session, Model model) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		int soLuongSach = userRepository.getSoLuongSach();
		int soLuongTheLoai = userRepository.getSoLuongTheLoai();
		model.addAttribute("slsach", soLuongSach);
		model.addAttribute("sltheloai", soLuongTheLoai);
		return "dashboard";
	}
	
	// Quan ly sach 
	@GetMapping(value="/dashboard/quanlysach")
	public String showBookManagement(@RequestParam ( value="page", required = false) String strPageNo,
			@RequestParam(value="theloai", required = false) String strTheLoai,
			@RequestParam(value="tensach",  required = false) String tenSach,
			Model model,
			HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		int pageNo;
		try {
		pageNo = Integer.parseInt(strPageNo);
		}
		catch (NumberFormatException e) {
		// TODO: handle exception
		pageNo = 1;
		}
		//int pageNo = Integer.valueOf(strPageNo, 1);
		//int theLoai = Integer.valueOf(strTheLoai, 1);
		System.out.println(pageNo+", "+strTheLoai+", "+tenSach);
		
		int pageSize = 5;
//		double a = (double) page
		int totalPage = (int)(Math.ceil(userRepository.getSoLuongSach(strTheLoai, tenSach)/(double)pageSize));
		int viTri = pageNo*pageSize-pageSize;
		
		System.out.println("tong so trang"+totalPage+", "+Math.ceil(10.0/3));
		List<Sach> sach = new ArrayList<Sach>();
		sach = userRepository.searchSach(strTheLoai, tenSach, viTri, pageSize);
		
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("theLoai", strTheLoai);
		model.addAttribute("tenSach", tenSach);
		model.addAttribute("listSach", sach);
		System.out.println(totalPage+", "+viTri+", "+strTheLoai+", "+tenSach+", dân");
		
		return "quanlysach";
	}
	
	@PostMapping(value="/dashboard/quanlysach")
	public String searchBookManagement(@RequestParam("list-search") int id,
						@RequestParam("search") String tenSach) {
		
		System.out.println(tenSach);
		tenSach = URLEncoder.encode(tenSach);
		return "redirect:/dashboard/quanlysach?page=1&theloai="+id+"&tensach="+tenSach;
	}
	
	
	@GetMapping(value="/dashboard/quanlysach/add")
	public String showAddBook(Model model, HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
		return "themsach";
	}
	
	@GetMapping(value="/dashboard/quanlysach/edit")
	public String showEditBook(@RequestParam(value="id") int id,
			Model model, HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		Sach sach = new Sach();
		sach = userRepository.getSach(id);
				
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		model.addAttribute("listSach",sach);
		
		
		return "suasach";
	}
	
	@PostMapping(value="/dashboard/quanlysach/add")
	public String addBook(@ModelAttribute(name="saveSachForm") Sach sach, 
			@RequestParam("list-search") int id, // get value tu list search
			RedirectAttributes ra, 
			@RequestParam("files") MultipartFile[] file) throws IOException
	{
		System.out.println(""+id);
		boolean flag=true;
		sach.setIdTheLoai(id);
		for(MultipartFile file2 : file) {
		String fileName = StringUtils.cleanPath(file2.getOriginalFilename());
		String fileExtension = Files.getFileExtension(fileName);
		System.out.println(Files.getFileExtension(fileName));
		if(fileExtension.equals("pdf") || fileExtension.equals("jpg") || fileExtension.equals("png"))
		{
			String uploadDir ="";
			if(fileExtension.equals("jpg")  || fileExtension.equals("png"))
			{
				sach.setImg(fileName);
				uploadDir ="./images/";
			}
			else 
			{
				sach.setFileEbook(fileName);
				uploadDir ="./pdf/";
			}
			Path uploadPath = Paths.get(uploadDir);
			
			if(!java.nio.file.Files.exists(uploadPath))
			{
				java.nio.file.Files.createDirectories(uploadPath);
			}
			
			try(InputStream inputStream = file2.getInputStream())
			{
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toFile().getAbsolutePath());
				java.nio.file.Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(IOException e)
			{
				throw new IOException("Khong the save file upload: "+fileName);
			}
		}
		else
		{
			System.out.println("File khong duoc ho tro: "+fileName);
			flag=false;
			break;
		}
		}
		System.out.println(sach.toString());
		if(flag==true && sach.getImg()!=null && sach.getFileEbook()!=null)
		{
			userRepository.addSachh(sach);
			ra.addFlashAttribute("message","Thêm thành công");
		}
		else
		{
			ra.addFlashAttribute("message","Thêm thất bại");
		}
		return "redirect:/dashboard/quanlysach/add";
		
//		return "themsach";
	}
	
	@PostMapping(value="/dashboard/quanlysach/edit")
	public String editBook(@ModelAttribute(name="editSachForm") Sach sach, 
			@RequestParam("list-search") int id, // get value tu list search
			RedirectAttributes ra, 
			@RequestParam("files") MultipartFile[] file) throws IOException
	{
		System.out.println(""+id);
		boolean flag=true;
		sach.setIdTheLoai(id);
		System.out.println(file.length);
		for(MultipartFile file2 : file) {
			System.out.println(file2.isEmpty());
			if(file2.isEmpty())
				continue;
//			System.out.println("ngan dau don");
		String fileName = StringUtils.cleanPath(file2.getOriginalFilename());
		String fileExtension = Files.getFileExtension(fileName);
		System.out.println(Files.getFileExtension(fileName));
		if(fileExtension.equals("pdf") || fileExtension.equals("jpg") || fileExtension.equals("png"))
		{
			String uploadDir ="";
			if(fileExtension.equals("jpg")  || fileExtension.equals("png"))
			{
				sach.setImg(fileName);
				uploadDir ="./images/";
			}
			else 
			{
				sach.setFileEbook(fileName);
				uploadDir ="./pdf/";
			}
			Path uploadPath = Paths.get(uploadDir);
			
			if(!java.nio.file.Files.exists(uploadPath))
			{
				java.nio.file.Files.createDirectories(uploadPath);
			}
			
			try(InputStream inputStream = file2.getInputStream())
			{
				Path filePath = uploadPath.resolve(fileName);
				System.out.println(filePath.toFile().getAbsolutePath());
				java.nio.file.Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(IOException e)
			{
				throw new IOException("Khong the save file upload: "+fileName);
			}
		}
		else
		{
			System.out.println("File khong duoc ho tro: "+fileName);
			flag=false;
			break;
		}
		}
		System.out.println(sach.toString());
		if(flag==true && sach.getImg()!=null && sach.getFileEbook()!=null)
		{
			userRepository.suaSach(sach);
			ra.addFlashAttribute("message","Sửa thành công");
		}
		else
		{
			ra.addFlashAttribute("message","Sửa thất bại");
		}
		return "redirect:/dashboard/quanlysach";
	}
	
	@GetMapping(value="/dashboard/quanlysach/delete")
	public String deleteBook(@RequestParam(value="id") int id,
			HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		userRepository.xoaSach(id);
		
		return "redirect:/dashboard/quanlysach";
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////
	/* ******* Quan ly the loai *******  */
	@GetMapping(value="/dashboard/quanlytheloai")
	public String showCategoryManagement(@RequestParam ( value="page", required = false) String strPageNo,
			@RequestParam(value="theloai", required = false) String strTheLoai,
			Model model, HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		int pageNo;
		try {
		pageNo = Integer.parseInt(strPageNo);
		}
		catch (NumberFormatException e) {
		// TODO: handle exception
		pageNo = 1;
		}
		//int pageNo = Integer.valueOf(strPageNo, 1);
		//int theLoai = Integer.valueOf(strTheLoai, 1);
		System.out.println(pageNo+", "+strTheLoai);
		
		int pageSize = 5;
//		double a = (double) page
		int totalPage = (int)(Math.ceil(userRepository.getSoLuongTheLoai(strTheLoai)/(double)pageSize));
//				userRepository.getSoLuongSach(strTheLoai, tenSach)/(double)pageSize));
		int viTri = pageNo*pageSize-pageSize;
		
		System.out.println("tong so trang"+totalPage+", "+Math.ceil(10.0/3));
		List<TheLoai> theLoai = new ArrayList<TheLoai>();
		theLoai = userRepository.searchTheLoai(strTheLoai, viTri, pageSize);
		
		model.addAttribute("totalPages", totalPage);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("theLoai", strTheLoai);
		model.addAttribute("listTheLoai", theLoai);
		System.out.println(totalPage+", "+viTri+", "+strTheLoai+", dân");
		
		return "quanlytheloai";
	}
	
	@PostMapping(value="/dashboard/quanlytheloai")
	public String searchCategoryManagement(@RequestParam(value="theloai") String tenTheLoai) {
		tenTheLoai = URLEncoder.encode(tenTheLoai);
		return "redirect:/dashboard/quanlytheloai?page=1&theloai="+tenTheLoai;
	}
	
	@GetMapping(value="/dashboard/quanlytheloai/add")
	public String showAddCategory(HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		return "themtheloai";
	}
	
	@GetMapping(value="/dashboard/quanlytheloai/edit")
	public String showEditCategory(@RequestParam(value="id") int id,
			Model model, HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		TheLoai theLoai = new TheLoai();
		theLoai = userRepository.getTheLoai(id);
		
		model.addAttribute("theloai", theLoai);
		return "suatheloai";
	}
	
	@PostMapping(value="/dashboard/quanlytheloai/add")
	public String addCategory(@RequestParam(value="tenTheLoai") String tenTheLoai,
			RedirectAttributes ra) {
		userRepository.addTheLoai(tenTheLoai);
		ra.addFlashAttribute("message", "Thêm thành công"); // gửi khi redirect trang
		
		return "redirect:/dashboard/quanlytheloai/add";
	}
	
	@PostMapping(value="/dashboard/quanlytheloai/edit")
	public String editCategory(@ModelAttribute(value="suaTheLoaiForm") TheLoai theLoai,
			RedirectAttributes ra) {
		userRepository.suaTheLoai(theLoai);
		ra.addFlashAttribute("message", "Sửa thành công");
		return "redirect:/dashboard/quanlytheloai";
	}
	
	@GetMapping(value="/dashboard/quanlytheloai/delete")
	public String deleteCategory(@RequestParam(value="id") int id,
			RedirectAttributes ra,
			HttpSession session) {
		if(session.getAttribute("userlogin")==null) return "redirect:/index";
		
		userRepository.xoaTheLoai(id);
		ra.addFlashAttribute("message","Xóa thành công");
		
		return "redirect:/dashboard/quanlytheloai";
	}
}

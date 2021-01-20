package com.example.testspringboot.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testspringboot.Sach;
import com.example.testspringboot.TheLoai;
import com.example.testspringboot.User;
import com.example.testspringboot.repository.ShareBookDAO;
//import com.example.testspringboot.repository.UserRepository;
import com.google.common.io.Files;

@Controller // Nơi tiếp nhận các request từ client và trả về chuỗi html để hiển thị lên trình duyệt web
//@RequestMapping(path="/user")
public class UserController {

	@Autowired
	ShareBookDAO userRepository; 
	
	
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userlogin");
		return "redirect:/index";
	}
	
	@RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
	public String getTest(Model model) {
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
		return "index";
	}
	
	@RequestMapping("/file/{fileName}")
	public void show(@PathVariable("fileName") String fileName, HttpServletResponse response) {
	      response.setContentType("application/pdf"); // thiet lap kieu du lieu ma web tra ve browser
	      response.setHeader("Content-Disposition", "attachment; filename=" +fileName); // bao cho trinh duyet download va save file, filename la ten cua file dc save
//	      response.setHeader("Content-Transfer-Encoding", "binary");
	      try {
	    	  BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	    	  FileInputStream fis = new FileInputStream("./pdf/"+fileName);
	    	  int len;
	    	  byte[] buf = new byte[1024];
	    	  while((len = fis.read(buf)) > 0) {
	    		  bos.write(buf,0,len);
	    	  }
	    	  bos.close();
	    	  response.flushBuffer(); // noi dung trong bo dem se duoc ghi may nguoi dung
	      }
	      catch(IOException e) {
	    	  e.printStackTrace();
	    	  
	      }
	}
	
	
	
	@GetMapping(value="/thongtin")
	public String showThongTin(@RequestParam(value="id") int id, Model model)
	{
		Sach sach = new Sach();
		TheLoai theLoai = new TheLoai();
		sach = userRepository.getSach(id);
		if(sach==null) {
			return "redirect:/index";
		}
		else
		{
		theLoai = userRepository.getTheLoai(sach.getIdTheLoai());
		
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
		model.addAttribute("sach",sach);
		model.addAttribute("theloai",theLoai); 
		
		
		
		return "thongtin";
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String getTest2(
			@RequestParam ( value="page", required = false) String strPageNo,
			@RequestParam(value="theloai", required = false) String strTheLoai,
			@RequestParam(value="tensach",  required = false) String tenSach,
			Model model) {
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
		
		int pageSize = 8;
		int totalPage = (int)(Math.ceil(userRepository.getSoLuongSach(strTheLoai, tenSach)/(double)pageSize));
//		int totalPage = (int)Math.ceil(userRepository.getSoLuongSach(strTheLoai, tenSach)/pageSize);
		int viTri = pageNo*pageSize-pageSize;
		
		
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
		return "search";
	}
	
	@PostMapping(value="/search") 
	public String searchBook(@RequestParam("list-search") int id,
						@RequestParam("search") String tenSach) {
		
		System.out.println(tenSach);
		tenSach = URLEncoder.encode(tenSach);
		return "redirect:/search?page=1&theloai="+id+"&tensach="+tenSach;
	}
	
}

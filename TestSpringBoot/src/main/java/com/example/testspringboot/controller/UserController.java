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
	
	
//	@RequestMapping(value="/login", method = RequestMethod.GET) // ánh xạ các yêu cầu của client vào method tương ứng
//	public String getLoginForm()
//	{
////		System.out.println(userRepository.getAllTheLoai().get(0).getTenTheLoai()); 
//		return "login2";
//	}
//	
//	@RequestMapping(value="/login", method=RequestMethod.POST) 
//	public String login(@ModelAttribute(name="loginForm")User user, Model model) 
//	// cầu nối giữa controller với view , từ controller, truyền dữ liệu qua cho view thông qua modelattribute
//	// từ view, sử dụng themeleaf để đọc dữ liệu từ model và hiển thị cho người dùng
//	{
//		String userName = user.getUsername();
//		String passWord = user.getPassword();
//		
//		String check = userRepository.getUser(userName,passWord);
//		
//		System.out.println(check);
//		if(check!="Khong tim thay")
//		{
//			login = true;
//			return "home";
//		}
//		
//		model.addAttribute("loiDangNhap", true);
//		return "login2";
//	}
	
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userlogin");
		return "redirect:/index";
	}
	
	@RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
	public String getTest(Model model) {
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
//		List<Sach> sachs = new ArrayList<Sach>();
//		Sach s1 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		Sach s2 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		Sach s3 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		Sach s4 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		Sach s5 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		Sach s6 =  new Sach(0, 1, "a", "https://sachvui.com/cover2/2019/khi-nguoi-ta-tu-duy.jpg", "Sach 1" , "s");
//		sachs.add(s1);
//		sachs.add(s2);
//		sachs.add(s3);
//		sachs.add(s4);
//		sachs.add(s5);
//		sachs.add(s6);
//		System.out.println(sachs.size());
//		for(int i=1;i<=sachs.size();i++)
//		{
//			System.out.println(i);
//		}
		return "index";
	}
	
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public String a(Model model) {
//		if(login==false)	return "index";
//		File folder = new File("./pdf/");
//		File[] listOfFiles = folder.listFiles();
//		model.addAttribute("files", listOfFiles);
//		List<Sach> sachs = userRepository.getAllSach();
//		model.addAttribute("listSale", sachs);
//		return "test";
//	}
	
//	private static final String EXTERNAL_FILE_PATH = "./pdf/";
	
	@RequestMapping("/file/{fileName}")
	@ResponseBody
	public void show(@PathVariable("fileName") String fileName, HttpServletResponse response) {
	      response.setContentType("application/pdf");
	      response.setHeader("Content-Disposition", "attachment; filename=" +fileName);
	      response.setHeader("Content-Transfer-Encoding", "binary");
	      try {
	    	  BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
	    	  FileInputStream fis = new FileInputStream("./pdf/"+fileName);
	    	  int len;
	    	  byte[] buf = new byte[1024];
	    	  while((len = fis.read(buf)) > 0) {
	    		  bos.write(buf,0,len);
	    	  }
	    	  bos.close();
	    	  response.flushBuffer();
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
		theLoai = userRepository.getTheLoai(sach.getIdTheLoai());
		
		List<TheLoai> theLoais = userRepository.getAllTheLoai();
		model.addAttribute("listTheLoai", theLoais);
		
		model.addAttribute("sach",sach);
		model.addAttribute("theloai",theLoai); 
		
		return "thongtin";
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
		sach = userRepository.searchSach(strTheLoai, tenSach, viTri);
		
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
	
//	@RequestMapping(value = "/testt", method = RequestMethod.GET)
//	public String getTestt() {
//		return "testt";
//	}
	
//	@PostMapping(value="/testt/a")
//	public String save(@ModelAttribute(name="saveSachForm") Sach sach, RedirectAttributes ra, 
//			@RequestParam("fileImage") MultipartFile[] file) throws IOException
//	{
//		boolean flag=true;
//		for(MultipartFile file2 : file) {
//		String fileName = StringUtils.cleanPath(file2.getOriginalFilename());
//		String fileExtension = Files.getFileExtension(fileName);
//		System.out.println(Files.getFileExtension(fileName));
//		if(fileExtension.equals("pdf") || fileExtension.equals("jpg") || fileExtension.equals("png"))
//		{
//			String uploadDir ="";
//			if(fileExtension.equals("jpg")  || fileExtension.equals("png"))
//			{
//				sach.setImg(fileName);
//				uploadDir ="./images/";
//			}
//			else 
//			{
//				sach.setFileEbook(fileName);
//				uploadDir ="./pdf/";
//			}
//			Path uploadPath = Paths.get(uploadDir);
//			
//			if(!java.nio.file.Files.exists(uploadPath))
//			{
//				java.nio.file.Files.createDirectories(uploadPath);
//			}
//			
//			try(InputStream inputStream = file2.getInputStream())
//			{
//				Path filePath = uploadPath.resolve(fileName);
//				System.out.println(filePath.toFile().getAbsolutePath());
//				java.nio.file.Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//			}
//			catch(IOException e)
//			{
//				throw new IOException("Khong the save file upload: "+fileName);
//			}
//		}
//		else
//		{
//			System.out.println("File khong duoc ho tro: "+fileName);
//			flag=false;
//			break;
//		}
//		}
////		System.out.println(sach2.getFileEbook());
//		if(flag==true && sach.getImg()!=null && sach.getFileEbook()!=null)
//		{
//			userRepository.addSachh(sach);
//			ra.addFlashAttribute("message","Thanh cong");
//		}
//		else
//		{
//			ra.addFlashAttribute("message","That bai");
//		}
//		return "redirect:/testt";
//		
//	}
	
	
	//
	
//	public String index()
//	{
//		return "index";
//	}
	
}

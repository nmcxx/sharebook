package com.example.testspringboot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.testspringboot.User;
import com.example.testspringboot.repository.ShareBookDAO;

@Controller
public class LoginController {
	
	@Autowired
	ShareBookDAO userRepository; 
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String getLoginForm(HttpSession session)
	{
		System.out.println(session.getAttribute("userlogin"));
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST) 
	public String login(@ModelAttribute(name="loginForm")User user, Model model,
			HttpSession session) 
	// cầu nối giữa controller với view , từ controller, truyền dữ liệu qua cho view thông qua modelattribute
	// từ view, sử dụng themeleaf để đọc dữ liệu từ model và hiển thị cho người dùng
	{
		String userName = user.getUsername();
		String passWord = user.getPassword();
		
		String check = userRepository.getUser(userName,passWord);
		
		System.out.println(check);
		if(check!="Khong tim thay")
		{
			session.setAttribute("userlogin", userName);
			return "redirect:/";
		}
		
		model.addAttribute("loiDangNhap", true);
		return "login";
	}
	
}

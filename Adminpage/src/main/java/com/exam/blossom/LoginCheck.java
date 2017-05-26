package com.exam.blossom;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginCheck {
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping("/loginCheck.do")
	public ModelAndView loginCheck(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(username.equals("admin") && password.equals("123456")) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("admin_id", username);
			map.put("admin_name", "adminstrator");
			request.getSession().setAttribute("loginInfo", map);
			
			modelAndView.setViewName("loginCheck");
			return modelAndView;
		} 
		
		modelAndView.setViewName("login");
		return modelAndView;
		
		
	}
	
	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(request.getSession().getAttribute("loginInfo") != null) {
			request.getSession().removeAttribute("loginInfo");
			modelAndView.setViewName("login");
			return modelAndView;
		}
		
		modelAndView.setViewName("login");
		return modelAndView;
		
		
		
		
	}
	
}

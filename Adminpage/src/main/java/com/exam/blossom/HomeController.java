package com.exam.blossom;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.exam.mybatis.BlossomDAO;
import com.exam.mybatis.BlossomTO;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private SqlSession sqlSession;
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */

	
	@RequestMapping("/login.do")
	public ModelAndView login() {
		
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		
		return modelAndView;
	}
	
	@RequestMapping("/chartjs.do")
	public ModelAndView chartjs() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("chartjs");
		
		return modelAndView;
	}
	
	@RequestMapping("/chartjs2.do")
	public ModelAndView chartjs2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("chartjs2");
		
		return modelAndView;
	}
	
	@RequestMapping("/echarts.do")
	public ModelAndView echarts() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("echarts");
		
		return modelAndView;
	}
	
	@RequestMapping("/morisjs.do")
	public ModelAndView morisjs() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("morisjs");
		
		return modelAndView;
	}
	
	@RequestMapping("/other_charts.do")
	public ModelAndView other_charts() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("other_charts");
		
		return modelAndView;
	}
	
	@RequestMapping("/other_charts2.do")
	public ModelAndView other_charts2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("other_charts2");
		
		return modelAndView;
	}
	
	@RequestMapping("/tables_dynamic.do")
	public ModelAndView tables_dynamic() {
		ModelAndView modelAndView = new ModelAndView();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		ArrayList<BlossomTO> datas = dao.userList();
		modelAndView.addObject("datas", datas);
		
		modelAndView.setViewName("tables_dynamic");
		
		return modelAndView;
	}
}

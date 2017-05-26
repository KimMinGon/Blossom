package com.exam.blossom;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.exam.mybatis.BlossomDAO;
import com.exam.mybatis.BlossomTO;

@Controller
public class JsonController {
	@Autowired
	private SqlSession sqlSession;
	private HashMap<String, String> map;
	
	@RequestMapping("/jsonUserDatas.do")
	public ModelAndView jsonUserDatas() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		int[] datas = dao.userDatas();
		
		for(int i=0 ; i<datas.length ; i++) {
			map.put(i + "", datas[i] + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
	@RequestMapping("/jsonCharacterDatas.do")
	public ModelAndView jsonCharacterDatas() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		int[] datas = dao.characterDatas();
		
		for(int i=0 ; i<datas.length ; i++) {
			map.put(i + "", datas[i] + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
	@RequestMapping("/jsonCharacterPoints.do")
	public ModelAndView jsonCharacterPoints() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		int[] datas = dao.characterPoints();
		
		for(int i=0 ; i<datas.length ; i++) {
			map.put(i + "", datas[i] + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
	@RequestMapping("/jsonWalkingDatas.do")
	public ModelAndView jsonWalkingDatas() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		int[] datas = dao.walkingDatas();
		
		for(int i=0 ; i<datas.length ; i++) {
			map.put(i + "", datas[i] + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
	@RequestMapping("/jsonDrinkingdatas.do")
	public ModelAndView jsonDrinkingdatas() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		int[] datas = dao.drinkingDatas();
		
		for(int i=0 ; i<datas.length ; i++) {
			map.put(i + "", datas[i] + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
	@RequestMapping("/jsonGpsdistance.do")
	public ModelAndView jsonGpsdistance() {
		
		ModelAndView modelAndView = new ModelAndView();
		
		map = new HashMap<String, String>();
		//BoardDAO dao = new BoardDAO();
		//map = dao.userDatas();
		
		BlossomDAO dao = sqlSession.getMapper(BlossomDAO.class);
		ArrayList<BlossomTO> datas = dao.gpsDistance();
		
		for(int i=0 ; i<datas.size() ; i++) {
			map.put("stime" + i, (datas.get(i).getStime()).substring(11, 13));
			map.put("tdistance" + i, datas.get(i).getTdistance() + "");
		}
		
		modelAndView.addAllObjects(map);
		
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	
}

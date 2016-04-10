package com.tauros.kaleido.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tauros on 2016/4/7.
 */
@Controller
public class HomeController extends BaseController {

	@RequestMapping({"/", "home", "index"})
	public String homePage(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute(request);
		model.addAttribute(response);
		model.addAttribute("message", "Welcome to Kaleido!");
		model.addAttribute("message_cn", "欢迎来到万花镜!");
		return "index";
	}

}

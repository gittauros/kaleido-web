package com.tauros.kaleido.web.interceptor;

import com.tauros.kaleido.web.controller.BaseController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tauros on 2016/4/7.
 * <p>
 * 全局拦截器
 * 1.request和response的注入
 */
public class GlobalInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//注入request和response
		injectControllerRequestAndResponse(handler, request, response);

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject("contextPath", request.getContextPath());
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		//清除request和response
		injectControllerRequestAndResponse(handler, null, null);
	}

	/**
	 * 为Controller注入request和response
	 *
	 * @param handler
	 * @param request
	 * @param response
	 */
	private void injectControllerRequestAndResponse(Object handler, HttpServletRequest request, HttpServletResponse response) {
		BaseController controller = getController(handler);
		if (controller != null) {
			controller.setRequest(request);
			controller.setResponse(response);
		}
	}

	private BaseController getController(Object handler) {
		if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Object bean = method.getBean();
			if (bean != null && bean instanceof BaseController) {
				return (BaseController) bean;
			}
		}
		return null;
	}
}

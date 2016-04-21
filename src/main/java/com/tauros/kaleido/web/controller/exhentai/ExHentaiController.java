package com.tauros.kaleido.web.controller.exhentai;

import com.tauros.kaleido.core.model.bean.ExHentaiListParamBean;
import com.tauros.kaleido.core.service.ExHentaiService;
import com.tauros.kaleido.core.util.ConsoleLog;
import com.tauros.kaleido.core.util.SystemUtils;
import com.tauros.kaleido.web.controller.BaseController;
import com.tauros.kaleido.web.util.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by tauros on 2016/4/9.
 */
@Controller
@RequestMapping("exhentai")
public class ExHentaiController extends BaseController {

	@Resource
	private ExHentaiService exHentaiService;

	@RequestMapping("list")
	public String exhentaiList(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
		String search = request.getParameter("search");
		String apply = request.getParameter("f_apply");
		int page = RequestUtils.getInt(request, "page");
		int fDoujinshi = RequestUtils.getBooleanInt(request, "fdoujinshi");
		int fManga = RequestUtils.getBooleanInt(request, "fmanga");
		int fArtistcg = RequestUtils.getBooleanInt(request, "fartistcg");
		int fGamecg = RequestUtils.getBooleanInt(request, "fgamecg");
		int fWestern = RequestUtils.getBooleanInt(request, "fwestern");
		int fNonh = RequestUtils.getBooleanInt(request, "fnonh");
		int fImageset = RequestUtils.getBooleanInt(request, "fimageset");
		int fCosplay = RequestUtils.getBooleanInt(request, "fcosplay");
		int fAsianporn = RequestUtils.getBooleanInt(request, "fasianporn");
		int fMisc = RequestUtils.getBooleanInt(request, "fmisc");

		ExHentaiListParamBean paramBean = new ExHentaiListParamBean();
		if (search != null) {
			paramBean.setfSearch(URLEncoder.encode(search, "UTF-8"));
			search = search.replaceAll("&", "&amp;");
			search = search.replaceAll("\"", "&quot;");
			search = search.replaceAll("<", "&lt;");
			search = search.replaceAll(">", "&gt;");
		}
		if (apply != null) {
			paramBean.setfApply(apply);
		}
		if (page <= 0) {
			page = 1;
		}
		paramBean.setPage(page);
		paramBean.setfDoujinshi(fDoujinshi);
		paramBean.setfManga(fManga);
		paramBean.setfArtistcg(fArtistcg);
		paramBean.setfGamecg(fGamecg);
		paramBean.setfWestern(fWestern);
		paramBean.setfNonh(fNonh);
		paramBean.setfImageset(fImageset);
		paramBean.setfCosplay(fCosplay);
		paramBean.setfAsianporn(fAsianporn);
		paramBean.setfMisc(fMisc);

		Map<String, Object> pageResult = exHentaiService.searchListPage(paramBean);

		for (Map.Entry<String, Object> entry : pageResult.entrySet()) {
			model.addAttribute(entry.getKey(), entry.getValue());
		}


		model.addAttribute("search", search);
		model.addAttribute("apply", apply);
		model.addAttribute("fdoujinshi", fDoujinshi == 1);
		model.addAttribute("fmanga", fManga == 1);
		model.addAttribute("fartistcg", fArtistcg == 1);
		model.addAttribute("fgamecg", fGamecg == 1);
		model.addAttribute("fwestern", fWestern == 1);
		model.addAttribute("fnonh", fNonh == 1);
		model.addAttribute("fimageset", fImageset == 1);
		model.addAttribute("fcosplay", fCosplay == 1);
		model.addAttribute("fasianporn", fAsianporn == 1);
		model.addAttribute("fmisc", fMisc == 1);
		model.addAttribute("page", page);
		model.addAttribute("savePath", SystemUtils.getSavePath());
		return "exhentaiList";
	}

	@RequestMapping("download")
	@ResponseBody
	public String download(HttpServletRequest request) {
		String url = request.getParameter("url");
		String saveBasePath = request.getParameter("path");
		long sleep = RequestUtils.getLong(request, "sleep");
		boolean origin = RequestUtils.getBoolean(request, "origin");

		if (sleep == 0) {
			sleep = 1000;
		}


		if (!saveBasePath.endsWith("/")) {
			if (saveBasePath.endsWith("\\")) {
				saveBasePath = saveBasePath.substring(0, saveBasePath.length() - 1) + "/";
			} else {
				saveBasePath = saveBasePath + "/";
			}
		}
		ConsoleLog.e("请求下载");
		String msg = exHentaiService.download(saveBasePath, url, sleep, origin);

		return msg;
	}

	@RequestMapping("modifySavePath")
	@ResponseBody
	public String modifySavePath(String savePath) {
		boolean result = SystemUtils.setSavePath(savePath);
		return result ? "success" : "failed";
	}
}

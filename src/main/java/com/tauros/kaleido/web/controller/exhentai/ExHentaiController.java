package com.tauros.kaleido.web.controller.exhentai;

import com.tauros.kaleido.core.constant.ExHentaiConstant;
import com.tauros.kaleido.core.model.param.ExHentaiListParam;
import com.tauros.kaleido.core.model.vo.ExHentaiGalleryVO;
import com.tauros.kaleido.core.model.vo.ExHentaiListVO;
import com.tauros.kaleido.core.model.vo.ExHentaiPhotoVO;
import com.tauros.kaleido.core.service.ExHentaiService;
import com.tauros.kaleido.core.util.SystemUtil;
import com.tauros.kaleido.web.controller.BaseController;
import com.tauros.kaleido.web.util.ExHentaiUrlConverter;
import com.tauros.kaleido.web.util.ImageUrlConverter;
import com.tauros.kaleido.web.util.RequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by tauros on 2016/4/9.
 */
@Controller
@RequestMapping("exhentai")
public class ExHentaiController extends BaseController implements ExHentaiConstant {

    @Resource
    private ExHentaiService exHentaiService;

    private static List<ExHentaiListVO> convertListBO(List<ExHentaiListVO> exHentaiListVOS) {
        if (exHentaiListVOS == null) {
            return null;
        }
        for (ExHentaiListVO exHentaiListVO : exHentaiListVOS) {
            exHentaiListVO.setCoverImg(ImageUrlConverter.convertExhentaiImageUrl(exHentaiListVO.getCoverImg()));
            exHentaiListVO.setTagImg(ImageUrlConverter.convertExhentaiImageUrl(exHentaiListVO.getTagImg()));
            exHentaiListVO.setGalleryUrl(ExHentaiUrlConverter.convertExhentaiGalleryUrl(exHentaiListVO.getGalleryUrl()));
        }
        return exHentaiListVOS;
    }

    private static List<ExHentaiGalleryVO> convertGalleryBO(List<ExHentaiGalleryVO> exHentaiGalleryVOS) {
        if (exHentaiGalleryVOS == null) {
            return null;
        }
        for (ExHentaiGalleryVO exHentaiGalleryVO : exHentaiGalleryVOS) {
            exHentaiGalleryVO.setLargeImg(ImageUrlConverter.convertExhentaiImageUrl(exHentaiGalleryVO.getLargeImg()));
            exHentaiGalleryVO.setSmallImg(ImageUrlConverter.convertExhentaiImageUrl(exHentaiGalleryVO.getSmallImg()));
            exHentaiGalleryVO.setSmallImgPlaceHolder(ImageUrlConverter.convertExhentaiImageUrl(exHentaiGalleryVO.getSmallImgPlaceHolder()));
            exHentaiGalleryVO.setPhotoUrl(ExHentaiUrlConverter.convertExhentaiPhotoUrl(exHentaiGalleryVO.getPhotoUrl()));
        }
        return exHentaiGalleryVOS;
    }

    private static ExHentaiPhotoVO convertPhotoBO(ExHentaiPhotoVO exHentaiPhotoVO) {
        if (exHentaiPhotoVO == null) {
            return null;
        }
        exHentaiPhotoVO.setPhotoImg(ImageUrlConverter.convertExhentaiImageUrl(exHentaiPhotoVO.getPhotoImg()));
        exHentaiPhotoVO.setFirstPageUrl(ExHentaiUrlConverter.convertExhentaiPhotoUrl(exHentaiPhotoVO.getFirstPageUrl()));
        exHentaiPhotoVO.setPrevPageUrl(ExHentaiUrlConverter.convertExhentaiPhotoUrl(exHentaiPhotoVO.getPrevPageUrl()));
        exHentaiPhotoVO.setNextPageUrl(ExHentaiUrlConverter.convertExhentaiPhotoUrl(exHentaiPhotoVO.getNextPageUrl()));
        exHentaiPhotoVO.setLastPageUrl(ExHentaiUrlConverter.convertExhentaiPhotoUrl(exHentaiPhotoVO.getLastPageUrl()));
        return exHentaiPhotoVO;
    }

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

        ExHentaiListParam param = new ExHentaiListParam();
        if (search != null) {
            param.setfSearch(URLEncoder.encode(search, "UTF-8"));
            search = search.replaceAll("&", "&amp;");
            search = search.replaceAll("\"", "&quot;");
            search = search.replaceAll("<", "&lt;");
            search = search.replaceAll(">", "&gt;");
        }
        if (apply != null) {
            param.setfApply(apply);
        }
        if (page <= 0) {
            page = 1;
        }
        param.setPage(page);
        param.setfDoujinshi(fDoujinshi);
        param.setfManga(fManga);
        param.setfArtistcg(fArtistcg);
        param.setfGamecg(fGamecg);
        param.setfWestern(fWestern);
        param.setfNonh(fNonh);
        param.setfImageset(fImageset);
        param.setfCosplay(fCosplay);
        param.setfAsianporn(fAsianporn);
        param.setfMisc(fMisc);

        Map<String, Object> pageResult = exHentaiService.searchListPage(param);
        //转换列表展示元素参数
        convertListBO((List<ExHentaiListVO>) pageResult.get(LIST_BO_KEY));
        int maxPage = (Integer) pageResult.get(MAX_PAGE_KEY);
        boolean overMaxPage = page > maxPage;
        if (overMaxPage) {
            page = maxPage;
        }

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
        model.addAttribute("overMaxPage", overMaxPage);
        model.addAttribute("savePath", SystemUtil.getSavePath());
        return "exhentai/exhentaiList";
    }

    @RequestMapping("gallery")
    public String gallery(Model model, String oriUrl, Boolean large, Integer page) {
        if (large == null) {
            large = false;
        }
        if (page == null || page == 0) {
            page = 1;
        }

        Map<String, Object> pageResult = exHentaiService.galleryPage(oriUrl, large, page);
        //转换相册展示元素参数
        convertGalleryBO((List<ExHentaiGalleryVO>) pageResult.get(GALLERY_BO_KEY));
        int maxPage = (Integer) pageResult.get(MAX_PAGE_KEY);
        if (page > maxPage) {
            page = maxPage;
        }

        for (Map.Entry<String, Object> entry : pageResult.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }

        try {
            String encodeOriUrl = URLEncoder.encode(oriUrl, "UTF-8");
            model.addAttribute("encodeOriUrl", encodeOriUrl);
        } catch (IOException ioe) {

        }
        model.addAttribute("oriUrl", oriUrl);
        model.addAttribute("large", large);
        model.addAttribute("page", page);
        model.addAttribute("savePath", SystemUtil.getSavePath());
        return "exhentai/exhentaiGallery";
    }

    @RequestMapping("photo")
    public String photo(Model model, String oriUrl) {
        Map<String, Object> pageResult = exHentaiService.photoPage(oriUrl);
        //转换图片展示元素参数
        convertPhotoBO((ExHentaiPhotoVO) pageResult.get(PHOTO_BO_KEY));

        for (Map.Entry<String, Object> entry : pageResult.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }

        return "exhentai/exhentaiPhoto";
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
        logger.info(String.format("request for download url=%s", url));
        String msg = exHentaiService.download(saveBasePath, url, sleep, origin);

        return msg;
    }

    @RequestMapping("modifySavePath")
    @ResponseBody
    public String modifySavePath(String savePath) {
        boolean result = SystemUtil.setSavePath(savePath);
        return result ? "success" : "failed";
    }
}

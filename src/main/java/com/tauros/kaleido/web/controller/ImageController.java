package com.tauros.kaleido.web.controller;

import com.tauros.kaleido.core.service.ExHentaiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tauros on 2016/4/10.
 */
@Controller
@RequestMapping("image")
public class ImageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ExHentaiService exHentaiService;

    @RequestMapping("exhentaiImage")
    @ResponseBody
    public void image(HttpServletResponse response, String imageUrl) {
        response.setContentType("image/jpeg");
        byte[] imageData = exHentaiService.image(imageUrl);

        try {
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(imageData);
            outputStream.flush();
//			outputStream.close();
        } catch (IOException ioe) {
            logger.warn("visit image exception imageUrl=" + imageUrl, ioe);
        }
    }
}

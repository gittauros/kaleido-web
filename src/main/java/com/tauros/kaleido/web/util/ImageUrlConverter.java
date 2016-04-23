package com.tauros.kaleido.web.util;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by tauros on 2016/4/10.
 */
public final class ImageUrlConverter {

	public static String convertExhentaiImageUrl(String imageUrl) {
		if (imageUrl == null) {
			return null;
		}
		try {
			imageUrl = URLEncoder.encode(imageUrl, "UTF-8");
		} catch (IOException ioe) {

		}
		return "/image/exhentaiImage?imageUrl=" + imageUrl;
	}
}

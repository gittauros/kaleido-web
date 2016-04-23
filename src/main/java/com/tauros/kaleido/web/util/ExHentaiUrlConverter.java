package com.tauros.kaleido.web.util;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by tauros on 2016/4/23.
 */
public final class ExHentaiUrlConverter {

	public static String convertExhentaiGalleryUrl(String galleryUrl) {
		if (galleryUrl == null) {
			return null;
		}
		try {
			galleryUrl = URLEncoder.encode(galleryUrl, "UTF-8");
		} catch (IOException ioe) {

		}
		return "/exhentai/gallery?oriUrl=" + galleryUrl;
	}

	public static String convertExhentaiPhotoUrl(String photoUrl) {
		if (photoUrl == null) {
			return null;
		}
		try {
			photoUrl = URLEncoder.encode(photoUrl, "UTF-8");
		} catch (IOException ioe) {

		}
		return "/exhentai/photo?oriUrl=" + photoUrl;
	}
}

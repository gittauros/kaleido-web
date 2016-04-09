package com.tauros.kaleido.web.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tauros on 2016/4/9.
 */
public final class RequestUtils {

	public static long getLong(HttpServletRequest request, String key) {
		return NumberUtils.toLong(request.getParameter(key), 0);
	}

	public static int getInt(HttpServletRequest request, String key) {
		return NumberUtils.toInt(request.getParameter(key), 0);
	}

	public static int getBooleanInt(HttpServletRequest request, String key) {
		String value = request.getParameter(key);
		boolean res = false;
		if (StringUtils.isNotBlank(value)) {
			res = "1".equals(value) || "true".equals(value);
		}
		return res ? 1 : 0;
	}

	public static boolean getBoolean(HttpServletRequest request, String key) {
		String value = request.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return "1".equals(value) || "true".equals(value);
		}
		return false;
	}
}

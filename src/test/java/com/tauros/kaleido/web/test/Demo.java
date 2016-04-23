package com.tauros.kaleido.web.test;

import org.apache.commons.lang.math.NumberUtils;

import java.io.Serializable;

/**
 * Created by tauros on 2016/4/9.
 */
public class Demo implements Serializable {
	private static final long serialVersionUID = 7929884469842370895L;

	public static void main(String[] args) {
		String str = "transparent url(http://95.211.202.80/m/000926/926726-00.jpg) -100px 0 no-repeat";
//		String smallImg = null;
//		int smallImgXOffset = 0;
//		int smallImgYOffset = 0;
//		int index = str.indexOf(" ");
//		int i = 1;
//		int smallImgStart = 0;
//		int smallImgEnd = 0;
//		int xOffsetEnd = 0;
//		int yOffsetEnd = 0;
//		while (index != -1) {
//			switch (i) {
//				case 1:
//					smallImgStart = index;
//					break;
//				case 2:
//					smallImgEnd = index;
//					smallImg = str.substring(smallImgStart, smallImgEnd);
//					smallImg = smallImg.replaceAll("url", "");
//					smallImg = smallImg.replaceAll("[()]", "");
//					smallImg = smallImg.replaceAll("http://", "");
//					smallImg = smallImg.trim();
//					break;
//				case 3:
//					xOffsetEnd = index;
//					String xOffsetStr = str.substring(smallImgEnd, xOffsetEnd);
//					smallImgXOffset = NumberUtils.toInt(xOffsetStr.replaceAll("-|px", "").trim());
//					if (xOffsetStr.contains("-")) {
//						smallImgXOffset = -smallImgXOffset;
//					}
//					break;
//				case 4:
//					yOffsetEnd = index;
//					String yOffsetStr = str.substring(xOffsetEnd, yOffsetEnd);
//					smallImgYOffset = NumberUtils.toInt(yOffsetStr.replaceAll("-|px", "").trim());
//					if (yOffsetStr.contains("-")) {
//						smallImgYOffset = -smallImgYOffset;
//					}
//					break;
//				default:
//					break;
//			}
//
//			index = str.indexOf(" ", index + 1);
//			i++;
//		}
//
//		System.out.println(smallImg);
//		System.out.println(smallImgXOffset);
//		System.out.println(smallImgYOffset);
	}
}

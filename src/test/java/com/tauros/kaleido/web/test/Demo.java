package com.tauros.kaleido.web.test;

import org.apache.commons.lang.math.NumberUtils;

import java.io.Serializable;

/**
 * Created by tauros on 2016/4/9.
 */
public class Demo implements Serializable {
	private static final long serialVersionUID = 7929884469842370895L;

	public static void main(String[] args) {
		Integer a=256, b=256;
		System.out.println(a);
		System.out.println(b);
		System.out.println(a>=b && a<=b && a!=b);
	}
}

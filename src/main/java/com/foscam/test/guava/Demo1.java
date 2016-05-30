package com.foscam.test.guava;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.google.common.base.Defaults;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.math.IntMath;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class Demo1 {

	public static void main(String[] args) {
		
		String s = com.google.common.base.Defaults.defaultValue(String.class);
		System.out.println(s);
		
		int a = Defaults.defaultValue(int.class);
		System.out.println(a);
		Integer b = Defaults.defaultValue(int.class);
		System.out.println(b);
		Integer d = Defaults.defaultValue(Integer.class);
		System.out.println(d);

		System.out.println(Strings.commonPrefix("acc", "abcc"));
		System.out.println(Strings.isNullOrEmpty(""));
		System.out.println(Strings.repeat("aagegisa", 3));
		
		String[] arr = new String[]{"1","2","a"};
		
		String[] arr1 = new String[10];
		
		System.arraycopy(arr, 0, arr1, 0, arr.length);
		System.out.println(Arrays.toString(arr1));
		
		
		List<String> ss = Arrays.asList("a","b");
		System.out.println(ss);
		
		Ordering<Integer> ord = new Ordering<Integer>() {

			@Override
			public int compare(Integer paramT1, Integer paramT2) {
				return paramT1 - paramT2;
			}
		};
		Integer m1 = ord.min(3,1,3,7,4);
		System.out.println(m1);
		
	}

}

package com.yinuo.utils.toolbox;

import java.util.UUID;

public class IdGen {
	private IdGen(){}
	private static final String BLANK = "";
	private static final String MINUS = "-";
	
	public static String uuid(){
		return UUID.randomUUID().toString().replace(MINUS, BLANK);
	}
}

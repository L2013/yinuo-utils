package com.yinuo.utils.toolbox;

public class BytesKit {
	public static byte[] hexToBytes(String hex) {
	    return hexToBytes(hex.toCharArray());
	}

	public static byte[] hexToBytes(char[] hex) {
	    int length = hex.length / 2;
	    byte[] raw = new byte[length];
	    for (int i = 0; i < length; i++) {
	        int high = Character.digit(hex[i * 2], 16);
	        int low = Character.digit(hex[i * 2 + 1], 16);
	        int value = (high << 4) | low;
	        if (value > 127) {
	            value -= 256;
	        }
	        raw[i] = (byte) value;
	    }
	    return raw;
	}
}

package com.catlbattery.alm.util;

public class CharacterUtil {

	public static boolean isLeftMirror(char ch) {
		if (Character.isMirrored(ch)) {
			switch (ch) {
			case '(':
				return true;
			case '[':
				return true;
			case '{':
				return true;
			default:
			}
		}
		return false;
	}

	public static boolean isRightMirror(char ch) {
		boolean result = false;
		if (Character.isMirrored(ch)) {
			switch (ch) {
			case ')':
				return true;
			case ']':
				return true;
			case '}':
				return true;
			default:
			}
		}
		return result;
	}
}

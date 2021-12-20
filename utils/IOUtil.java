package com.andedit.arcubit.utils;

public final class IOUtil {
	public static final boolean
	isWindows = System.getProperty("os.name").contains("Windows"),
	isLinux = System.getProperty("os.name").contains("Linux"),
	isMac = System.getProperty("os.name").contains("Mac");
}

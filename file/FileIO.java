package com.andedit.arcubit.file;

import static com.badlogic.gdx.Gdx.files;

import com.andedit.arcubit.utils.IOUtil;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.files.FileHandle;

public final class FileIO {
	public static final String DIR = IOUtil.isWindows ? "AppData/Roaming/Arcubit/" : Util.isDesktop() ? "Arcubit/" : "";

	public static FileHandle external(String path) {
		return files.external(DIR + path);
	}
}

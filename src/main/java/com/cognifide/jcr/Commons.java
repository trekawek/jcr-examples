package com.cognifide.jcr;

import java.io.File;
import java.io.IOException;

import org.apache.jackrabbit.core.fs.local.FileUtil;

public class Commons {
	public static void cleanUpDirs() {
		try {
			FileUtil.delete(new File("repository"));
			FileUtil.delete(new File("repository.xml"));
		} catch (IOException e) {
		}
	}
}

package cc.ethon.coldspot.backend.cpp;

import java.util.Arrays;
import java.util.stream.Collectors;

import cc.ethon.coldspot.common.ClassName;

class CppClassNameUtil {

	public static String makeIncludeGuardName(ClassName name) {
		final String packagePart = Arrays.stream(name.getPackageParts()).map(part -> part.toUpperCase()).collect(Collectors.joining("_"));
		return packagePart + "_" + name.getName().toUpperCase() + "_HPP";
	}

	public static String makeQualifiedName(ClassName name) {
		final String packagePart = Arrays.stream(name.getPackageParts()).collect(Collectors.joining("::"));
		return packagePart + "::" + name.getName();
	}

}

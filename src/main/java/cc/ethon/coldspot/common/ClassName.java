package cc.ethon.coldspot.common;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassName {

	private final String[] packageParts;
	private final String name;

	public ClassName(String className) {
		final String[] parts = className.split("/");
		packageParts = new String[parts.length - 1];
		System.arraycopy(parts, 0, packageParts, 0, packageParts.length);
		name = parts[parts.length - 1];
	}

	public String[] getPackageParts() {
		return packageParts;
	}

	public String getName() {
		return name;
	}

	public String getPackageAsPath() {
		return Arrays.stream(packageParts).collect(Collectors.joining("/"));
	}

	@Override
	public String toString() {
		return Arrays.stream(packageParts).collect(Collectors.joining(".")) + "." + getName();
	}

}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(packageParts);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ClassName other = (ClassName) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (!Arrays.equals(packageParts, other.packageParts)) {
			return false;
		}
		return true;
	}

}

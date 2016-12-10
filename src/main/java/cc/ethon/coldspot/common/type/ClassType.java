package cc.ethon.coldspot.common.type;

import java.util.HashMap;
import java.util.Map;

import cc.ethon.coldspot.common.ClassName;

public class ClassType implements Type {

	private static final Map<ClassName, ClassType> cache = new HashMap<ClassName, ClassType>();

	private final ClassName name;

	private ClassType(ClassName name) {
		super();
		this.name = name;
	}

	public static ClassType typeOf(ClassName name) {
		synchronized (cache) {
			ClassType classType = cache.get(name);
			if (classType == null) {
				classType = new ClassType(name);
				cache.put(name, classType);
			}
			return classType;
		}
	}

	public ClassName getName() {
		return name;
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

package cc.ethon.coldspot.common.type;

import cc.ethon.coldspot.common.ClassName;

public class ClassType implements Type {

	private final ClassName name;

	public ClassType(ClassName name) {
		super();
		this.name = name;
	}

	public ClassName getName() {
		return name;
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

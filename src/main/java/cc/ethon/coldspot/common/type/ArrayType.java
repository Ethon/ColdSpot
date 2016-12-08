package cc.ethon.coldspot.common.type;

public class ArrayType implements Type {

	private final Type subType;

	public ArrayType(Type subType) {
		this.subType = subType;
	}

	public Type getSubType() {
		return subType;
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

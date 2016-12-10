package cc.ethon.coldspot.common.type;

public class IntType implements Type {

	public static final IntType INT = new IntType();

	private IntType() {

	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

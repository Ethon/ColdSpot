package cc.ethon.coldspot.common.type;

public class FloatType implements Type {

	public static final FloatType FLOAT = new FloatType();

	private FloatType() {

	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

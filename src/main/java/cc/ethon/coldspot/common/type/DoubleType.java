package cc.ethon.coldspot.common.type;

public class DoubleType implements Type {

	public static final DoubleType DOUBLE = new DoubleType();

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

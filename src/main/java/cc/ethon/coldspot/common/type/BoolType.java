package cc.ethon.coldspot.common.type;

public class BoolType implements Type {

	public static final BoolType BOOL = new BoolType();

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

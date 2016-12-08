package cc.ethon.coldspot.common.type;

public class VoidType implements Type {

	public static final VoidType VOID = new VoidType();

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

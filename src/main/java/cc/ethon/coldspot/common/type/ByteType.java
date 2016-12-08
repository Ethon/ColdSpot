package cc.ethon.coldspot.common.type;

public class ByteType implements Type {

	public static final ByteType BYTE = new ByteType();

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

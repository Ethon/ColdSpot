package cc.ethon.coldspot.common.type;

public class ByteType implements Type {

	public static final ByteType BYTE = new ByteType();

	private ByteType() {

	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

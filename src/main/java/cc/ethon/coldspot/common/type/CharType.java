package cc.ethon.coldspot.common.type;

public class CharType implements Type {

	public static final CharType CHAR = new CharType();

	private CharType() {
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

package cc.ethon.coldspot.common.type;

public class ShortType implements Type {

	public static final ShortType SHORT = new ShortType();

	private ShortType() {

	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

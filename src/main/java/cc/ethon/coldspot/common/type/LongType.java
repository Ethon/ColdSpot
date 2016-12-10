package cc.ethon.coldspot.common.type;

public class LongType implements Type {

	public static final LongType LONG = new LongType();

	private LongType() {
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

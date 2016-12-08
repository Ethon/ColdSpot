package cc.ethon.coldspot.common.type;

public interface Type {

	public <T> T accept(TypeVisitor<T> visitor);

}

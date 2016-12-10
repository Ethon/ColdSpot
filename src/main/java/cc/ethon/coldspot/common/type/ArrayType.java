package cc.ethon.coldspot.common.type;

import java.util.IdentityHashMap;

public class ArrayType implements Type {

	private static final IdentityHashMap<Type, ArrayType> cache = new IdentityHashMap<Type, ArrayType>();

	private final Type subType;

	private ArrayType(Type subType) {
		this.subType = subType;
	}

	public static ArrayType typeOf(Type subType) {
		synchronized (cache) {
			ArrayType arrayType = cache.get(subType);
			if (arrayType == null) {
				arrayType = new ArrayType(subType);
				cache.put(subType, arrayType);
			}
			return arrayType;
		}
	}

	public Type getSubType() {
		return subType;
	}

	@Override
	public <T> T accept(TypeVisitor<T> visitor) {
		return visitor.accept(this);
	}

}

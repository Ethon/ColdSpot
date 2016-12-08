package cc.ethon.coldspot.common.type;

public interface TypeVisitor<T> {

	T accept(ByteType byteType);

	T accept(CharType charType);

	T accept(DoubleType doubleType);

	T accept(FloatType floatType);

	T accept(IntType intType);

	T accept(LongType longType);

	T accept(ClassType classType);

	T accept(ShortType shortType);

	T accept(VoidType voidType);

	T accept(BoolType boolType);

	T accept(ArrayType arrayType);

}

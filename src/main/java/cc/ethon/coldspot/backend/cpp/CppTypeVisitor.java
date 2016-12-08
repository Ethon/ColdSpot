package cc.ethon.coldspot.backend.cpp;

import cc.ethon.coldspot.common.type.ArrayType;
import cc.ethon.coldspot.common.type.BoolType;
import cc.ethon.coldspot.common.type.ByteType;
import cc.ethon.coldspot.common.type.CharType;
import cc.ethon.coldspot.common.type.ClassType;
import cc.ethon.coldspot.common.type.DoubleType;
import cc.ethon.coldspot.common.type.FloatType;
import cc.ethon.coldspot.common.type.IntType;
import cc.ethon.coldspot.common.type.LongType;
import cc.ethon.coldspot.common.type.ShortType;
import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.common.type.TypeVisitor;
import cc.ethon.coldspot.common.type.VoidType;

public class CppTypeVisitor implements TypeVisitor<String> {

	public static final CppTypeVisitor INSTANCE = new CppTypeVisitor();

	public static String getTypeName(Type type) {
		return type.accept(INSTANCE);
	}

	@Override
	public String accept(ClassType classType) {
		return CppClassNameUtil.makeQualifiedName(classType.getName()) + "*";
	}

	@Override
	public String accept(IntType intType) {
		return "std::int32_t";
	}

	@Override
	public String accept(VoidType voidType) {
		return "void";
	}

	@Override
	public String accept(ByteType byteType) {
		return "std::int8_t";
	}

	@Override
	public String accept(CharType charType) {
		return "std::int16_t";
	}

	@Override
	public String accept(DoubleType doubleType) {
		return "double";
	}

	@Override
	public String accept(FloatType floatType) {
		return "float";
	}

	@Override
	public String accept(LongType longType) {
		return "std::int64_t";
	}

	@Override
	public String accept(ShortType shortType) {
		return "std::int16_t";
	}

	@Override
	public String accept(BoolType boolType) {
		return "bool";
	}

	@Override
	public String accept(ArrayType arrayType) {
		return "coldspotrt::Array<" + getTypeName(arrayType.getSubType()) + ">*";
	}
}

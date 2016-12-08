package cc.ethon.coldspot.common;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;

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
import cc.ethon.coldspot.common.type.VoidType;

public class MethodSignature {

	private final int access;
	private final String name;
	private Type returnType;
	private List<Type> argumentTypes;

	private int parseType(String signature, int index, boolean isArgument) {
		if (index < 0 || index >= signature.length()) {
			throw new IllegalArgumentException();
		}

		Type type = null;
		switch (signature.charAt(index)) {
		case 'B':
			type = ByteType.BYTE;
			++index;
			break;
		case 'C':
			type = CharType.CHAR;
			++index;
			break;
		case 'D':
			type = DoubleType.DOUBLE;
			++index;
			break;
		case 'F':
			type = FloatType.FLOAT;
			++index;
			break;
		case 'I':
			type = IntType.INT;
			++index;
			break;
		case 'J':
			type = LongType.LONG;
			++index;
			break;
		case 'L':
			++index;
			final StringBuilder nameBuilder = new StringBuilder();
			for (; signature.charAt(index) != ';' && index < signature.length(); ++index) {
				nameBuilder.append(signature.charAt(index));
			}
			if (signature.charAt(index) == ';') {
				type = new ClassType(new ClassName(nameBuilder.toString()));
				++index;
			} else {
				throw new IllegalArgumentException();
			}
			break;
		case 'S':
			type = ShortType.SHORT;
			++index;
			break;
		case 'V':
			if (isArgument) {
				throw new IllegalArgumentException();
			}
			type = VoidType.VOID;
			++index;
			break;
		case 'Z':
			type = BoolType.BOOL;
			++index;
			break;
		case '[':
			// Hack - treat the subtype as argument and remove it from the
			// arguments.
			index = parseType(signature, index + 1, true);
			final Type subType = argumentTypes.remove(argumentTypes.size() - 1);
			type = new ArrayType(subType);
			break;

		default:
			throw new IllegalArgumentException();
		}

		if (isArgument) {
			argumentTypes.add(type);
		} else {
			returnType = type;
		}
		return index;
	}

	private void parseSignature(String signature) {
		final int endOfArguments = signature.indexOf(')');
		if (signature.charAt(0) != '(' || endOfArguments == -1) {
			throw new IllegalArgumentException();
		}

		this.argumentTypes = new ArrayList<Type>();
		for (int i = 1; i < endOfArguments;) {
			i = parseType(signature, i, true);
		}
		parseType(signature, endOfArguments + 1, false);
	}

	public MethodSignature(int access, String name, String signature) {
		this.access = access;
		this.name = name;
		parseSignature(signature);
	}

	public Type getReturnType() {
		return returnType;
	}

	public List<Type> getArgumentTypes() {
		return argumentTypes;
	}

	public int getAccess() {
		return access;
	}

	public String getName() {
		return name;
	}

	public boolean isPublic() {
		return (access & Opcodes.ACC_PUBLIC) != 0;
	}

	public boolean isProtected() {
		return (access & Opcodes.ACC_PROTECTED) != 0;
	}

	public boolean isPrivate() {
		return (access & Opcodes.ACC_PRIVATE) != 0;
	}

}

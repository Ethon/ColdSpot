package cc.ethon.coldspot.common.type;

import cc.ethon.coldspot.common.ClassName;

public final class TypeParsing {

	public static class ParsedType {
		public int nextSourceIndex;
		public Type type;
	}

	private TypeParsing() {
	}

	public static Type parseType(String input) {
		final ParsedType result = new ParsedType();
		parseNextType(input, 0, input.length(), result);
		return result.type;
	}

	public static void parseNextType(String input, int index, int end, ParsedType result) {
		if (index < 0 || index > end || end > input.length()) {
			throw new IllegalArgumentException();
		}

		Type type = null;
		switch (input.charAt(index)) {
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
			for (; input.charAt(index) != ';' && index < end; ++index) {
				nameBuilder.append(input.charAt(index));
			}
			if (input.charAt(index) == ';') {
				type = ClassType.typeOf(new ClassName(nameBuilder.toString()));
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
			type = VoidType.VOID;
			++index;
			break;
		case 'Z':
			type = BoolType.BOOL;
			++index;
			break;
		case '[':
			final ParsedType nextResult = new ParsedType();
			parseNextType(input, index + 1, end, nextResult);
			type = ArrayType.typeOf(nextResult.type);
			index = nextResult.nextSourceIndex;
			break;

		default:
			final String start = input.substring(index);
			throw new UnsupportedOperationException("Unknown type starting at '" + start + "'");
		}

		result.type = type;
		result.nextSourceIndex = index;
	}

}

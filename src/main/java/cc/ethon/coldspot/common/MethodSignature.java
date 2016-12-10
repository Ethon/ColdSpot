package cc.ethon.coldspot.common;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;

import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.common.type.TypeParsing;
import cc.ethon.coldspot.common.type.TypeParsing.ParsedType;

public class MethodSignature {

	private final int access;
	private final String name;
	private Type returnType;
	private List<Type> argumentTypes;

	private void parseSignature(String signature) {
		final int endOfArguments = signature.indexOf(')');
		if (signature.charAt(0) != '(' || endOfArguments == -1) {
			throw new IllegalArgumentException();
		}

		this.argumentTypes = new ArrayList<Type>();
		final ParsedType parseResult = new ParsedType();
		for (int i = 1; i < endOfArguments;) {
			TypeParsing.parseNextType(signature, i, endOfArguments, parseResult);
			argumentTypes.add(parseResult.type);
			i = parseResult.nextSourceIndex;
		}
		TypeParsing.parseNextType(signature, endOfArguments + 1, signature.length(), parseResult);
		this.returnType = parseResult.type;
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

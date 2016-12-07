package cc.ethon.coldspot.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassVisitor;

import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.frontend.ast.ClassNode;

class AstBuildingClassVisitor extends ClassVisitor {

	private ClassNode classNode;

	public AstBuildingClassVisitor() {
		super(ColdSpotConstants.SUPPORTED_ASM_API);
	}

	public ClassNode getClassNode() {
		return classNode;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		final ClassName className = new ClassName(name);
		final ClassName superClassName = new ClassName(superName);
		final List<ClassName> interfacesNames = Arrays.stream(interfaces).map(ifName -> new ClassName(ifName)).collect(Collectors.toList());
		classNode = new ClassNode(className, superClassName, interfacesNames);
	}

}

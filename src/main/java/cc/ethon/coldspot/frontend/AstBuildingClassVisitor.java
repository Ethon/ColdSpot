package cc.ethon.coldspot.frontend;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.frontend.ast.ClassNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;

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

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		final MethodSignature methodSignature = new MethodSignature(access, name, desc);
		final MethodNode node = new MethodNode(classNode, methodSignature);
		if (methodSignature.isPublic()) {
			classNode.getPublicMethods().add(node);
		} else if (methodSignature.isProtected()) {
			classNode.getProtectedMethods().add(node);
		} else if (methodSignature.isPrivate()) {
			classNode.getPrivateMethods().add(node);
		} else {
			// Treat package-local methods as public methods.
			classNode.getPublicMethods().add(node);
		}
		return new AstBuildingMethodVisitor(node);
	}

}

package cc.ethon.coldspot.frontend;

import java.io.IOException;

import org.objectweb.asm.ClassReader;

import cc.ethon.coldspot.frontend.ast.ClassNode;

public class AstBuilder {

	public ClassNode buildClass(String qualifiedName) throws IOException {
		final AstBuildingClassVisitor classVisitor = new AstBuildingClassVisitor();
		new ClassReader(qualifiedName).accept(classVisitor, 0);
		return classVisitor.getClassNode();
	}

}

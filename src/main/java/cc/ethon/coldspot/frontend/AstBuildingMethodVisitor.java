package cc.ethon.coldspot.frontend;

import org.objectweb.asm.MethodVisitor;

import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.frontend.ast.ClassNode;

class AstBuildingMethodVisitor extends MethodVisitor {

	private final ClassNode parent;

	public AstBuildingMethodVisitor(ClassNode parent) {
		super(ColdSpotConstants.SUPPORTED_ASM_API);
		this.parent = parent;
	}
}

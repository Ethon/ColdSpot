package cc.ethon.coldspot.frontend;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.common.type.TypeParsing;
import cc.ethon.coldspot.frontend.ast.MethodNode;

class AstBuildingMethodVisitor extends MethodVisitor {

	private final MethodNode methodNode;
	private LabelManager labels;
	private LocalVariableManager locals;
	private BasicBlockBuilder basicBlockBuilder;
	private InstructionCodeBuilder instructionCodeBuilder;

	public AstBuildingMethodVisitor(MethodNode method) {
		super(ColdSpotConstants.SUPPORTED_ASM_API);
		this.methodNode = method;
	}

	@Override
	public void visitCode() {
		labels = new LabelManager();
		locals = new LocalVariableManager(labels);
		basicBlockBuilder = new BasicBlockBuilder();
		instructionCodeBuilder = new InstructionCodeBuilder(basicBlockBuilder, locals);

		locals.addMethodArguments(methodNode);
	}

	@Override
	public void visitEnd() {
		methodNode.setBody(basicBlockBuilder.compile());
	}

	@Override
	public void visitLabel(Label label) {
		labels.addLabel(label, instructionCodeBuilder.getInstructionIndex());
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		labels.setLabelLineNumber(start, line);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		locals.updateNameAndType(index, start, end, name, TypeParsing.parseType(desc));
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		methodNode.setMaxStack(maxStack);
		methodNode.setMaxLocals(maxLocals);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		instructionCodeBuilder.handleVariableExpression(var);
	}

	@Override
	public void visitInsn(int opcode) {
		switch (opcode) {
		case Opcodes.IADD:
		case Opcodes.LADD:
		case Opcodes.FADD:
		case Opcodes.DADD:
			instructionCodeBuilder.handleBinaryExpression("+");
			break;

		case Opcodes.IRETURN:
		case Opcodes.LRETURN:
		case Opcodes.FRETURN:
		case Opcodes.DRETURN:
		case Opcodes.ARETURN:
		case Opcodes.RETURN:
			instructionCodeBuilder.handleReturn(opcode != Opcodes.RETURN);
			break;

		default:
			throw new UnsupportedOperationException("Unsupported opcode " + opcode);
		}
	}
}

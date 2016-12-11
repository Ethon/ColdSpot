package cc.ethon.coldspot.frontend;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.common.type.TypeParsing;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.errors.InvalidLocalVariableException;

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
		locals.resolve(index, start, end, basicBlockBuilder);
		locals.updateNameAndType(index, start, end, name, TypeParsing.parseType(desc));
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		methodNode.setMaxStack(maxStack);
		methodNode.setMaxLocals(maxLocals);
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		try {
			instructionCodeBuilder.handleIncrement(var, increment);
		} catch (final InvalidLocalVariableException e) {
			throw new RuntimeException("Error processing iinc instruction", e);
		}
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		try {
			switch (opcode) {
			case Opcodes.IF_ICMPEQ:
				instructionCodeBuilder.handleConditionalBinaryJump("==", label);
				break;
			case Opcodes.IF_ICMPNE:
				instructionCodeBuilder.handleConditionalBinaryJump("!=", label);
				break;
			case Opcodes.IF_ICMPLT:
				instructionCodeBuilder.handleConditionalBinaryJump("<", label);
				break;
			case Opcodes.IF_ICMPGE:
				instructionCodeBuilder.handleConditionalBinaryJump(">=", label);
				break;
			case Opcodes.IF_ICMPGT:
				instructionCodeBuilder.handleConditionalBinaryJump(">", label);
				break;
			case Opcodes.IF_ICMPLE:
				instructionCodeBuilder.handleConditionalBinaryJump("<=", label);
				break;

			default:
				throw new UnsupportedOperationException("Unsupported jumpinsn opcode " + opcode);
			}
		} catch (final Exception e) {
			throw new RuntimeException("Error processing jump instruction", e);
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		try {
			switch (opcode) {
			case Opcodes.ILOAD:
			case Opcodes.LLOAD:
			case Opcodes.FLOAD:
			case Opcodes.DLOAD:
			case Opcodes.ALOAD:
				instructionCodeBuilder.handleLocalVariableLoad(var);
				break;

			case Opcodes.ISTORE:
			case Opcodes.LSTORE:
			case Opcodes.FSTORE:
			case Opcodes.DSTORE:
			case Opcodes.ASTORE:
				instructionCodeBuilder.handleLocalVariableStore(var);
				break;

			default:
				throw new UnsupportedOperationException("Unsupported varinsn opcode " + opcode);
			}
		} catch (final Exception e) {
			throw new RuntimeException("Error processing variable instruction", e);
		}
	}

	@Override
	public void visitInsn(int opcode) {
		switch (opcode) {

		case Opcodes.ICONST_M1:
			instructionCodeBuilder.handleIntLiteralExpression(-1);
			break;
		case Opcodes.ICONST_0:
			instructionCodeBuilder.handleIntLiteralExpression(0);
			break;
		case Opcodes.ICONST_1:
			instructionCodeBuilder.handleIntLiteralExpression(1);
			break;
		case Opcodes.ICONST_2:
			instructionCodeBuilder.handleIntLiteralExpression(2);
			break;
		case Opcodes.ICONST_3:
			instructionCodeBuilder.handleIntLiteralExpression(3);
			break;
		case Opcodes.ICONST_4:
			instructionCodeBuilder.handleIntLiteralExpression(4);
			break;
		case Opcodes.ICONST_5:
			instructionCodeBuilder.handleIntLiteralExpression(5);
			break;

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
			throw new UnsupportedOperationException("Unsupported insn opcode " + opcode);
		}
	}
}

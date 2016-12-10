package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cc.ethon.coldspot.common.ColdSpotConstants;
import cc.ethon.coldspot.frontend.BasicBlock.LeftBy;
import cc.ethon.coldspot.frontend.ast.BinaryExpressionNode;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;

class AstBuildingMethodVisitor extends MethodVisitor {

	private static class LabelPosition {
		private final int instructionIndex;
		private int line;

		public LabelPosition(int instructionIndex) {
			super();
			this.instructionIndex = instructionIndex;
			this.line = -1;
		}

		public int getInstructionIndex() {
			return instructionIndex;
		}

		@SuppressWarnings("unused")
		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

	}

	private final MethodNode method;
	@SuppressWarnings("unused")
	private Label firstLabel, lastLabel;
	private IdentityHashMap<Label, LabelPosition> labels;
	private int instructionIndex;
	private List<BasicBlock> basicBlocks;
	private boolean isBasicBlockFinished;
	private Stack<ExpressionNode> expressionStack;
	private Map<Integer, List<VariableDeclarationStatementNode>> locals;

	private boolean isInstructionIndexBetweenLabels(Label start, Label end, int instructionIndex) {
		final LabelPosition startPos = labels.get(start);
		final LabelPosition endPos = labels.get(end);
		return instructionIndex >= startPos.getInstructionIndex() && instructionIndex < endPos.getInstructionIndex();
	}

	private VariableDeclarationStatementNode getVariableDeclarationForIndex(int localIndex, int instructionIndex) {
		final List<VariableDeclarationStatementNode> variables = locals.get(localIndex);
		if (variables == null || variables.isEmpty()) {
			throw new IllegalArgumentException();
		}
		VariableDeclarationStatementNode bestMatch = null;
		for (final VariableDeclarationStatementNode cur : variables) {
			if (cur.getInstructionIndex() <= instructionIndex) {
				if (bestMatch != null) {
					bestMatch = cur.getInstructionIndex() > bestMatch.getInstructionIndex() ? cur : bestMatch;
				} else {
					bestMatch = cur;
				}
			}
		}
		if (bestMatch == null) {
			throw new IllegalArgumentException();
		}
		return bestMatch;
	}

	private void addLocal(int localIndex, VariableDeclarationStatementNode decl) {
		List<VariableDeclarationStatementNode> forIndex = locals.get(localIndex);
		if (forIndex == null) {
			forIndex = new ArrayList<VariableDeclarationStatementNode>();
			locals.put(localIndex, forIndex);
		}
		forIndex.add(decl);
	}

	private void addStatement(StatementNode statement) {
		if (isBasicBlockFinished) {
			basicBlocks.add(new BasicBlock(new StatementBlock(instructionIndex)));
		}
		basicBlocks.get(basicBlocks.size() - 1).getStatements().getStatements().add(statement);
	}

	private void finishBasicBlock() {
		isBasicBlockFinished = true;
	}

	private void handleBinaryExpression(String operator) {
		final ExpressionNode right = expressionStack.pop();
		final ExpressionNode left = expressionStack.pop();
		expressionStack.push(new BinaryExpressionNode(instructionIndex, left, right, operator));
	}

	private void handleReturn(boolean withResult) {
		Optional<ExpressionNode> result = Optional.empty();
		if (withResult) {
			result = Optional.of(expressionStack.pop());
		}
		addStatement(new ReturnStatementNode(instructionIndex, result));
		basicBlocks.get(basicBlocks.size() - 1).setLeftBy(LeftBy.RETURN);
		finishBasicBlock();
	}

	public AstBuildingMethodVisitor(MethodNode method) {
		super(ColdSpotConstants.SUPPORTED_ASM_API);
		this.method = method;
	}

	@Override
	public void visitCode() {
		labels = new IdentityHashMap<Label, LabelPosition>();
		instructionIndex = 0;
		basicBlocks = new ArrayList<BasicBlock>();
		basicBlocks.add(new BasicBlock(new StatementBlock(instructionIndex)));
		expressionStack = new Stack<ExpressionNode>();
		locals = new HashMap<Integer, List<VariableDeclarationStatementNode>>();

		for (int i = 0; i < method.getArguments().size(); i++) {
			final VariableDeclarationStatementNode variableDeclarationNode = method.getArguments().get(i);
			addLocal(i, variableDeclarationNode);
		}
	}

	@Override
	public void visitEnd() {
		method.setBody(basicBlocks.get(basicBlocks.size() - 1).getStatements());
	}

	@Override
	public void visitLabel(Label label) {
		labels.put(label, new LabelPosition(instructionIndex));
		if (firstLabel == null) {
			firstLabel = label;
		}
		lastLabel = null;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		labels.get(start).setLine(line);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		// Also update variable declarations.
		final List<VariableDeclarationStatementNode> decls = locals.get(index);
		if (decls == null || decls.isEmpty()) {
			return;
		}
		for (final VariableDeclarationStatementNode decl : decls) {
			if (isInstructionIndexBetweenLabels(start, end, decl.getInstructionIndex())) {
				decl.setName(name);
			}
		}
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		method.setMaxStack(maxStack);
		method.setMaxLocals(maxLocals);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		final VariableDeclarationStatementNode decl = getVariableDeclarationForIndex(var, instructionIndex);
		final VariableExpressionNode variableExpressionNode = new VariableExpressionNode(instructionIndex, decl);
		expressionStack.push(variableExpressionNode);
		++instructionIndex;
	}

	@Override
	public void visitInsn(int opcode) {
		switch (opcode) {
		case Opcodes.IADD:
		case Opcodes.LADD:
		case Opcodes.FADD:
		case Opcodes.DADD:
			handleBinaryExpression("+");
			break;

		case Opcodes.IRETURN:
		case Opcodes.LRETURN:
		case Opcodes.FRETURN:
		case Opcodes.DRETURN:
		case Opcodes.ARETURN:
		case Opcodes.RETURN:
			handleReturn(opcode != Opcodes.RETURN);
			break;

		default:
			throw new UnsupportedOperationException("Unsupported opcode " + opcode);
		}
		++instructionIndex;
	}
}

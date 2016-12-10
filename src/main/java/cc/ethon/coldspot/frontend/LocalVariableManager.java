package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.objectweb.asm.Label;

import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;

class LocalVariableManager {

	private final LabelManager labels;
	private final Map<Integer, List<VariableDeclarationStatementNode>> locals;
	private final Map<Integer, List<VariableDeclarationStatementNode>> unresolved;

	public LocalVariableManager(LabelManager labels) {
		this.labels = labels;
		locals = new HashMap<Integer, List<VariableDeclarationStatementNode>>();
		unresolved = new HashMap<Integer, List<VariableDeclarationStatementNode>>();
	}

	public Optional<VariableDeclarationStatementNode> getVariableDeclarationForIndex(int localIndex, int instructionIndex) {
		final List<VariableDeclarationStatementNode> variables = locals.get(localIndex);
		if (variables == null || variables.isEmpty()) {
			return Optional.empty();
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
		return Optional.ofNullable(bestMatch);
	}

	public void addLocal(int localIndex, VariableDeclarationStatementNode decl) {
		List<VariableDeclarationStatementNode> forIndex = locals.get(localIndex);
		if (forIndex == null) {
			forIndex = new ArrayList<VariableDeclarationStatementNode>();
			locals.put(localIndex, forIndex);
		}
		forIndex.add(decl);
	}

	public VariableDeclarationStatementNode addUnresolvedLocal(int instructionIndex, int localIndex) {
		final VariableDeclarationStatementNode decl = new VariableDeclarationStatementNode(instructionIndex);
		decl.setName("unresolved" + localIndex);
		addLocal(localIndex, decl);

		List<VariableDeclarationStatementNode> forIndex = unresolved.get(localIndex);
		if (forIndex == null) {
			forIndex = new ArrayList<VariableDeclarationStatementNode>();
			unresolved.put(localIndex, forIndex);
		}
		forIndex.add(decl);
		return decl;
	}

	public void addMethodArguments(MethodNode methodNode) {
		for (int i = 0; i < methodNode.getArguments().size(); i++) {
			final VariableDeclarationStatementNode variableDeclarationNode = methodNode.getArguments().get(i);
			addLocal(i, variableDeclarationNode);
		}
	}

	public void updateNameAndType(int localIndex, Label start, Label end, String name, Type type) {
		final List<VariableDeclarationStatementNode> decls = locals.get(localIndex);
		if (decls == null || decls.isEmpty()) {
			return;
		}
		for (final VariableDeclarationStatementNode decl : decls) {
			if (labels.isInstructionIndexBetweenLabels(start, end, decl.getInstructionIndex())) {
				decl.setName(name);
				decl.setType(type);
			}
		}
	}

	public void resolve(int localIndex, Label start, Label end, BasicBlockBuilder basicBlockBuilder) {
		final List<VariableDeclarationStatementNode> decls = unresolved.get(localIndex);
		if (decls == null || decls.isEmpty()) {
			return;
		}
		final List<VariableDeclarationStatementNode> toRemove = new ArrayList<VariableDeclarationStatementNode>();
		for (final VariableDeclarationStatementNode decl : decls) {
			if (labels.isInstructionIndexBetweenLabels(start, end, decl.getInstructionIndex())) {
				toRemove.add(decl);
				final int startInstructionIndex = labels.getInstructionIndexForLabel(start);
				decl.updateInstructionIndex(startInstructionIndex);
				basicBlockBuilder.injectStatementBefore(decl);
			}
		}
		decls.removeAll(toRemove);
	}

}

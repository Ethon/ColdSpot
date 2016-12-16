package cc.ethon.coldspot.frontend;

import java.util.IdentityHashMap;

import org.objectweb.asm.Label;

class LabelManager {

	static class LabelPosition {
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

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		@Override
		public String toString() {
			return "LabelPosition [instructionIndex=" + instructionIndex + ", line=" + line + "]";
		}

	}

	private Label firstLabel, lastLabel;
	private final IdentityHashMap<Label, LabelPosition> labels;

	public LabelManager() {
		labels = new IdentityHashMap<Label, LabelManager.LabelPosition>();
	}

	public Label getFirstLabel() {
		return firstLabel;
	}

	public Label getLastLabel() {
		return lastLabel;
	}

	public int getInstructionIndexForLabel(Label label) {
		return labels.get(label).instructionIndex;
	}

	public void addLabel(Label label, int instructionIndex) {
		labels.put(label, new LabelPosition(instructionIndex));
		if (firstLabel == null) {
			firstLabel = label;
		}
		lastLabel = label;
	}

	public void setLabelLineNumber(Label label, int lineNumber) {
		labels.get(label).setLine(lineNumber);
	}

	public boolean isInstructionIndexBetweenLabels(Label start, Label end, int instructionIndex) {
		final LabelPosition startPos = labels.get(start);
		final LabelPosition endPos = labels.get(end);
		return instructionIndex >= startPos.getInstructionIndex() && instructionIndex < endPos.getInstructionIndex();
	}

}

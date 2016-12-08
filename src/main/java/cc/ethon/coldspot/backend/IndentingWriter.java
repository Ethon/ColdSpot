package cc.ethon.coldspot.backend;

import java.io.PrintWriter;

public class IndentingWriter {

	private final PrintWriter writer;
	private int indentLevel;

	private void indent() {
		for (int i = 0; i < indentLevel; ++i) {
			writer.print('\t');
		}
	}

	public IndentingWriter(PrintWriter writer) {
		super();
		this.writer = writer;
		indentLevel = 0;
	}

	public void increaseIndentation() {
		++indentLevel;
	}

	public void decreaseIndentation() {
		--indentLevel;
	}

	public void print(String text) {
		indent();
		writer.print(text);
	}

	public void printUnindented(String text) {
		writer.print(text);
	}

	public void println(String line) {
		indent();
		writer.println(line);
	}

	public void println() {
		writer.println();
	}

	public void printlnUnindented(String line) {
		writer.println(line);
	}

}

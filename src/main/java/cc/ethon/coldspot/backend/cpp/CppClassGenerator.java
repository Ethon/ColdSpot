package cc.ethon.coldspot.backend.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import cc.ethon.coldspot.backend.ClassGenerator;
import cc.ethon.coldspot.backend.IndentingWriter;
import cc.ethon.coldspot.frontend.ast.ClassNode;

public class CppClassGenerator extends ClassGenerator {

	private final GenerationSettings settings;

	private void generateHeaderFile(ClassNode classNode) throws FileNotFoundException {
		final File dest = getFileForClass(classNode.getName(), ".hpp");
		try (PrintWriter printWriter = new PrintWriter(dest)) {
			final IndentingWriter writer = new IndentingWriter(printWriter);
			new CppHeaderGeneratingVisitor(writer, settings).visit(classNode);
		}
	}

	private void generateSourceFile(ClassNode classNode) throws FileNotFoundException {
		final File dest = getFileForClass(classNode.getName(), ".cpp");
		try (PrintWriter printWriter = new PrintWriter(dest)) {
			final IndentingWriter writer = new IndentingWriter(printWriter);
			new CppSourceFileGeneratingVisitor(writer, settings).visit(classNode);
		}
	}

	public CppClassGenerator(File baseDir, GenerationSettings settings) {
		super(baseDir);
		this.settings = settings;
	}

	@Override
	public void generate(ClassNode classNode) throws Exception {
		generateHeaderFile(classNode);
		generateSourceFile(classNode);
	}

}

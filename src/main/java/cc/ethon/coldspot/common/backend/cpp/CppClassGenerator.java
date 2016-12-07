package cc.ethon.coldspot.common.backend.cpp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import cc.ethon.coldspot.common.backend.ClassGenerator;
import cc.ethon.coldspot.common.backend.IndentingWriter;
import cc.ethon.coldspot.frontend.ast.ClassNode;

public class CppClassGenerator extends ClassGenerator {

	private void generateHeaderFile(ClassNode classNode) throws FileNotFoundException {
		final File dest = getFileForClass(classNode.getName(), ".hpp");
		try (PrintWriter printWriter = new PrintWriter(dest)) {
			final IndentingWriter writer = new IndentingWriter(printWriter);
			new CppHeaderGeneratingVisitor(writer).visit(classNode);
		}
	}

	private void generateSourceFile(ClassNode classNode) throws FileNotFoundException {
		final File dest = getFileForClass(classNode.getName(), ".cpp");
		try (PrintWriter printWriter = new PrintWriter(dest)) {
			final IndentingWriter writer = new IndentingWriter(printWriter);
			new CppSourceFileGeneratingVisitor(writer).visit(classNode);
		}
	}

	public CppClassGenerator(File baseDir) {
		super(baseDir);
	}

	@Override
	public void generate(ClassNode classNode) throws Exception {
		generateHeaderFile(classNode);
		generateSourceFile(classNode);
	}

}

package cc.ethon.coldspot.common.backend;

import java.io.File;

import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.frontend.ast.ClassNode;

public abstract class ClassGenerator {

	protected File baseDir;

	protected File getFileForClass(ClassName className, String suffix) {
		final File directory = new File(baseDir, className.getPackageAsPath());
		directory.mkdirs();
		final File file = new File(directory, className.getName() + suffix);
		return file.getAbsoluteFile();
	}

	public ClassGenerator(File baseDir) {
		if (!baseDir.isDirectory()) {
			throw new IllegalArgumentException("Base directory '" + baseDir + "' for class generation is no directory");
		}
		this.baseDir = baseDir;
	}

	public abstract void generate(ClassNode classNode) throws Exception;

}

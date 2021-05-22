import java.io.Console;
import java.io.File;

/**
 * Provides data fields and methods to receive path to README file from user.
 *
 * @author Bhavyai Gupta
 * @since 1.0
 */
public class App {
	/**
	 * The file created based on user input would be stored here.
	 */
	private File readmeFile;
	private Console console;

	/**
	 * Constructs a App object and initializes the console.
	 */
	public App() {
		this.console = System.console();

		if (this.console == null) {
			System.err.println("%n%nApplication not started in console");
			Runtime.getRuntime().exit(1);
		}
	}

	/**
	 * Get a string representing a valid README file from the user.
	 */
	void getValidString() {
		String temp = null;

		while (true) {
			temp = this.console.readLine("%n%nEnter the full path of the markdown file: ");

			if (isValidFile(temp)) {
				return;
			}

			this.console.printf("%n%nPlease enter a valid markdown file name with .md or .markdown extension");
		}
	}

	/**
	 * Check if the string passed to this function is a valid markdown file.
	 *
	 * @param readMePath
	 * @return {@code true} is file is valid markdown file, {@code false} otherwise
	 */
	boolean isValidFile(String readMePath) {
		this.readmeFile = new File(readMePath);

		if (readmeFile.isFile() && (readmeFile.getAbsolutePath().endsWith(".md")
				|| readmeFile.getAbsolutePath().endsWith(".markdown"))) {
			return true;
		}

		else {
			return false;
		}
	}

	public static void main(String[] args) {
		App a = new App();

		if (args.length == 0) {
			a.getValidString();
		}

		else if (!a.isValidFile(args[0])) {
			a.getValidString();
		}

		TocGenerator toc = new TocGenerator();
		toc.generate(a.readmeFile);
	}
}

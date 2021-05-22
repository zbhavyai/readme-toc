import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.Console;

/**
 * Provides methods to analyze the README file and prints the Table of Contents
 * that can be used to navigate internally within the README file
 *
 * @author Bhavyai Gupta
 * @since 1.0
 */
public class TocGenerator {
	private Console console;
	/**
	 * <code>true</code> if there has been atleast one flag raised by the program;
	 * <code>false</code> otherwise
	 */
	private boolean atleastOne;
	private boolean codeMode;
	// private int indentationLevel;

	/**
	 * Constructs a TocGenerator object with default values of the data fields.
	 */
	TocGenerator() {
		console = System.console();
		atleastOne = false;
	}

	/**
	 * Loop over all the lines of the README file, and pass each line to
	 * lineAnalyzer(). Print start and end messages.
	 *
	 * @param readmeFile The <code>File</code> pointing to the .py file
	 */
	public void generate(File readmeFile) {
		int lineCount = 0;
		try {
			Scanner sc = new Scanner(new BufferedReader(new FileReader(readmeFile)));

			while (sc.hasNextLine()) {
				this.lineAnalyzer(sc.nextLine(), ++lineCount);
			}

			if (atleastOne == false) {
				console.printf("%n%n## Table of Contents%n%n");
			}

			else {
				console.printf("%n%nNo headings in the README%n%n");
			}

			sc.close();
		}

		catch (FileNotFoundException e) {
			console.printf("%n%nFile not found! Exit%n");
			System.exit(1);
		}
	}

	private boolean checkCodeMode(String str) {
		/*
		 * spaces are checked for code first as spaces can override ``` behavior and
		 * make them as an inline-code itself, rather than starting or ending a code
		 * block
		 *
		 * So, 3 backticks with leading 4 or more spaces won't toggle the code block
		 *
		 */

		// check for one line code (starting with four or more spaces)
		if (str.indexOf("    ") != -1) {
			return true;
		}

		// check for block of code (bounded by three backticks) and toggle the codeMode
		// this must be checked to ignore leading #'s in the code
		if (str.trim().indexOf("```") == 0) {
			if (this.codeMode == false) {
				this.codeMode = true;
			}

			else {
				this.codeMode = false;
			}
		}

		return this.codeMode;
	}

	/**
	 * Loop over all the characters of the String
	 *
	 * @param str       The string passed to be analyzed by the function
	 * @param lineCount The current line number in the file
	 */
	private void lineAnalyzer(String str, int lineCount) {
		// if line is empty, skip it
		if (str.length() == 0) {
			return;
		}

		// if the line is part of a code snippet, skip it
		if (checkCodeMode(str)) {
			return;
		}

		// remove the spaces to begin further operations
		str = str.trim();

		// int headingLevel = 0; // count the heading level by counting number of #
		if (checkHeading(str) == -1)
			return;

		else {
			this.console.printf("%s%n", createLink(str.substring(checkHeading(str) + 1), checkHeading(str)));
		}

	}

	/**
	 * Check if the string is a heading by checking the leading number of pounds
	 * followed by atleast one space
	 *
	 * @param str the string to be checked for heading
	 * @return integer representing the heading level, -1 is string is not a heading
	 */
	private int checkHeading(String str) {
		int isHeading = -1;

		if (str.indexOf("# ") == 0)
			isHeading = 1;

		else if (str.indexOf("## ") == 0)
			isHeading = 2;

		else if (str.indexOf("### ") == 0)
			isHeading = 3;

		else if (str.indexOf("#### ") == 0)
			isHeading = 4;

		else if (str.indexOf("##### ") == 0)
			isHeading = 5;

		else if (str.indexOf("###### ") == 0)
			isHeading = 6;

		return isHeading;
	}

	public String createLink(String str, int level) {
		// try something like in python -> str * level = strstrstr
		String indent = null;
		switch (level) {
			case 1:
				indent = "";
				break;
			case 2:
				indent = "   ";
				break;
			case 3:
				indent = "      ";
				break;
			case 4:
				indent = "         ";
				break;
			case 5:
				indent = "            ";
				break;
			case 6:
				indent = "               ";
				break;
		}

		// modify this replace with regex (like multiple spaces to single dash -)
		// replace other characters as well
		String temp = str.toLowerCase().replace(" ", "-");

		String link = String.format("%s1. [%s](#%s)", indent, str, temp);

		return link;
	}
}

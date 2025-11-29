/*
 * jsh.java
 * Author: Jacob Francis
 * Date: 11/03/2025
 * 
 * Description:
 * A Java shell program that lets you type commands just like a normal terminal.
 * It runs the commands in their own process (outside the JVM), shows their output,
 * and supports changing directories with "cd". Keeps running until you stop it or type exit.
 * 
 * Type 'javac jsh.java' in terminal to compile the program and 'java jsh' to run the shell.
 */

import java.io.*;

public class jsh {
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		// start off in whatever folder this program was launched from
		File currentDirectory = new File(System.getProperty("user.dir"));

		// main loop – keeps asking for commands until you stop it
		while (true) {
			System.out.print("jsh> "); // prompt
			commandLine = console.readLine();

			// skip empty input (just pressing enter)
			if (commandLine == null || commandLine.trim().equals(""))
				continue;

			// quick way to exit without hitting Ctrl+C
			if (commandLine.equals("exit") || commandLine.equals("quit")) {
				System.out.println("Exiting jsh...");
				break;
			}

			// *** (1) Split input into parts ***
			String[] tokens = commandLine.trim().split("\\s+");
			String command = tokens[0];

			// *** (2) Handle "cd" command ***
			if (command.equals("cd")) {
				if (tokens.length < 2) {
					System.out.println("Usage: cd <directory>");
				} else {
					File newDir = new File(tokens[1]);

					// make it relative to current dir if not absolute
					if (!newDir.isAbsolute()) {
						newDir = new File(currentDirectory, tokens[1]);
					}

					// actually change dir if it exists
					if (newDir.exists() && newDir.isDirectory()) {
						currentDirectory = newDir.getCanonicalFile();
					} else {
						System.out.println("Directory not found: " + tokens[1]);
					}
				}
				continue; // skip rest since cd doesn’t start a process
			}

			// *** (3) Run external commands ***
			try {
				ProcessBuilder pb = new ProcessBuilder(tokens); // ProcessBuilder.command()
				pb.directory(currentDirectory); // ProcessBuilder.directory()
				pb.redirectErrorStream(true); // combine stdout + stderr

				// *** (4) Start process ***
				Process process = pb.start(); // ProcessBuilder.start()

				// *** (5) Print the command output ***
				BufferedReader output = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = output.readLine()) != null) {
					System.out.println(line);
				}

				// wait for process to finish before showing prompt again
				process.waitFor();
			} catch (IOException e) {
				System.out.println("Command not found or failed: " + command);
			} catch (InterruptedException e) {
				System.out.println("Process interrupted.");
			}
		}
	}
}

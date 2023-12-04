package cs2043group10.test;

import java.util.Base64;
import java.util.Scanner;

import cs2043group10.misc.PasswordHasher;

public class PasswordHasherCLI {

	public static void main(String[] args) throws Exception {
		PasswordHasher hasher = new PasswordHasher();
		Scanner in = new Scanner(System.in);
		while (true) {			
			String ln = in.nextLine();
			ln = ln.strip();
			byte[] salt = hasher.generateSalt();
			byte[] hash = hasher.hashPassword(ln, salt);
			System.out.println(Base64.getEncoder().encodeToString(hash));
		}
	}
}

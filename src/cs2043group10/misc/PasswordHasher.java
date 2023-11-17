package cs2043group10.misc;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
	private final SecretKeyFactory keyFactory;
	private final SecureRandom rng;
	private static final int NUM_ITERATIONS = 1000;
	
	public PasswordHasher() throws NoSuchAlgorithmException {
		keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		rng = SecureRandom.getInstance("SHA1PRNG");
	}
	
	public byte[] generateSalt() {
		byte[] salt = new byte[16];
		rng.nextBytes(salt);
		return salt;
	}
	
	public byte[] hashPassword(String password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, NUM_ITERATIONS, 1024);
		byte[] hash;
		try {
			hash = keyFactory.generateSecret(spec).getEncoded();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
		byte[] total = new byte[salt.length + hash.length];
		System.arraycopy(salt, 0, total, 0, salt.length);
		System.arraycopy(hash, 0, total, salt.length, hash.length);
		return total;
	}
	
	public static byte[] getSaltFromHash(byte[] hash) {
		byte[] salt = new byte[16];
		System.arraycopy(hash, 0, salt, 0, 16);
		return salt;
	}
}

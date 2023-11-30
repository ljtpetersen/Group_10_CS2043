package cs2043group10.misc;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * This class hashes plaintext passwords in a one-way process.
 * 
 * @author James Petersen
 */
public class PasswordHasher {
	/**
	 * The key factory used to generate the hash.
	 */
	private final SecretKeyFactory keyFactory;
	/**
	 * The random number generator used to generate the salt.
	 */
	private final SecureRandom rng;
	/**
	 * The number of iterations to be done when hashing passwords.
	 */
	private static final int NUM_ITERATIONS = 1000;
	
	/**
	 * Initialize a new password hasher.
	 * @throws NoSuchAlgorithmException
	 */
	public PasswordHasher() throws NoSuchAlgorithmException {
		keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		rng = SecureRandom.getInstance("SHA1PRNG");
	}
	
	/**
	 * Randomly generate a salt for a password hash.
	 * @return The salt.
	 */
	public byte[] generateSalt() {
		byte[] salt = new byte[16];
		rng.nextBytes(salt);
		return salt;
	}
	
	/**
	 * Hash a given password.
	 * @param password The password to hash.
	 * @param salt The salt with which the password should be hashed.
	 * @return The hashed password.
	 */
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
	
	/**
	 * Get the salt from a hashed password.
	 * @param hash The hashed password.
	 * @return The salt.
	 */
	public static byte[] getSaltFromHash(byte[] hash) {
		byte[] salt = new byte[16];
		System.arraycopy(hash, 0, salt, 0, 16);
		return salt;
	}
}

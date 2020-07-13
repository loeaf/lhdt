package lhdt.security;

import org.junit.jupiter.api.Test;

public class CryptTest {

	/**
	 * 암복호화 테스트
	 */
	@Test
	public void 암복호화() {
		System.out.println("url : " + Crypt.encrypt("jdbc:postgresql://localhost:15432/lhdt"));
		System.out.println("user : " + Crypt.encrypt("postgres"));
		System.out.println("password : " + Crypt.encrypt("postgres"));
		
//		System.out.println(Crypt.decrypt("GvEa084OoJKPfNVpNHbfs/KHGXnmV1yqVqZU7yr5tl8KYzgd7JnC4jBQstOh1a5b"));
	}
}

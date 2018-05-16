//package encryption;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.Test;
//
//public class StringMessageEncryptionTDD {
//	
//	@Test
//	void encyptStringWithKey88() {
//		String toEncrypt = "this is a test string";
//		int key = 88;
//		
//		String encoded = "";
//		
//		for(int i=0; i<toEncrypt.length(); i++) {
//			encoded += (char) (toEncrypt.charAt(i) ^ key);
//		}
//		assertEquals(",01+x1+x9x,=+,x+,*16?", encoded);
//	}
//	
//	@Test
//	void decryptStringWithKey88() {
//		String encrypted = ",01+x1+x9x,=+,x+,*16?";
//		int key = 88;
//		String decrypted = "";
//		for(int i=0; i<encrypted.length(); i++) {
//			decrypted += (char) (encrypted.charAt(i) ^ key);
//		}
//		assertEquals("this is a test string", decrypted);
//	}
//
//}

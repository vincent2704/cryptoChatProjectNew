package cryptography;

public class StringMessageEncryption {
	
	public static String encode(String message, int key) {
		String encoded = "";

		for (int i = 0; i < message.length(); i++) {
			encoded += (char) (message.charAt(i) ^ key);
		}
		 
		return encoded;
	 }

}

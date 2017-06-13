package jWARE;

import javax.crypto.Cipher;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import java.io.IOException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
 
public class FileCrypt {
	private String passKey = "";
	private File inputFile;
	private File outputFile;

	/* Initiates the FileCrypter by setting default variables */
	FileCrypt(String key, File fileInput, File fileOutput) {
		passKey = key;
		inputFile = fileInput;
		outputFile = fileOutput;
	}

	/* Encrypts/decrypts files based on cryptType, uses AES */
	private void crypter(int cryptType, String passKey, File inputFile, File outputFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream byteStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

       	Cipher aesCIPHER = Cipher.getInstance("AES");
        Key cryptKey = new SecretKeySpec(passKey.getBytes(), "AES");
           	
        aesCIPHER.init(cryptType, cryptKey);

        byte[] inBytes = new byte[(int)inputFile.length()];
        byteStream.read(inBytes);
		byte[] outBytes = aesCIPHER.doFinal(inBytes);
		
        outputStream.write(outBytes);

        outputStream.close();
        
        byteStream.close();

    }

	/* Encrypts file */
	public void encryptFile() {
		try {
			crypter(Cipher.ENCRYPT_MODE, passKey, inputFile, outputFile);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Decrypts File */
    public void decryptFile() {
        try {
			crypter(Cipher.DECRYPT_MODE, passKey, inputFile, outputFile);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
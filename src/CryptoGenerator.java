import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;

public class CryptoGenerator {

    private final static String ENCODING = "UTF-8";
    private final static String AESAlgorithm = "AES";
    private final static String RSAAlgorithm = "RSA";
    private final static String INSTANCE = "AES/CFB8/NoPadding";
    private static byte[] ivBytes = new byte[]{0x15, 0x14, 0x13, 0x12, 0x11, 0x10, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};


    public static void encryptFile(final File input, final File output, final SecretKeySpec aesKeySpec) {
        try {
            final Cipher aesCipher = Cipher.getInstance(INSTANCE);
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, new IvParameterSpec(ivBytes));

            FileInputStream fileInputStream = new FileInputStream(input);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(new FileOutputStream(output), aesCipher);

            copy(fileInputStream, cipherOutputStream);

            fileInputStream.close();
            cipherOutputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
            e.printStackTrace();
        }
        boolean confirmation = input.delete();
        System.out.println("Deleted: " + confirmation);
    }


    public static String encrypt(PublicKey publicKey, SecretKeySpec aesKeySpec) throws UnsupportedEncodingException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
            IllegalBlockSizeException {

        String encryptedS = "";

        final String encodedKey = Base64.encodeBase64String(aesKeySpec.getEncoded());
        final byte[] plainBytes = encodedKey.getBytes(ENCODING);
        final Cipher cipher = Cipher.getInstance(RSAAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        final byte[] encryptedBytes = cipher.doFinal(plainBytes);
        encryptedS = new String(Base64.encodeBase64(encryptedBytes));

        return encryptedS;
    }


    public static SecretKeySpec decrypt(PrivateKey privateKey, String text) throws UnsupportedEncodingException,
            InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
            IllegalBlockSizeException {

        SecretKeySpec aesKeySpec;
        final byte[] plainBytes = Base64.decodeBase64(text.getBytes(ENCODING));
        final Cipher cipher = Cipher.getInstance(RSAAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        final byte[] decryptedBytes = cipher.doFinal(plainBytes);
        final String decryptedS = new String(decryptedBytes, ENCODING);

        byte[] decodedKey = Base64.decodeBase64(decryptedS);
        aesKeySpec = new SecretKeySpec(decodedKey, 0, decodedKey.length, AESAlgorithm);
        return aesKeySpec;
    }

    public static void decryptFile(final File input, final File output, final SecretKeySpec aesKeySpec) {

        try {
            final Cipher aesCipher = Cipher.getInstance(INSTANCE);
            aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec, new IvParameterSpec(ivBytes));

            CipherInputStream inputStream = new CipherInputStream(new FileInputStream(input), aesCipher);
            FileOutputStream outputStream = new FileOutputStream(output);
            copy(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException e) {
            e.printStackTrace();
        }

        boolean confirmation = input.delete();
        System.out.println("Deleted: " + confirmation);
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int i;
        final byte[] bytes = new byte[8192];
        while ((i = input.read(bytes)) != -1) {
            output.write(bytes, 0, i);
            output.flush();
        }
        output.close();
        input.close();
    }
}

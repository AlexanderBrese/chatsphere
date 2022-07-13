package de.chatsphere.feature.auth;

import de.chatsphere.util.Util;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TokenService {

  private static final Logger log = LoggerFactory.getLogger(TokenService.class);
  private PrivateKey privateKey;
  private PublicKey publicKey;

  /**
   * Initializes a new token services by loading private/public key files from resources.
   */
  TokenService() {
    loadKeys();
  }

  /**
   * Creates a secured JWT with an id.
   *
   * @param username the username
   *
   * @return the secured JWT
   */
  public String createToken(String username) {
    return Jwts.builder()
      .setId(username)
      .signWith(privateKey)
      .compressWith(CompressionCodecs.GZIP)
      .compact();
  }

  /**
   * Reads the content of a secured JWT.
   *
   * @param jws the secured JWT
   *
   * @return the content
   * @throws JwtException the provided jws could not be read
   */
  public String readToken(String jws) throws JwtException {
    return Jwts
      .parser()
      .setSigningKey(publicKey)
      .parseClaimsJws(jws)
      .getBody()
      .getId();
  }

  /**
   * Creates a public key from with a file name located in the resource folder and a encryption
   * algorithm.
   *
   * @param fileName  the file name
   * @param algorithm the encryption algorithm
   *
   * @throws IOException              file could not be read
   * @throws NoSuchAlgorithmException algorithm is wrong
   * @throws InvalidKeySpecException  key spec could not be created
   */
  private void createPublicKey(String fileName, String algorithm)
    throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = getKeyBytes(fileName);

    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    publicKey = keyFactory.generatePublic(spec);
  }

  /**
   * Creates a private key from with a file name located in the resource folder and a encryption
   * algorithm.
   *
   * @param fileName  the file name
   * @param algorithm the encryption algorithm
   *
   * @throws IOException              file could not be read
   * @throws NoSuchAlgorithmException algorithm is wrong
   * @throws InvalidKeySpecException  key spec could not be created
   */
  private void createPrivateKey(String fileName, String algorithm)
    throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = getKeyBytes(fileName);

    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    privateKey = keyFactory.generatePrivate(spec);
  }

  /**
   * Gets the bytes of a key file.
   *
   * @param fileName the file name
   *
   * @return the bytes
   * @throws IOException file could not be read
   */
  private byte[] getKeyBytes(String fileName) throws IOException {
    File publicFile = Util.getFileFromName(fileName);
    FileInputStream fis1 = new FileInputStream(publicFile);
    DataInputStream dis1 = new DataInputStream(fis1);

    byte[] keyBytes = new byte[(int) publicFile.length()];
    dis1.readFully(keyBytes);
    dis1.close();

    return keyBytes;
  }

  /**
   * Creates both private and public key.
   */
  private void loadKeys() {
    try {
      createPublicKey("public_key.der", "RSA");
      createPrivateKey("private_key.der", "RSA");
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

}

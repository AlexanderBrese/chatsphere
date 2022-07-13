package de.chatsphere.io.database.schema.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

/**
 * A Receiver is a User that registered to receive push notifications.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "UserReceiver")
public class Receiver implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * The User.
   *
   * @throws SQLException When inserting a duplicate {#user}x{#key} combination, the corresponding
   * DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "user", foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User user;

  /**
   * The browser provider's push server endpoint
   *
   * <p>2^16-1 / 4 ~ 15.000 Unicode Characters.
   */
  @DatabaseField(columnName = "endpoint")
  private String endpoint;

  /**
   * The Base64 encoded public key of the browser's push subscription used to encrypt messages.
   *
   * <p>128 Unicode Characters.
   *
   * @throws SQLException When inserting a duplicate {#user}x{#key} combination, the corresponding
   * DAO will throw an exception.
   */
  @DatabaseField(columnName = "key")
  private String key;

  /**
   * A Base64 encoded authentication secret that used in authentication of messages.
   *
   * <p>64 Unicode Characters.
   */
  @DatabaseField(columnName = "auth")
  private String auth;

  /**
   * Time the entry was updated (automatically maintained by the database).
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date updatedAt;

  /**
   * Returns the base64 encoded auth string as a byte[].
   */
  public byte[] getAuthAsBytes() {
    return Base64.getDecoder().decode(getAuth());
  }

  /**
   * Returns the base64 encoded public key string as a byte[].
   */
  public byte[] getKeyAsBytes() {
    return Base64.getDecoder().decode(getKey());
  }

  /**
   * Returns the base64 encoded public key as a PublicKey object.
   */
  public PublicKey getUserPublicKey()
    throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
    // Add BouncyCastle as an algorithm provider
    if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
      Security.addProvider(new BouncyCastleProvider());
    }

    KeyFactory kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
    ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
    ECPoint point = ecSpec.getCurve().decodePoint(getKeyAsBytes());
    ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);

    return kf.generatePublic(pubSpec);
  }

}

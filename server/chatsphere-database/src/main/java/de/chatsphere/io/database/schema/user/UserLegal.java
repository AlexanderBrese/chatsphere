package de.chatsphere.io.database.schema.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.chatsphere.io.database.Storeable;
import java.sql.SQLException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Stores legal properties for a user account.
 */
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "UserLegal")
public class UserLegal implements Storeable {

  /**
   * Primary Key of the table.
   */
  @DatabaseField(generatedId = true)
  private Integer id;

  /**
   * User to whom the properties refer.
   * 
   * @throws SQLException When inserting a duplicate, the corresponding DAO will throw an exception.
   */
  @NonNull
  @DatabaseField(columnName = "user", foreign = true, unique = true, foreignAutoCreate = true, foreignAutoRefresh = true)
  private User user;

  /**
   * Time of the AGB consent.
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date agb;

  /**
   * Time of the privacy consent.
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date privacy;

  /**
   * Time at which the account was locked.
   *
   * <p>A block prevents the automatic deletion of an account.
   */
  @DatabaseField(dataType = DataType.DATE)
  private Date locked;
}

package de.chatsphere.io.database;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public abstract class DatabaseTest {

  private static final DatabaseConfig TEST_DATABASE_CONFIG = new DatabaseConfig(
      "jdbc:mariadb://localhost:3306/chatsphere_test",
      "chatsphere_test",
      "chatsphere",
      "QJUE2TiaxoovgqCV"
  );

  protected static final String testString = "٩(-̮̮̃-̃)۶ ٩(●̮̮̃•̃)۶ ٩(͡๏̯͡๏)۶ ٩(-̮̮̃•̃)";
  protected static final byte[] validSha256 = new byte[]{112, -115, 111, 13, 120, -112, -60, -99, 67, 69, -48, 115, 40,
    95, 78, 24, -22, -114, -52, 124, 95, -53, -60, 77, 60, 62, 50, -99, -67, -36, 23, -27}; // of "some random text"

  protected static Database db;

  @BeforeClass
  public static void init() throws SQLException {
    db = new Database(TEST_DATABASE_CONFIG);
  }

  /**
   * Closes the database connection
   */
  @AfterClass
  public static void destroy() throws IOException {
    db.closeConnection();
  }
}

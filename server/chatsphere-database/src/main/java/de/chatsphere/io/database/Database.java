package de.chatsphere.io.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {

  private final ConnectionSource connectionSource;
  private static final Logger log = LoggerFactory.getLogger(Database.class);

  /**
   * Initializes a new Database instance by connecting to a JDBC Source with given credentials and setting up data
   * access objects.
   *
   * @param config the database configuration object
   * @throws SQLException connection failed
   */
  public Database(DatabaseConfig config) throws SQLException {

    JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(
        config.getUrl(),
        config.getUsername(),
        config.getPassword()
    );
    connectionSource.setTestBeforeGet(true);
    this.connectionSource = connectionSource;
  }

  /**
   * Returns the Data Access Object (DAO) for a specific class.
   *
   * @param <D> The specific generated DAO
   * @param <T> The class type that represents the data
   * @param clazz A Class implementing the {@link Storeable} interface
   * @return associated DataAccessObject
   * @throws SQLException on database access failures
   * @see DaoManager#createDao
   */
  public <D extends Dao<T, ?>, T extends Storeable> D getDao(Class<T> clazz) {
    D dao = null;
    try {
      dao = DaoManager.createDao(this.connectionSource, clazz);
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
    return dao;
  }

  /**
   * Terminates the database connection.
   *
   * @throws IOException an error occurred during termination
   */
  public void closeConnection() throws IOException {
    connectionSource.close();
  }
}

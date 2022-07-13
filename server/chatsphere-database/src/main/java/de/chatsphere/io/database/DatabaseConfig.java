package de.chatsphere.io.database;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DatabaseConfig {
  @Getter
  private String url = "jdbc:mariadb://localhost:3306/${database.database}";
  @Getter
  private String database = "chatsphere";
  @Getter
  private String username = "root";
  @Getter
  private String password;
}

import java.io.FileReader;
import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class eg1 {
  public static void main(String[] args) {
    try {
      // Read the configuration from the conf.json file
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(new FileReader("conf.json"));
      JSONObject config = (JSONObject) obj;

      // Get the JDBC driver, connection URL, username, and password from the config object
      String jdbcDriver = (String) config.get("jdbc-driver");
      String connectionUrl = (String) config.get("connection-url");
      String username = (String) config.get("username");
      String password = (String) config.get("password");

      // Load the JDBC driver
      Class.forName(jdbcDriver);

      // Get the connection to the database
      Connection conn = DriverManager.getConnection(connectionUrl, username, password);

      // Create a statement object
      Statement stmt = conn.createStatement();

      // Execute a SELECT query against the information schema
      String query = "SELECT C.TABLE_NAME,C.COLUMN_NAME,C.DATA_TYPE,C.CHARACTER_MAXIMUM_LENGTH,C.IS_NULLABLE,C.COLUMN_DEFAULT,C.COLUMN_TYPE,C.COLUMN_KEY,C.EXTRA,K.CONSTRAINT_NAME FROM INFORMATION_SCHEMA.COLUMNS C LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE K ON C.TABLE_SCHEMA = K.TABLE_SCHEMA AND C.TABLE_NAME = K.TABLE_NAME AND C.COLUMN_NAME = K.COLUMN_NAME WHERE C.TABLE_SCHEMA = 'tmdb'";
      ResultSet rs = stmt.executeQuery(query);

      // Iterate through the ResultSet and extract the required information
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        String columnName = rs.getString("COLUMN_NAME");
        String dataType = rs.getString("Data_TYPE");
        String characterMaximumLength = rs.getString("CHARACTER_MAXIMUM_LENGTH");
        String isNullable = rs.getString("IS_NULLABLE");
        String columnType = rs.getString("COLUMN_TYPE");
        String columnDefault = rs.getString("COLUMN_DEFAULT");
        String columnKey = rs.getString("COLUMN_KEY");
        String extra = rs.getString("EXTRA");
        String constraintName = rs.getString("CONSTRAINT_NAME");

        System.out.println("Table Name: " + tableName);
        System.out.println("Column Name: " + columnName);
        System.out.println("Column Type: " + columnType);
        System.out.println("Data Type: " + dataType);
        System.out.println("Character Maximum length: " + characterMaximumLength);
        System.out.println("IS Nullable :" + isNullable);
        System.out.println("Column Default :" + columnDefault);
        System.out.println("Column Key :" + columnKey);
        System.out.println("Extra :" + extra);
        System.out.println("Contraint Name :" + constraintName);
      }

      // Close the ResultSet, statement, and connection objects
      rs.close();
      stmt.close();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

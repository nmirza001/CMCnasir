package cmc.backend;

import java.sql.*;

/**
 * This is like an alternate UniversityDB class with the custom tables.
 * @author Roman Lefler
 * @version Apr 2, 2025
 */
public class DBExtension implements AutoCloseable {

	// URL found in string in lib/UniversityDBLib.class
	private static final String URL_F = "jdbc:mysql://cscimysqlsrv.csbsju.edu/%s";
	private String username;
	private String password;
	
	private Connection conn;
	
	/**
	 * Creates the info to connect to the database.
	 * Must call {@link #connect()} to actually connect to database.
	 * @param username Username for db
	 * @param password Password for db
	 */
	public DBExtension(String username, String password) {
		this.username = username;
		this.password = password;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			throw new IllegalStateException("Failed to load MySQL driver.");
		}
	}
	
	/**
	 * Connects to the database.
	 * Must be called before interacted with.
	 */
	public void connect() {
		try {
			String url = String.format(URL_F, username);
			conn = DriverManager.getConnection(url, username, password);
		}
		catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Closes the database connection.
	 */
	public void close() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public String getWebpageUrl(String uniName) {
		try {
			return getWebpageUrlInternal(uniName);
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String getWebpageUrlInternal(String uniName) throws SQLException {
		String sql = "SELECT WebpageUrl FROM UnivExt WHERE School = \"?\"";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, uniName);
		ResultSet resultSet = statement.executeQuery(sql);
		resultSet.next();
		String url = resultSet.getString("WebpageUrl");
		return url;
	}
	
}

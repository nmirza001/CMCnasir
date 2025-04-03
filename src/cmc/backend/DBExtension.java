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
	private Statement statement;
	
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
	
	public void connect() {
		try {
			String url = String.format(URL_F, username);
			conn = DriverManager.getConnection(url, username, password);
			
			statement = conn.createStatement();
			
			test();
		}
		catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void close() {
		try {
			conn.close();
		}
		catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void test() {
		try {
			testInternal();
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void testInternal() throws SQLException {
		String sql = "SELECT * FROM University WHERE State = \"Texas\"";
		ResultSet resultSet = statement.executeQuery(sql);
		resultSet.next();
		String name = resultSet.getString("School");
	}
	
}

package cmc.backend;

import java.sql.*;

/**
 * This is like an alternate UniversityDB class with the custom tables.
 * @author Roman Lefler
 * @version Apr 2, 2025
 */
public class DBExtension implements AutoCloseable {

	private static final String NOT_CONN_MSG = "Call connect() before interacting with the database.";
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
	
	/**
	 * Gets the webpage URL for a specified university.
	 * @param uniName Name of the university.
	 * @return The webpage URL or {@code null} if it doesn't exist.
	 */
	public String getWebpageUrl(String uniName) {
		try {
			return getWebpageUrlInternal(uniName);
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String getWebpageUrlInternal(String uniName) throws SQLException {
		if(uniName == null) throw new IllegalArgumentException();
		if(conn == null) throw new IllegalStateException(NOT_CONN_MSG);
		
		String sql = "SELECT WebpageUrl FROM UnivExt WHERE School = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, uniName);
		ResultSet resultSet = statement.executeQuery();
		
		// If nothing found
		if(!resultSet.next()) return null;
		
		String url = resultSet.getString("WebpageUrl");
		return url;
	}
	
	/**
	 * Sets the webpage URL for a specified university.
	 * This will either insert or update accordingly.
	 * @param uniName Name of the university.
	 * @param url URL of webpage.
	 */
	public void setWebpageUrl(String uniName, String url) {
		try {
			setWebpageUrlInternal(uniName, url);
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void setWebpageUrlInternal(String uniName, String url) throws SQLException {
		if(uniName == null) throw new IllegalArgumentException();
		if(conn == null) throw new IllegalStateException(NOT_CONN_MSG);
		
		// First try update
		String sql = "UPDATE UnivExt SET WebpageUrl = ? WHERE School = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, url);
		statement.setString(2, uniName);
		int affected = statement.executeUpdate();
		// If it works or url is blank return
		if(affected > 0 || url == null) return;
		
		// If it fails then insert
		sql = "INSERT INTO UnivExt (School, WebpageUrl) VALUES (?, ?)";
		statement = conn.prepareStatement(sql);
		statement.setString(1, uniName);
		statement.setString(2, url);
		statement.executeUpdate();
	}

	/**
	 * Gets the image URL for a specified university.
	 * @param uniName Name of the university.
	 * @return The image URL or {@code null} if it doesn't exist.
	 */
	public String getImageUrl(String uniName) {
		try {
			return getImageUrlInternal(uniName);
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private String getImageUrlInternal(String uniName) throws SQLException {
		if(uniName == null) throw new IllegalArgumentException();
		if(conn == null) throw new IllegalStateException(NOT_CONN_MSG);
		
		String sql = "SELECT ImageUrl FROM UnivExt WHERE School = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, uniName);
		ResultSet resultSet = statement.executeQuery();
		
		// If nothing found
		if(!resultSet.next()) return null;
		
		String url = resultSet.getString("ImageUrl");
		return url;
	}
	
	/**
	 * Sets the image URL for a specified university.
	 * This will either insert or update accordingly.
	 * @param uniName Name of the university.
	 * @param url URL of image.
	 */
	public void setImageUrl(String uniName, String url) {
		try {
			setImageUrlInternal(uniName, url);
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void setImageUrlInternal(String uniName, String url) throws SQLException {
		if(uniName == null) throw new IllegalArgumentException();
		if(conn == null) throw new IllegalStateException(NOT_CONN_MSG);
		
		// First try update
		String sql = "UPDATE UnivExt SET ImageUrl = ? WHERE School = ?";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setString(1, url);
		statement.setString(2, uniName);
		int affected = statement.executeUpdate();
		// If it works or url is blank return
		if(affected > 0) return;
		
		// If it fails then insert
		sql = "INSERT INTO UnivExt (School, ImageUrl) VALUES (?, ?)";
		statement = conn.prepareStatement(sql);
		statement.setString(1, uniName);
		statement.setString(2, url);
		statement.executeUpdate();
	}
	
	/**
	 * Removes a university row and all its data.
	 * @param uniName University name.
	 * @return {@code true} if university was successfully deleted, that is,
	 *         if a row in this DB existed.
	 */
	public boolean removeUniversityRow(String uniName) {
		try {
			if(uniName == null) throw new IllegalArgumentException();
			if(conn == null) throw new IllegalStateException(NOT_CONN_MSG);
			
			String sql = "DELETE FROM UnivExt WHERE School = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, uniName);
			return statement.executeUpdate() == 1;
		} catch(SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
}

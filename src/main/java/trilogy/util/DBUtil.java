package trilogy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
	public static boolean insertIntoDB(String sql) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		boolean ret = false;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager
					.getConnection(
							"jdbc:postgresql://ec2-107-21-219-142.compute-1.amazonaws.com:5432/d54kp92sj98g83?currentSchema=megahackathon",
							"pbbnglrgsfpbso", "3VGLI4DZIDZGYOp4uwaU9oBbg1");
			preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				ret = true;
			}
			preparedStatement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String fetchFromDB(String sql) {
		String response = "";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager
					.getConnection(
							"jdbc:postgresql://ec2-107-21-219-142.compute-1.amazonaws.com:5432/d54kp92sj98g83?currentSchema=megahackathon",
							"pbbnglrgsfpbso", "3VGLI4DZIDZGYOp4uwaU9oBbg1");
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				try {
					response = resultSet.getString(1) + ","
							+ resultSet.getString(2) + ","
							+ resultSet.getString(3) + ","
							+ resultSet.getString(4) + ","
							+ resultSet.getString(5);
				} catch (Exception e) {
					response = "";
					e.printStackTrace();
					break;
				}
			}
			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (Exception e) {
			response = "";
			e.printStackTrace();
		}
		return response;
	}
}

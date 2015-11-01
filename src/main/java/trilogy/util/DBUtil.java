package trilogy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	public static boolean insertIntoDB(String sql) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-107-21-219-142.compute-1.amazonaws.com:5432/megahackathon","pbbnglrgsfpbso","3VGLI4DZIDZGYOp4uwaU9oBbg1");
			preparedStatement = connection.prepareStatement(sql);
			int result = preparedStatement.executeUpdate();

			if (result == 1) {
				// Add a log statment taht the record is inserted succesfully
				// System.out.println("Result >>>>>>>>>>>>>>> "+result);
				return true;
			} else {
				// Add a log statment taht the record is not inserted
				// succesfully
				// System.out.println("Result >>>>>>>>>>>>>>> "+result);
				return false;
			}

		} catch (SQLException e) {
			// System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static String fetchFromDB(String sql) {
		String response = "";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
					"jdbc:postgresql://ec2-107-21-219-142.compute-1.amazonaws.com:5432/megahackathon","pbbnglrgsfpbso","3VGLI4DZIDZGYOp4uwaU9oBbg1");
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String lastColumn = "";
				try {
					lastColumn = resultSet.getString(5);
					response = resultSet.getString(1) + ","
							+ resultSet.getString(2) + ","
							+ resultSet.getString(3) + ","
							+ resultSet.getString(4) + ","
							+ resultSet.getString(5);
				} catch (SQLException sqlex) {
					// sqlex.printStackTrace();
					response = resultSet.getString(1) + ","
							+ resultSet.getString(2) + ","
							+ resultSet.getString(3) + ","
							+ resultSet.getString(4);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}
}

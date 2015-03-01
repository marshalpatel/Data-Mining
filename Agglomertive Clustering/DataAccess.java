package com.marshalp.aggclustering;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*DataAccess class for querying database*/

public class DataAccess {

	private Connection connect = null;
	// private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	// Map<Users,Pairs> distMap = new HashMap<Users, Pairs>();
	ArrayList<DataPoints> uList = new ArrayList<DataPoints>();

	public ArrayList<DataPoints> getUserData() throws Exception {

		int id, x, y;

		try {

			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/localDB?"
							+ "user=root&password=Mrp@IU");
			if(ACMain.isUser()){
			preparedStatement = connect
					.prepareStatement("select UserID, Age, Occupation from USERS");
			}
			else{
			 preparedStatement =
			 connect.prepareStatement("select m.MovieID,g.Id, r.Rating from MOVIES m, RATINGS r,GENRE g  where m.MovieID = r.MovieID and m.Genre like concat('%',g.Genre,'%') group by m.MovieID,g.Genre, r.Rating");
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				
				if(ACMain.isUser()){
				id = Integer.parseInt(resultSet.getString("UserID"));
				y = Integer.parseInt(resultSet.getString("Age"));
				x = Integer
						.parseInt(resultSet.getString("Occupation"));
				uList.add(new DataPoints(id, y, x));
				}
				else{
					id = Integer.parseInt(resultSet.getString("MovieID"));
					 y = Integer.parseInt(resultSet.getString("Id"));
					 x = Integer.parseInt(resultSet.getString("Rating"));
					 uList.add(new DataPoints(id, y, x));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resultSet.close();
			connect.close();
			preparedStatement.close();

		}
		return uList;
	}

}

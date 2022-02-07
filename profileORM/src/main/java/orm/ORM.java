package orm;

import com.revature.models.Profile;
import com.revature.util.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static orm.ORMConnection.getConnection;

public class ORM {

    public static <DbData> List<DbData> getAllData(String dataBase) {
        Connection conn = getConnection();
        String sql = "SELECT * FROM " + dataBase;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            //Extract all movies out of the ResultSet
            List<DbData> data = new ArrayList<>();
            while (rs.next()) {
                //Add each movie to our list of movies.
                data.add((DbData) buildObject(rs));
            }
            return data;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Profile getDataById(int id){

        //***** Try and make this a class variable
        // instead of method var so the whole class can use it **************
        Connection conn = getConnection();

        //Make a String for the SQL statement you want executed. Use Placeholders for data values.
        String sql = "SELECT * FROM profiles WHERE p_id = ?";

        try {
            //Set up PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);
            //Set values for any Placeholders
            ps.setInt(1, id);

            //Execute the query, store the results -> ResultSet
            ResultSet rs = ps.executeQuery();

            //Extract results our of ResultSet
            if(rs.next()) {
                return buildObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Profile deleteData(int id)throws ResourceNotFoundException {

        Connection conn = getConnection();

        String sql = "DELETE FROM profiles WHERE p_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildObject(rs);
            } else {
                throw new ResourceNotFoundException("Resource with id: " + id + " was not found in database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Profile addData(Profile p){

        String sql = "INSERT INTO profiles VALUES (default,?,?,?,?) RETURNING *";
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            //set values for all the placeholders: ?
            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3,p.getMiddleName());
            ps.setString(4, p.getPhoneNumber());


            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Profile updateData(Profile change){

        Connection conn = getConnection();
        String sql = "UPDATE profiles set f_name=?, l_name=?, m_name=?, p_num=? WHERE p_id = ? RETURNING *";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, change.getFirstName());
            ps.setString(2, change.getLastName());
            ps.setString(3, change.getMiddleName());
            ps.setString(4, change.getPhoneNumber());
            ps.setInt(5, change.getId());


            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildObject(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Helper Method
    public static Profile buildObject(ResultSet rs) throws SQLException {
        Profile p = new Profile();
        p.setId(rs.getInt("p_id"));
        p.setFirstName(rs.getString("f_name"));
        p.setLastName(rs.getString("l_name"));
        p.setMiddleName(rs.getString("m_name"));
        p.setPhoneNumber(rs.getString("p_num"));
        return p;
    }

}
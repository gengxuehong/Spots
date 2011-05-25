package edoor.Data.JDBC;

import edoor.Data.*;
import java.sql.*;


/**
 * Data source for accessing data stored in JDBC database
 * @author Geng Xuehong
 *
 */
public class JDBCDataSource 
	implements IDataSource  
{
    /**
     * Open a connection to data source
     * @param dbName JDBC data source URL
     * @param user User name used to connect to database
     * @param password Password of the user
     * @return data connection opened
     * @throws exception indicates problem during connecting
     */
    @Override
    public IDataConnection connect(String dbName, String user, String password) throws DataException {
        // Open a JDBC connection
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbName, user, password);
        } catch (SQLException ex) {
            DataException e = new DataException("Open DB connection failed!");
            e.initCause(ex);
            throw e;
        }
        
        // Wrap it with JDBCConnection
        JDBCConnection jConn = new JDBCConnection(conn);
        return jConn;
    }
}

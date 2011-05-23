/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data.JDBC;

import java.sql.*;
import edoor.Data.*;
import java.util.*;
import java.util.logging.*;

/**
 * Class that wrap JDBC connection for easy using
 * @author Geng Xuehong
 */
public class JDBCConnection 
    implements IDataConnection, IDataSourceSchema {
    private Connection _connection = null;
    
    /**
     * Construct with a Connection
     * @param conn JDBC connection
     * @throws DataException 
     */
    public JDBCConnection(Connection conn) throws DataException {
        if(conn == null)
            throw new DataException("Null Connection!");
        _connection = conn;
    }
    
    /**
     * Check if the JDBC connection is connected
     * @return true if connected.
     */
    @Override
    public boolean isConnected() {
        if(_connection == null)
            return false;
        try {
            return !_connection.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Re-open connection after it was closed. For JDBC connection, this method is not supported.
     * @throws DataException 
     */
    @Override
    public void reopen() throws DataException {
        throw new DataException("Unsupport reopen within JDBC connection!");
    }

    /**
     * Close the connection
     * @throws DataException 
     */
    @Override
    public void close() throws DataException {
        if(_connection == null)
            return;
        try {
            _connection.close();
            _connection = null;
        } catch (SQLException ex) {
            DataException e = new DataException("Close connection failed!");
            e.initCause(ex);
            throw e;
        }
    }

    /**
     * Commit transaction
     * @throws DataException 
     */
    @Override
    public void commit() throws DataException {
        if(_connection == null)
            throw new DataException("No valid connection!");
        try {
            _connection.commit();
        } catch (SQLException ex) {
            DataException e = new DataException("Commit failed!");
            e.initCause(ex);
            throw e;
        }
    }

    /**
     * Rollback transaction
     * @throws DataException 
     */
    @Override
    public void rollback() throws DataException {
        if(_connection == null) 
            throw new DataException("No valid connection!");
        try {
            _connection.rollback();
        } catch (SQLException ex) {
            DataException e = new DataException("Rollback failed!");
            e.initCause(ex);
            throw e;
        }
    }

    /**
     * Run a sql to update data source
     * @param sql SQL statement
     * @param args arguments used in this SQL
     * @return Affected row count
     * @throws DataException 
     */
    @Override
    public int execute(String sql, Object... args) throws DataException {
        if(_connection == null) {
            throw new DataException("Connection closed!");
        }
        
        try {
            Statement cmd = _connection.createStatement();
            int iRet = cmd.executeUpdate(sql);
            return iRet;
        } catch (SQLException ex) {
            String msg = String.format("Run SQL failed! \n%s", sql);
            DataException err = new DataException(msg);
            err.initCause(ex);
            throw err;
        }
    }

    /**
     * Run a sql to query data from data source
     * @param sql SQL statement
     * @param args arguments used in this SQL
     * @return Queried datatable
     * @throws DataException 
     */
    @Override
    public DataTable query(String sql, Object... args) throws DataException {
        if(_connection == null) {
            throw new DataException("Connection closed!");
        }
        
        try {
            Statement cmd = _connection.createStatement();
            ResultSet result = cmd.executeQuery(sql);
            return DataTable.fromResultSet(result);
        } catch (SQLException ex) {
            String msg = String.format("Run SQL failed! \n%s", sql);
            DataException err = new DataException(msg);
            err.initCause(ex);
            throw err;
        }
    }

    @Override
    public IDataAdapter createTableAdapter(String tableName) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isTableExists(String tableName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getAllTables() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createTable(DataTable tableSchema) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataTable openTable(String tableName) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateTable(DataTable tableSchema) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteTable(String tableName) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

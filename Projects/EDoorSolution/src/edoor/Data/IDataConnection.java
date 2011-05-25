package edoor.Data;

/**
 * Interface for connection to data source
 * @author Geng Xuehong
 *
 */
public interface IDataConnection {
    /**
     * Check if data connection is connected
     * @return true if it is connected
     */    
    boolean isConnected();
        
    /**
     * Re-open the connection
     */
    void reopen() throws DataException;

    /**
     * Close connection.
     * Closed connection could be reopened again by calling reopen().
     */
    void close() throws DataException;

    /**
     * Commit changes made in data source
     */
    void commit() throws DataException;

    /**
     * Roll back changes made in data source
     */
    void rollback() throws DataException;

    /**
     * Execute SQL command on data source to update data
     * @param sql SQL statement
     * @param args arguments for SQL command
     * @return effected row count
     */
    int execute(String sql, Object... args) throws DataException;

    /**
     * Execute SQL command on data source to query data
     * @param sql SQL statement
     * @param args arguments for SQL query
     * @return DataTable contains queried data.
     */
    DataTable query(String sql, Object... args) throws DataException;

    /**
     * Get schema for accessing data structure of source DB
     * @return IDataSourceSchema interface
     * @throws DataException 
     */
    IDataSourceSchema getSchema() throws DataException;
    
    /**
     * Create a adapter for specific data table
     * @param tableName name of table
     * @return Adapter for data table
     */
    IDataAdapter createTableAdapter(String tableName) throws DataException;
}

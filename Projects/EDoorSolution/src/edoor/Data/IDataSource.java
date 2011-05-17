package edoor.Data;

/**
 * Interface for access data source
 * @author Geng Xuehong
 *
 */
public interface IDataSource {
    /**
     * Open a connection to data source
     * @param dbName a string with database name
     * @return data connection opened
     * @throws exception indicates problem during connecting
     */
    IDataConnection connect(String dbName, String user, String password) throws DataException;

    /**
     * Create a new database in data source
     * @param dbName name of new database
     * @return data schema used to create data structure
     */
    IDataSourceSchema createSchema(String dbName) throws DataException;

    /**
     * Open data schema for editing data structure of existing database
     * @param dbName name of existing database
     * @return data schema used to edit data structure
     */
    IDataSourceSchema openSchema(String dbName) throws DataException;

    /**
     * Delete a database from data source
     * @param dbName name of database to be deleted
     */
    void deleteSchema(String dbName) throws DataException;
}

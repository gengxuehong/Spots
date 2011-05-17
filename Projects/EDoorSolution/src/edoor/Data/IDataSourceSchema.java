package edoor.Data;

/**
 * Interface for accessing data source schema.
 * Using this interface, data structure could be queried and managed by caller.
 * @author Geng Xuehong
 *
 */
public interface IDataSourceSchema {
    /**
     * Check if a table is exist in schema.
     * @param tableName
     * @return
     */
    boolean isTableExists(String tableName);

    /**
     * Get all names of tables.
     * @return a string array that contains all table names.
     */
    String[] getAllTables();

    /**
     * Create a table in data schema
     * @param tableSchema schema of table to be created.
     * @throws DataException exception when something wrong in data management.
     */
    void createTable(DataTable tableSchema) throws DataException;

    /**
     * Open a table in data schema.
     * @param tableName name of table to be opened.
     * @return DataTable that contains schema only.
     */
    DataTable openTable(String tableName) throws DataException;

    /**
     * Update table schema in data schema
     * @param tableSchema new table schema
     */
    void updateTable(DataTable tableSchema) throws DataException;

    /**
     * Delete a table from data schema
     * @param tableName name of table to be deleted
     * @throws DataException exception when something wrong in data management.
     */
    void deleteTable(String tableName) throws DataException;
}

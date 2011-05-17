package edoor.Data;

/**
 * Interface for update data table to data source
 * @author Geng Xuehong
 *
 */
public interface IDataAdapter {
    /**
     * Load data from data source to data table
     * @param table DataTable to hold the data
     * @return Count of rows loaded
     */
    int Load(DataTable table) throws DataException;

    /**
     * Load data from data source to data table with filter condition
     * @param table DataTable to hold the data
     * @param filter Condition used to filter data
     * @return Count of rows loaded
     */
    int Load(DataTable table, String filter) throws DataException;

    /**
     * Update data from data table to data source
     * @param table DataTable contains data
     */
    void update(DataTable table) throws DataException;

    /**
     * Load data from data source to data set.
     * All depended table will be loaded too.
     * @param dataset DataSet to hold the data
     * @param mainTable Name of main table
     * @return Count of rows loaded into main table
     * @throws DataException
     */
    int Load(DataSet dataset, String mainTable) throws DataException;

    /**
     * Load data from data source to data set.
     * All depended table will be loaded too.
     * @param dataset DataSet to hold the data
     * @param mainTable Name of main table
     * @param filter Condition of query to filter data
     * @return Count of rows loaded into main table
     * @throws DataException
     */
    int Load(DataSet dataset, String mainTable, String filter) throws DataException;

    /**
     * Update whole data set to data source
     * @param dataset DataSet contains data
     */
    void update(DataSet dataset) throws DataException;
}

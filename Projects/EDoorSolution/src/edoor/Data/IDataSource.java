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
}

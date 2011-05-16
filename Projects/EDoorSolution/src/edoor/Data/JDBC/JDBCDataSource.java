package edoor.Data.JDBC;

import edoor.Data.DataException;
import edoor.Data.IDataConnection;
import edoor.Data.IDataSource;
import edoor.Data.IDataSourceSchema;

/**
 * Data source for accessing data stored in JDBC database
 * @author Geng Xuehong
 *
 */
public class JDBCDataSource 
	implements IDataSource  
{

	@Override
	public IDataConnection connect(String dbName) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataSourceSchema createSchema(String dbName) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDataSourceSchema openSchema(String dbName) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSchema(String dbName) throws DataException {
		// TODO Auto-generated method stub
		
	}

}

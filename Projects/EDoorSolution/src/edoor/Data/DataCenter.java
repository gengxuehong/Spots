/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data;

import edoor.Command.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that manage application data
 * @author Geng Xuehong
 */
@CommandBase( AutoRegister = false )
public class DataCenter 
    extends CmdTarget 
    implements ICommandCenter
{
    private IDataSource _source = null;
    private IDataConnection _connection = null;
    private boolean _sqlMode = false;
    
    /**
     * Construct data center with a data source
     * @param src IDataSource interface
     */
    public DataCenter(IDataSource src) throws DataException {
        if(src == null)
            throw new DataException("Null data source!");
        _source = src;
    }
    
    /**
     * Construct data center with a class name of data source
     * @param classOfDataSource
     * @throws DataException 
     */
    public DataCenter(String classOfDataSource) throws DataException {
        // Create a data source object
        try {
            Class<?> cls = Class.forName(classOfDataSource);
            Object objSrc = cls.newInstance();
            _source = (IDataSource)objSrc;
        } catch(Throwable err) {
            DataException ex = new DataException("Create instance of data source failed!");
            ex.initCause(err);
            throw ex;
        }
    }
    
    /**
     * Load JDBC driver for application
     * @param clsDriver Class name of JDBCDriver
     */
    @CommandEntry( Token = "DBLoadDriver", Syntax = "DBLoadDriver [class name of driver]")
    public void LoadJDBCDriver(String clsDriver) throws ClassNotFoundException {
        Class<?> cls = Class.forName(clsDriver);
        writeConsole("Driver loaded from %s\n", cls.getProtectionDomain().getCodeSource().getLocation().toString());
    }
    
    /**
     * Connect to a specific database
     * @param connection a string that contains connection string 
     * @param  user a string with Username for connecting to database
     * @param password Password of this user
     * @throws DataException 
     */
    @CommandEntry( Token = "DBOpen", Syntax = "DBOpen [connection string] [username] [password]")
    public void Connect(String connection, String user, String password) throws DataException {
        if(_source == null)
            throw new DataException("Data center was not initialized!");
        if(_connection != null)
            throw new DataException("Already connected to another database! Must disconnect before connect to another database!");
        
        _connection = _source.connect(connection, user, password);
        
        writeConsole("Connected.\n");
    }
    
    /**
     * Disconnect from current database
     */
    @CommandEntry( Token = "DBClose", Syntax = "DBClose")
    public void Disconnect() throws DataException {
        if(_connection == null) return; // already disconnected
        _connection.close();
        _connection = null;
        writeConsole("Disconnected.\n");
    }
    
    /**
     * Command to switch to SQL mode
     * @throws DataException 
     */
    @CommandEntry( Token = "DoSQL", Syntax = "DoSQL" )
    public void DoSQL() throws DataException {
        if(_connection == null || !_connection.isConnected()) {
            throw new DataException("Connection to database not opened yet!");
        }
        _sqlMode = true;
        _cmdCenter.installHook(this);
    }
    
    /**
     * Describe table structure of a class
     * @param clsName Name of class that will be described as a table
     */
    @CommandEntry( Token="DBDescClass", Syntax="DBDescClass [class name]")
    public void DescribeClass(String clsName) 
            throws ClassNotFoundException, DataException {
        Class<?> cls = Class.forName(clsName);
        DataTable tb = DataTable.fromClass(cls);
        StringBuilder out = new StringBuilder();
        tb.Describe(out);
        writeConsole("Table structure of class %s is:\n%s", clsName, out.toString());
    }

    /**
     * Generate data base schema for class
     * @param clsName Class name for schema generation
     * @throws ClassNotFoundException
     * @throws DataException 
     */
    @CommandEntry( Token="DBCreateTable", Syntax="DBCreateTable [class name]")
    public void GenerateSchemaForClass(String clsName) 
            throws ClassNotFoundException, DataException {
        Class<?> cls = Class.forName(clsName);
        DataTable tb = DataTable.fromClass(cls);
        IDataSourceSchema schema = _connection.getSchema();
        if(!schema.isTableExists(tb.getName())) {
            // Create new table
            writeConsole("Table not exist.\n");
            schema.createTable(tb);
        } else {
            // Check and update table structure
            writeConsole("Table already exist.\n");
            schema.updateTable(tb);
        }
        writeConsole("Table \"%s\" was generated for class %s\n", tb.getName(), clsName);
    }
    
    @Override
    public String getPrompt() {
        return "SQL>";
    }

    /**
     * Tool to output data table to console
     * @param table Data table
     * @param console Console
     */
    public static void OutputTable(DataTable table, IConsole console) throws DataException {
        // Output Column head
        int iRowLen = 0;
        for(DataColumn col : table.getColumns()) {
            String colName = col.getName();
            console.print("%s\t", colName);
            iRowLen += colName.length() + 4;
        }
        console.print("\n");
        // Output seperator
        for(int i=0;i<iRowLen;i++)
            console.print("-");
        console.print("\n");
        // Output rows
        for(DataRow row : table.getRows()) {
            for(DataColumn col : table.getColumns()) {
                String colName = col.getName();
                Object val = row.getField(colName);
                console.print("%s\t", val.toString());
            }
            console.print("\n");
        }
    }
    
    @Override
    public void doCommand(String cmd) throws CommandException {
        if(cmd.toLowerCase().startsWith("quit")) {
            _sqlMode = false;
            _cmdCenter.uninstallHook();
            return;
        }
        
        // Do SQL command
        if(cmd.toLowerCase().startsWith("select ")) {
            DataTable result = null;
            try {
                // It's a query
                result = _connection.query(cmd);
            } catch (DataException ex) {
                CommandException err = new CommandException("Query failed!");
                err.initCause(ex);
                throw err;
            }
            // Show queried data
            if(_console != null) {
                _console.print("%d lines found:\n", result.getRows().size());
                try {
                    OutputTable(result, _console);
                } catch (DataException ex) {
                    CommandException err = new CommandException("Show queried data failed!");
                    err.initCause(ex);
                    throw err;
                }
            }
        } else {
            int iResult = 0;
            try {
                // It's modifying
                iResult = _connection.execute(cmd);
            } catch (DataException ex) {
                CommandException err = new CommandException("Execution failed!");
                err.initCause(ex);
                throw err;
            }
            writeConsole("%d lines affected.\n", iResult);
        }
    }

    @Override
    public void installHook(ICommandCenter hook) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void uninstallHook() throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

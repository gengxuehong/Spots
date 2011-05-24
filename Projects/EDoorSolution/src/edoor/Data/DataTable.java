package edoor.Data;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

/**
 * class for access data stored in a table
 * @author Geng Xuehong
 *
 */
public class DataTable {

    private String _name = "";
    private LinkedHashMap<String, DataColumn> _columns = new LinkedHashMap<String, DataColumn>();
    private LinkedList<DataRow> _rows = new LinkedList<DataRow>();
    
    private static void AddColumnToTable(DataTable table, DataField annoField) throws DataException {
        String fieldName = annoField.FieldName();
        DataColumn col = null;
        if(table.hasColumn(annoField.FieldName())) {
            // Column already exist, update its properties
            col = table.getColumn(annoField.FieldName());
            if(annoField.Scale() > col.getScale()) col.setScale(annoField.Scale());
            if(col.getType() == annoField.Scale()) throw new DataException("DataField types in annotations are not consistent!");
        } else {
            // Column not exist, create new one
            col = table.NewColumn(fieldName, annoField.Type(), annoField.Scale(), annoField.Precision());
        }
        String oldKey = col.getKey();
        if(annoField.isPrimaryKey()) {
            if(oldKey != null && !oldKey.isEmpty() && 0 != oldKey.compareToIgnoreCase(annoField.Key()))
                throw new DataException("Primary key name in annotations are not consistent!");
            else
                col.setPrimaryKey(annoField.Key());
        }
        else if(annoField.isIndexed()) {
            if(oldKey != null && !oldKey.isEmpty() && 0 != oldKey.compareToIgnoreCase(annoField.Key()))
                throw new DataException("Index key name in annotations are not consistent!");
            else
                col.setIndexed(annoField.Key());            
        }
    }
    
    /**
     * Generate DataTable from a Class
     * @param cls Class used to create data table
     * @return Generated data table
     */
    public static DataTable fromClass(Class<?> cls) throws DataException {
        DataRecord annoRecord = (DataRecord)cls.getAnnotation(DataRecord.class);
        if(annoRecord == null) {
            throw new DataException("Class does not marked as DataRecord!");
        }
        String tableName = annoRecord.TableName();
        
        DataTable table = new DataTable(tableName);
        
        // Lookup fields from class fields
        Field[] fields = cls.getFields();
        for(Field field : fields) {
            DataField annoField = (DataField)field.getAnnotation(DataField.class);
            if(annoField == null) continue;
            AddColumnToTable(table, annoField);
        }
        
        // Lookup fields from methods
        for(Method method : cls.getMethods()) {
            DataField annoField = (DataField)method.getAnnotation(DataField.class);
            if(annoField == null) continue;
            Class<?>[] paramTypes = method.getParameterTypes();
            Class<?> type = null;
            if(method.getReturnType().equals(void.class) && paramTypes.length == 1) {
                // This must be a set method
                type = paramTypes[0];
            } else if(!method.getReturnType().equals(void.class) && paramTypes.length == 0) {
                // This is a get method
                type = method.getReturnType();
            } else {
                String msg = String.format("Method %s could not be treated as DataField!", method.getName());
                throw new DataException(msg);
            }
            AddColumnToTable(table, annoField);
        }
        return table;
    }
    
    /**
     * Generate DataTable from Result set
     * @param result Result set contains data
     * @return Generated data table
     */
    public static DataTable fromResultSet(ResultSet result) throws DataException {
        if(result == null)
            throw new DataException("Result set is null!");
        
        try {
            DataTable table = new DataTable();
            // Initialize structure
            ResultSetMetaData metaData = result.getMetaData();
            int iCols = metaData.getColumnCount();
            for(int i=1; i<=iCols; i++) {
                int colType = metaData.getColumnType(i);
                int colScale = metaData.getScale(i);
                int colPrecision = metaData.getPrecision(i);
                String colName = metaData.getColumnLabel(i);
                DataColumn col = table.NewColumn(colName, colType, colScale, colPrecision);
                col.setNullable(metaData.isNullable(i) == metaData.columnNoNulls ? false : true);
            }
            // Load data
            table.Read(result);
            return table;
        } catch (SQLException ex) {
            DataException err = new DataException("Create DataTable from ResultSet failed!");
            err.initCause(ex);
            throw err;
        }
    }
    /**
     * Describe data structure of this table into a string builder
     * @param out String builder
     */
    public void Describe(StringBuilder out) {
        out.append(getName());
        out.append("\n");
        for(DataColumn col : getColumns()) {
            col.Describe(out);
        }
    }
    
    public DataTable() {
        
    }
    
    public DataTable(String name) {
        _name = name;
    }
    
    public void setName(String name) {
        _name = name;
    }
    public String getName() {
        return _name;
    }
    
    /**
     * Get collection of columns
     * @return a collection that contains all columns
     */
    public Collection<DataColumn> getColumns() {
        return _columns.values();
    }
    
    /**
     * Check if a column is contained in the table
     * @param name Name of column
     * @return true if column exists
     */
    public boolean hasColumn(String name) {
        return _columns.containsKey(name);
    }
    
    /**
     * Create a new column in table
     * @param name Name of new column
     * @param type Data type of new column
     * @param scale Max length of column. Ignored for fix size columns
     * @param precision Precision of column
     * @return Newly created column
     * @throws DataException 
     */
    public DataColumn NewColumn(String name, int type, int scale, int precision) throws DataException {
        if(_columns.containsKey(name)) {
            throw new DataException("Column already exist!");
        }
        
        DataColumn col = new DataColumn(this, name, type, scale, precision);
        _columns.put(name, col);
        
        return col;
    }
    
    /**
     * Get specific column
     * @param name Column name
     * @return Column object
     */
    public DataColumn getColumn(String name) throws DataException {
        if(_columns.containsKey(name)) {
            return _columns.get(name);
        } else {
            throw new DataException("Column not exist!");
        }
    }
    /**
     * Remove column from table
     * @param name Name of column to be removed
     * @throws DataException
     */
    public void RemoveColumn(String name) throws DataException {
        if(!_columns.containsKey(name))
            throw new DataException("Column not exist!");
        DataColumn col = _columns.get(name);
        _columns.remove(name);
        col.AfterRemoved();
    }
    
    /**
     * Get all rows
     * @return 
     */
    public List<DataRow> getRows() {
        return _rows;
    }
    
    /**
     * Create a new row base on this table's schema. 
     * New row could only be append to the same table.
     * @return Newly created data row
     * @throws DataException 
     */
    public DataRow NewRow() throws DataException {
        DataRow row = new DataRow(this);
        return row;
    }
    
    /**
     * Append newly created row to this table
     * @param row Row to be appended
     * @throws DataException 
     */
    public void AppendRow(DataRow row) throws DataException {
        // TODO Check PK and unique index for AppendRow
        row.setNew(true);
        _rows.add(row);
    }
    
    /**
     * Remove row from data table
     * @param row Row to be removed
     * @throws DataException 
     */
    public void RemoveRow(DataRow row) throws DataException {
        _rows.remove(row);
    }
    
    /**
     * Accept changes made in this table
     * @throws DataException 
     */
    public void AcceptChanges() throws DataException {
        for(DataRow row : _rows) {
            if(row.isDeleted()) {
                // Row should be deleted
                RemoveRow(row);
            } else {
                row.AcceptChanges();
            }
        }
    }
    
    /**
     * Reject changes made in this table
     * @throws DataException 
     */
    public void RejectChanges() throws DataException {
        for(DataRow row : _rows) {
            if(row.isNew()) {
                RemoveRow(row);
            }else {
                if(row.isDeleted()) {
                    row.setDeleted(false);
                }
                row.RejectChanges();
            }
        }
    }

    /**
     * Read data from result set
     * @param rs
     * @throws DataException 
     */
    public void Read(ResultSet rs) throws DataException {
        if(rs == null) 
            throw new DataException("Result set is null!");
        
        try {
            if(!rs.next())
                return; // no data

            do {
                DataRow newRow = NewRow();
                newRow.Read(rs);
                AppendRow(newRow);
            } while(rs.next());
            
            AcceptChanges();
            
        } catch (SQLException ex) {
            DataException err = new DataException("Read data from result set failed!");
            err.initCause(ex);
            throw err;
        }
    }
}
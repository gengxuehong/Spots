package edoor.Data;

import java.lang.reflect.*;
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
    
    private static void AddColumnToTable(DataTable table, DataField annoField, Class<?> type) throws DataException {
        String fieldName = annoField.FieldName();
        int maxlen = annoField.MaxLength();
        DataColumn col = null;
        if(table.hasColumn(annoField.FieldName())) {
            // Column already exist, update its properties
            col = table.getColumn(annoField.FieldName());
            if(maxlen > col.getMaxLen()) col.setMaxLen(maxlen);
            if(!col.getType().equals(type)) throw new DataException("DataField types in annotations are not consistent!");
        } else {
            // Column not exist, create new one
            col = table.NewColumn(fieldName, type, maxlen);
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
            AddColumnToTable(table, annoField, field.getType());
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
            AddColumnToTable(table, annoField, type);
        }
        return table;
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
     * @param maxlen Max length of column. Ignored for fix size columns
     * @return Newly created column
     * @throws DataException 
     */
    public DataColumn NewColumn(String name, Class<?> type, int maxlen) throws DataException {
        if(_columns.containsKey(name)) {
            throw new DataException("Column already exist!");
        }
        
        DataColumn col = new DataColumn(this, name, type, maxlen);
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
        _columns.remove(name);
    }
    
    /**
     * Create a new row base on this table's schema. 
     * New row could only be append to the same table.
     * @return Newly created data row
     * @throws DataException 
     */
    public DataRow NewRow() throws DataException {
        DataRow row = new DataRow();
        // TODO Implement NewRow
        return row;
    }
    
    /**
     * Append newly created row to this table
     * @param row Row to be appended
     * @throws DataException 
     */
    public void AppendRow(DataRow row) throws DataException {
        // TODO Implement AppendRow
    }
    
}
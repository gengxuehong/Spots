package edoor.Data;

import java.lang.reflect.Field;
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
        Field[] fields = cls.getFields();
        for(Field field : fields) {
            DataField annoField = (DataField)field.getAnnotation(DataField.class);
            if(annoField == null) continue;
            String fieldName = annoField.FieldName();
            Class<?> type = field.getType();
            int maxlen = annoField.MaxLength();
            DataColumn col = table.NewColumn(fieldName, type, maxlen);
            if(annoField.isPrimaryKey())
                col.setPrimaryKey(annoField.Key());
            else if(annoField.isIndexed())
                col.setIndexed(annoField.Key());
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
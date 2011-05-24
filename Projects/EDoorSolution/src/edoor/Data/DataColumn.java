package edoor.Data;

import java.math.*;
import org.apache.derby.client.am.Types;

/**
 * Column in the table
 * @author Geng Xuehong
 */
public class DataColumn {
    private String _name = "";
    private String _title = "";
    private int _type = Types.VARCHAR;
    private int _scale = -1;
    private int _precision = 0;
    private boolean _isPrimaryKey = false;
    private boolean _isIndexed = false;
    private boolean _isNullable = true;
    private String _key = "";
    private String _outKey = "";
    private String _outTable="";
    private DataTable _table = null;
    
    /**
     * Tool to convert SQL type to java runtime class
     * @param sqlType SQL type
     * @return Cooresponding java class
     * @throws DataException 
     */
    public static Class<?> SQLTypeToClass(int sqlType) throws DataException {
        switch(sqlType) {
            case Types.BOOLEAN:         return boolean.class;
            case Types.SMALLINT:        return short.class;
            case Types.INTEGER:         return int.class;
            case Types.BIGINT:          return long.class;
            case Types.REAL:            return float.class;
            case Types.DOUBLE:          return double.class;
            case Types.DECIMAL:         return BigDecimal.class;
            case Types.CHAR:            return String.class;
            case Types.VARCHAR:         return String.class;
            case Types.LONGVARCHAR:     return String.class;
            case Types.DATE:            return java.sql.Date.class;
            case Types.TIME:            return java.sql.Time.class;
            case Types.TIMESTAMP:       return java.sql.Timestamp.class;
            case Types.BINARY:          return java.sql.Blob.class;
            case Types.VARBINARY:       return java.sql.Blob.class;
            case Types.LONGVARBINARY:   return java.sql.Blob.class;
            case Types.BLOB:            return java.sql.Blob.class;
            case Types.CLOB:            return java.sql.Clob.class;
            default:
                throw new DataException("Unsupported SQL type!");
        }
    }
    
    /**
     * Tool to convert java class to SQL type
     * @param cls java class
     * @return SQL type
     * @throws DataException 
     */
    public static int ClassToSQLTypes(Class<?> cls) throws DataException {
        if(cls.equals(boolean.class))       return Types.BOOLEAN;
        else if(cls.equals(short.class))    return Types.SMALLINT;
        else if(cls.equals(int.class))      return Types.INTEGER;
        else if(cls.equals(long.class))     return Types.BIGINT;
        else if(cls.equals(float.class))    return Types.REAL;
        else if(cls.equals(double.class))   return Types.DOUBLE;
        else if(cls.equals(BigDecimal.class))   return Types.DECIMAL;
        else if(cls.equals(String.class))   return Types.VARCHAR;
        else if(cls.equals(java.sql.Date.class) ||
                cls.equals(java.util.Date.class)) return Types.DATE;
        else if(cls.equals(java.sql.Time.class)) return Types.TIME;
        else if(cls.equals(java.sql.Timestamp.class)) return Types.TIMESTAMP;
        else if(cls.equals(java.sql.Blob.class)) return Types.BLOB;
        else if(cls.equals(java.sql.Clob.class)) return Types.CLOB;
        else throw new DataException("Unsupported class for SQL type!");
    }
    
    /**
     * Convert a object to specific SQL type
     * @param val
     * @param tgtSqlType
     * @return 
     */
    public static Object ConvertSQLValue(Object val, int tgtSqlType) {
        if(val == null) 
            return null;
        
        return val;
    }
    
    /**
     * Construct a emtpy column
     */
    protected DataColumn(DataTable owner) {
        _table = owner;
    }
    
    /**
     * Construct a column with properties
     * @param name Column name
     * @param type Data type
     * @param maxlen Max length of column
     */
    protected DataColumn(DataTable owner, String name, int type, int scale, int precision) {
        _name = name;
        _type = type;
        _scale = scale;
        _precision = precision;
    }
    
    protected void AfterRemoved() {
        _table = null;
    }
    
    /**
     * Describe column info to a string builder
     * @param out String builder
     */
    public void Describe(StringBuilder out) {
        String str = String.format("%s\t %s (%d.%d)", _name, Types.getTypeString(_type), _scale, _precision);
        out.append(str);
        if(_isPrimaryKey) {
            str = String.format("\tPK(%s)", _key);
            out.append(str);
        } else if(_isIndexed) {
            str = String.format("\tIdx(%s)", _key);
            out.append(str);
        }
        out.append("\n");
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setType(int type) {
        _type = type;
    }
    
    public int getType() {
        return _type;
    }
    
    public void setScale(int maxLen) {
        _scale = maxLen;
    }
    
    public int getScale() {
        return _scale;
    }
    
    public void setPrecision(int precision) {
        _precision = precision;
    }
    
    public int getPrecision() {
        return _precision;
    }
    
    public void setPrimaryKey(String key) {
        _isPrimaryKey = true;
        _isIndexed = true;
        _key = key;
    }
    public void removeFromPrimaryKey() {
        _isPrimaryKey = false;
        _isIndexed = false;
        _key = "";
    }
    public boolean isPrimaryKey() {
        return _isPrimaryKey;
    }
    
    public void setIndexed(String key) {
        _isIndexed = true;
        _key = key;
    }
    public void removeFromIndex() {
        _isIndexed = false;
        _key = "";
    }
    public boolean isIndexed() { 
        return _isIndexed;
    }
    
    public String getKey() {
        return _key;
    }
    
    public void setNullable(boolean isNullable) {
        _isNullable = isNullable;
    }
    public boolean isNullable() {
        return _isNullable;
    }
    
    public void setOutKey(String outTable, String outKey) {
        _outTable = outTable;
        _outKey = outKey;
    }
    public boolean isOutKey() {
        return !_outKey.isEmpty();
    }
    public String getOutTable() {
        return _outTable;
    }
    public String getOutKey() {
        return _outKey;
    }
    
    /**
     * Get class of column type
     * @return 
     */
    protected Class<?> getTypeClass() throws DataException {
        return SQLTypeToClass(_type);
    }
    
    /**
     * Cast a object to its value
     * @param val
     * @return 
     */
    public Object cast(Object val) throws DataException {
        if(val == null) return null;
        Class<?> cls = getTypeClass();
        if(cls.isAssignableFrom(val.getClass())) {
            return val;
        } else {
            Variable var = new Variable(val);
            Object res = var.ConvertTo(cls);
            if(res == null)
                throw new DataException("Invalid data convertion!");
            return res;
        }
    }
}

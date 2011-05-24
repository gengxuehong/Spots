package edoor.Data;

import java.math.*;
import java.util.Date;
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
        switch(_type) {
            case Types.BIGINT:      return long.class;
            case Types.BOOLEAN:     return boolean.class;
            case Types.CHAR:        return String.class;
            case Types.DATE:        return Date.class;
            case Types.DECIMAL:     return BigDecimal.class;
            case Types.DOUBLE:      return double.class;
            case Types.INTEGER:     return Integer.class;
            case Types.REAL:        return double.class;
            case Types.SMALLINT:    return short.class;
            case Types.TIME:        return Date.class;
            case Types.TIMESTAMP:   return Date.class;
            case Types.VARCHAR:     return String.class;
            case Types.LONGVARCHAR: return String.class;
            default:
                throw new DataException("Unsupported SQL type!");
        }
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
            return cls.cast(val);
        }
    }
}

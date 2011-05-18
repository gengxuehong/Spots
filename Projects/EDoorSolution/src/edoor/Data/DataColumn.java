package edoor.Data;

/**
 * Column in the table
 * @author Geng Xuehong
 */
public class DataColumn {
    private String _name = "";
    private Class<?> _type = null;
    private int _maxlen = -1;
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
    protected DataColumn(DataTable owner, String name, Class<?> type, int maxlen) {
        _name = name;
        _type = type;
        _maxlen = maxlen;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setType(Class<?> type) {
        _type = type;
    }
    
    public Class<?> getType() {
        return _type;
    }
    
    public void setMaxLen(int maxLen) {
        _maxlen = maxLen;
    }
    
    public int getMaxLen() {
        return _maxlen;
    }
}

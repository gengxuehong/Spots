package edoor.Data;

/**
 * Column in the table
 * @author Geng Xuehong
 */
public class DataColumn {
    private String _name = "";
    private Class<?> _type = null;
    private int _maxlen = -1;
    private boolean _isPrimaryKey = false;
    private boolean _isIndexed = false;
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
    protected DataColumn(DataTable owner, String name, Class<?> type, int maxlen) {
        _name = name;
        _type = type;
        _maxlen = maxlen;
    }
    
    /**
     * Describe column info to a string builder
     * @param out String builder
     */
    public void Describe(StringBuilder out) {
        String str = String.format("%s\t %s (%d)", _name, _type.getName(), _maxlen);
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
    
}

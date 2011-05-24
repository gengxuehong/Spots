package edoor.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;

/**
 * class for accessing data stored in a row
 * @author Geng Xuehong
 *
 */
public class DataRow {
    public static int NEW = 0x00000001;
    public static int CHANGED = 0x00000002;
    public static int DELETED = 0x00000004;
    
    private DataTable _owner = null;
    private int _state = NEW;

    /**
     * Inner class for cells in the row
     */
    protected class DataCell {
        private DataColumn _column = null;
        private Object _baseVal = null;
        private Object _curVal = null;
        private boolean _changed = false;
        
        public DataCell(DataColumn col) {
            if(col == null)
                throw new NullPointerException();
            _column = col;
        }
        
        /**
         * Set value of cell
         * @param val New value
         */
        public void setValue(Object val) throws DataException {
            if(val == null) {
                if(!_column.isNullable())
                    throw new DataException("Cannot set null to column that is not null able!");
            }
            Object castedVal = _column.cast(val);
            if(_baseVal == null) {
                // No base value. It's first time set it
                _baseVal = castedVal;
                _curVal = castedVal;
                _changed = false;
            } else {
                // Has base value. Changed
                _curVal = castedVal;
                _changed = true;
            }
        }
        
        /**
         * Get value of cell
         * @return
         * @throws DataException 
         */
        public Object getValue() throws DataException {
            return _curVal;
        }
        
        /**
         * Accept changed value
         * @throws DataException 
         */
        public void AcceptChange() throws DataException {
            _baseVal = _curVal;
            _changed = false;
        }
        
        /**
         * Reject changed value
         * @throws DataException
         */
        public void RejectChange() throws DataException {
            _curVal = _baseVal;
            _changed = false;
        }

        /**
         * Read data from Result set
         * @param rs Result set
         * @param iCol Index of column where data read from
         */
        private void Read(ResultSet rs, int iCol) throws DataException {
            if(rs == null)
                throw new DataException("ResultSet is null!");
                
            try {
                Object val = null;
                switch(_column.getType()) {
                    case Types.BIGINT: val = rs.getLong(iCol); break;
                    case Types.BOOLEAN: val = rs.getBoolean(iCol); break;
                    case Types.CHAR: val = rs.getString(iCol); break;
                    case Types.DATE: val = rs.getDate(iCol); break;
                    case Types.DECIMAL: val = rs.getBigDecimal(iCol); break;
                    case Types.DOUBLE: val = rs.getDouble(iCol); break;
                    case Types.FLOAT: val = rs.getFloat(iCol); break;
                    case Types.INTEGER: val = rs.getInt(iCol); break;
                    case Types.LONGNVARCHAR: val = rs.getString(iCol); break;
                    case Types.LONGVARCHAR: val = rs.getString(iCol); break;
                    case Types.NCHAR: val = rs.getNString(iCol); break;
                    case Types.NUMERIC: val = rs.getInt(iCol); break;
                    case Types.NVARCHAR: val = rs.getNString(iCol); break;
                    case Types.REAL: val = rs.getDouble(iCol); break;
                    case Types.ROWID: val = rs.getRowId(iCol); break;
                    case Types.SMALLINT: val = rs.getShort(iCol); break;
                    case Types.TIME: val = rs.getTime(iCol); break;
                    case Types.TIMESTAMP: val = rs.getTimestamp(iCol); break;
                    case Types.TINYINT: val = rs.getByte(iCol); break;
                    case Types.VARCHAR: val = rs.getString(iCol); break;
                }
                setValue(val);
            } catch (SQLException ex) {
                DataException err = new DataException("Read data from ResultSet failed!");
                err.initCause(ex);
                throw err;
            }
        }
    }
    
    private LinkedHashMap<String, DataCell> _cells = new LinkedHashMap<String, DataCell>();
    
    protected DataRow(DataTable owner) {
        if(owner == null)
            throw new NullPointerException();
        _owner = owner;
        InitRow();
    }
    
    /**
     * Initialize data row
     */
    private void InitRow() {
        _cells.clear();
        for(DataColumn col : _owner.getColumns()) {
            DataCell cell = new DataCell(col);
            _cells.put(col.getName(), cell);
        }
    }
    
    /**
     * Set field value of column
     * @param field Column name
     * @param value Field value
     */
    public void setField(String field, Object value) throws DataException {
        if(!_cells.containsKey(field)) {
            throw new DataException("Field not exist!");
        }
        if(isDeleted()) {
            throw new DataException("DataRow already deleted!");
        }
        DataCell cell = _cells.get(field);
        cell.setValue(value);
        _state |= CHANGED;
    }
    
    /**
     * Get field value of column
     * @param field Column name
     * @return Field value
     */
    public Object getField(String field) throws DataException {
        if(!_cells.containsKey(field)) {
            throw new DataException("Field not exist!");
        }
        if(isDeleted()) {
            throw new DataException("DataRow already deleted!");
        }
        DataCell cell = _cells.get(field);
        return cell.getValue();
    }
    
    /** 
     * Accept all changes made on this row
     */
    public void AcceptChanges() throws DataException {
        for(DataCell cell : _cells.values()) {
            cell.AcceptChange();
        }
        _state &= ~CHANGED;
    }
    
    /**
     * Reject all changes mde on this row
     */
    public void RejectChanges() throws DataException {
        for(DataCell cell : _cells.values()) {
            cell.AcceptChange();
        }        
        _state &= ~CHANGED;
    }
    
    /**
     * Change isNew state of row
     * @param isNew true if change to NEW
     */
    public void setNew(boolean isNew) {
        if(isNew)
            _state |= NEW;
        else
            _state &= ~NEW;
    }
    
    /**
     * Check if the row is NEW one
     * @return true if it's NEW
     */
    public boolean isNew() {
        return (_state & NEW) == 0 ? false : true;
    }
    
    /**
     * Change isDeleted state of row
     * @param isDeleted true if row deleted
     */
    public void setDeleted(boolean isDeleted) {
        if(isDeleted)
            _state |= DELETED;
        else 
            _state &= ~DELETED;
    }
    
    /**
     * Check if row is deleted
     * @return true if row deleted
     */
    public boolean isDeleted() {
        return (_state & DELETED) == 0 ? false : true;
    }

    /**
     * Read data from current cusor of ResultSet
     * @param rs ResultSet
     */
    void Read(ResultSet rs) throws DataException {
        try {
            if(rs == null)
                throw new DataException("ResultSet is null!");
            if(rs.isClosed())
                throw new DataException("ResultSet already closed!");
            
            for(String colName : _cells.keySet()) {
                int iCol = -1;
                try { iCol = rs.findColumn(colName); } catch (Throwable err) { continue; }
                DataCell cell = _cells.get(colName);
                cell.Read(rs, iCol);
            }
        } catch (SQLException ex) {
            DataException err = new DataException("Read row from ResultSet failed!");
            err.initCause(ex);
            throw err;
        }
        
    }
    
}

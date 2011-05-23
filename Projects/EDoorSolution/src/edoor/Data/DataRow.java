package edoor.Data;

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
}

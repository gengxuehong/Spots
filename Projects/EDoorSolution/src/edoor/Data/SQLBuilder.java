/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data;

import java.util.*;

/**
 * Tool to build SQL statement
 * @author Geng Xuehong
 */
public class SQLBuilder {
    ISqlFormater _formater = null;
    
    public SQLBuilder(ISqlFormater fmt) {
        _formater = fmt;
    }
    
    /**
     * Tool to generate SQL for creating table
     * @param table
     * @return 
     */
    public String SqlForCreateTable(DataTable table) {
        StringBuilder s = new StringBuilder();
        s.append("CREATE TABLE ");
        s.append(table.getName());
        s.append("( ");
        boolean bFirst = true;
        // Create all columns
        ArrayDeque<DataColumn> PK = new ArrayDeque<DataColumn>();
        for(DataColumn col : table.getColumns()) {
            if(!bFirst)
                s.append(", ");
            bFirst = false;
            s.append(FormatColumnCreation(col));
            if(col.isPrimaryKey())
                PK.add(col);
        }
        // Create primary key
        if(PK.size() > 0) {
            bFirst = true;
            s.append(", PRIMARY KEY (");
            for(DataColumn col : PK) {
                if(!bFirst)
                    s.append(", ");
                bFirst = false;
                s.append(col.getName());
            }
            s.append(" )");
        }
        s.append(" )");
        return s.toString();
    }
    
    /**
     * Generate string of column type 
     * @param col DataColumn used for type generation
     * @return String with column type
     */
    protected String FormatColumnCreation(DataColumn col) {
        StringBuilder s = new StringBuilder();
        s.append("\"" + col.getName() + "\" ");
        String type = _formater.FormatDataType(col.getType(), col.getPrecision(), col.getScale());
        s.append(type);
        if(!col.isNullable())
            s.append(" NOT NULL");
        return s.toString();
    }
}

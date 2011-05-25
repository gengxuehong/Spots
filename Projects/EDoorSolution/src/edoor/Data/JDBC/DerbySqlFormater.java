/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data.JDBC;

import edoor.Data.*;
import java.sql.*;

/**
 * Class used to format SQL for Derby database
 * @author Geng Xuehong
 */
public class DerbySqlFormater implements ISqlFormater {

    /**
     * Get the name of SQL data type
     * @param dataType SQL data type id
     * @param precision Precision of data
     * @param scale Scale of data
     * @return Formated data type expression
     */
    @Override
    public String FormatDataType(int dataType, int precision, int scale) {
        String typeName = "";
        boolean usePrecision = false;
        boolean useScale = false;
        StringBuilder s = new StringBuilder();
        switch(dataType) {
            case Types.BIGINT: typeName = "BIGINT"; break;
            case Types.BLOB: typeName = "BLOB"; break;
            case Types.CHAR: typeName = "CHAR"; usePrecision = true; if(precision < 1) precision = 10; break;
            case Types.CLOB: typeName = "CLOB"; break;
            case Types.DATE: typeName = "DATE"; break;
            case Types.DECIMAL: typeName = "DECIMAL"; usePrecision = true; useScale = true; break;
            case Types.DOUBLE: typeName = "DOUBLE"; break;
            case Types.FLOAT: typeName = "FLOAT"; break;
            case Types.INTEGER: typeName = "INTEGER"; break;
            case Types.LONGNVARCHAR: typeName = "LONG VARCHAR"; usePrecision = true; if(precision < 1) precision = 512; break;
            case Types.LONGVARBINARY: typeName = "BLOB"; usePrecision = true; break;
            case Types.LONGVARCHAR: typeName = "LONG VARCHAR"; usePrecision = true; if(precision < 1) precision = 512; break;
            case Types.NCHAR: typeName = "CHAR"; usePrecision = true; if(precision < 1) precision = 10; break;
            case Types.NCLOB: typeName = "CLOB"; usePrecision = true; break;
            case Types.NUMERIC: typeName = "NUMERIC"; usePrecision = true; useScale = true; break;
            case Types.NVARCHAR: typeName = "VARCHAR"; usePrecision = true; if(precision < 1) precision = 128; break;
            case Types.REAL: typeName = "REAL"; break;
            case Types.ROWID: typeName = "INTEGER"; break;
            case Types.SMALLINT: typeName = "SMALLINT"; break;
            case Types.SQLXML: typeName = "XML"; usePrecision = true; break;
            case Types.TIME: typeName = "TIME"; break;
            case Types.TIMESTAMP: typeName = "TIMESTAMP"; break;
            case Types.TINYINT: typeName = "SMALLINT"; break;
            case Types.VARBINARY: typeName = "BLOB"; usePrecision = true; break;
            case Types.VARCHAR: typeName = "VARCHAR"; usePrecision = true; if(precision < 1) precision = 128; break;
            default:
                throw new java.lang.IllegalArgumentException();
        }
        s.append(typeName);
        if(usePrecision) {
            s.append("(");
            s.append(precision);
            if(useScale && scale > 0) {
                s.append(",");
                s.append(scale);
            }
            s.append(")");
        }
        return s.toString();
    }
    
}

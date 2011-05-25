/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data;

/**
 * Interface for helper object used to generate sql statement
 * @author Geng Xuehong
 */
public interface ISqlFormater {
    /**
     * Get the name of SQL data type
     * @param dataType SQL data type id
     * @param scale Scale of data
     * @param precision Precision of data
     * @return Formated data type expression
     */
    String FormatDataType(int dataType, int scale, int precision);
    
}

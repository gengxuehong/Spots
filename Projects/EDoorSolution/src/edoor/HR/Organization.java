/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.HR;

import edoor.Data.*;

/**
 * Organization is the basic org-chart node for any enterprices and institutions.
 * @author Geng Xuehong
 */
@DataRecord(TableName="Organization")
public class Organization {
    private String _id = "";
    
    @DataField(FieldName="ID", MaxLength=256, isPrimaryKey=true)
    public String getID() {
        return _id;
    }
    @DataField(FieldName="ID", MaxLength=256, isPrimaryKey=true)
    public void setID(String id) {
        _id = id;
    }
    
    @DataField(FieldName="Name", MaxLength=128, isIndexed=true)
    public String _Name = "";
    @DataField(FieldName="Desc")
    public String _Description = "";
}

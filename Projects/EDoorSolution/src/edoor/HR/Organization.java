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
    @DataField(FieldName="ID", MaxLength=256, isPrimaryKey=true)
    public String _ID = "";
    @DataField(FieldName="Name", MaxLength=128, isIndexed=true)
    public String _Name = "";
    @DataField(FieldName="Desc")
    public String _Description = "";
}

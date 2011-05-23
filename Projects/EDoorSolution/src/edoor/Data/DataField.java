/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Data;

import java.lang.annotation.*;

/**
 *
 * @author Geng Xuehong
 */
@Target( {ElementType.FIELD, ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {
    public String FieldName() default "";
    public int Type();
    public int Scale() default -1;
    public int Precision() default -1;
    public boolean isPrimaryKey() default false;
    public boolean isIndexed() default false;
    public String Key() default "";
    public String OutKey() default "";
}

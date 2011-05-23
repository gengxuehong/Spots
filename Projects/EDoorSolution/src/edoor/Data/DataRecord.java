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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRecord {
    public String TableName() default "";
}

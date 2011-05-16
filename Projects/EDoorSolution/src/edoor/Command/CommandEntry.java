/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

import java.lang.annotation.*;

/**
 *
 * @author Geng Xuehong
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandEntry {
    String Token();
    String Syntax() default "";
}

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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandBase {
    public boolean AutoRegister() default true;
}

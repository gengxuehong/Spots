/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

/**
 * Base exception class thrown in command
 * @author Geng Xuehong
 */
public class CommandException extends Throwable {
    public CommandException(String msg) {
        super(msg);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

/**
 * Interface that must be implemented by a class that support CommandBase annotation
 * @author Geng Xuehong
 */
public interface ICommandBase {
    /**
     * Set command console for output command executing logs and results.
     * @param console IConsole interface that representing a console
     */
    void setConsole(IConsole console);
}

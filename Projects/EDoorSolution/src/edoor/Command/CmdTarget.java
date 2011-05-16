/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

/**
 * Base class for all classes that support mapping its method to command
 * @author Geng Xuehong
 */
public class CmdTarget implements ICommandBase {
    
    protected IConsole _console = null;
    
    /**
     * Set console for this command target
     * @param console Console used to output command result
     */
    @Override
    public void setConsole(IConsole console) {
        _console = console;
    }
}

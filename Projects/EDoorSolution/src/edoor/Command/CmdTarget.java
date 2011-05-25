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
    protected ICommandCenter _cmdCenter = null;
    
    /**
     * Set console for this command target
     * @param console Console used to output command result
     */
    @Override
    public void setConsole(IConsole console) {
        _console = console;
    }

    /**
     * Set command center
     * @param center Interface of command center
     */
    @Override
    public void setCommandCenter(ICommandCenter center) {
        _cmdCenter = center;
    }
    
    /**
     * Tool to write information to console
     * @param fmt Format string
     * @param args Arguments used to format string
     */
    protected void writeConsole(String fmt, Object... args) {
        if(_console == null)
            return;
        
        _console.print(fmt, args);
    }
}

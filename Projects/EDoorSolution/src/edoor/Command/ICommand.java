/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

/**
 * Interface implemented by any Command class
 * @author Geng Xuehong
 */
public interface ICommand {
    /**
     * Get command name
     * @return a string that contains command name
     */
    String getName() throws CommandException;
    
    /**
     * Get help text of this command
     * @return a string that contains command help. Might be multi-line text
     */
    String getHelp() throws CommandException;
    
    /**
     * Run command from a string that contains command line
     * @param cmdLine a string that contains command arguments
     * @param console Console that used to output command result
     * @throws CommandException 
     */
    void Run(String cmdLine, IConsole console) throws CommandException;
    
    /**
     * Run command from multiple arguments
     * @param console Console that used to output command result
     * @param args Object array contains arguments
     * @throws CommandException 
     */
    void Run(IConsole console, Object... args) throws CommandException;
}

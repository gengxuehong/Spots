/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

/**
 * Interface of command center
 * @author Geng Xuehong
 */
public interface ICommandCenter {
    /**
     * Get prompt string of command center
     * @return 
     */
    String getPrompt();
    
    /**
     * Run command with command line
     * @param cmd 
     */
    public void doCommand(String cmd) throws CommandException;
        
    /**
     * Install a command hook to this command center.
     * A hook is another command center which take over all command inputs.
     * @param hook A command center that will act as a hook
     */
    void installHook(ICommandCenter hook);
    
    /**
     * Remove command hook
     */
    void uninstallHook() throws CommandException;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

import java.util.*;
import java.util.jar.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * Command center is a center of commands
 * @author Geng Xuehong
 */
@CommandBase
public class CommandCenter 
    implements ICommandBase
{
    private static Hashtable<String, ICommand> _Commands = new Hashtable<String, ICommand>();
    private IConsole _console = null;
    
    public CommandCenter() {
    }

    @Override
    public void setConsole(IConsole console) {
        _console = console;
    }
    
    /**
     * Output help text of command center to console
     * @param cmd Name of command whose help text will be outputed; Empty if list all commands.
     */
    @CommandEntry(Token = "help", Syntax = "help | help %command%")
    public void Help(String cmd) {
        if(cmd == null || cmd.isEmpty()) {
            // List all available commands
            _console.print("Total %d commands. Type \"help [cmdName]\" for more help on command.\n", _Commands.isEmpty() ? 0 : _Commands.size());
            Enumeration<String> keys = _Commands.keys();
            LinkedList<String> lst = new LinkedList<String>();
            while(keys.hasMoreElements()) {
                String key = keys.nextElement();
                lst.add(key);
            }
            String[] ary = lst.toArray(new String[0]);
            Arrays.sort(ary);
            for(String key : ary) {
                _console.print("%s\n", key);
            }
        } else {
            // List syntax help for specific command
            if(_Commands.containsKey(cmd)) {
                ICommand cmdObj = _Commands.get(cmd);
                try {
                    _console.print("%s\n", cmdObj.getHelp());
                } catch (CommandException ex) {
                    //Logger.getLogger(CommandCenter.class.getName()).log(Level.SEVERE, null, ex);
                    _console.print("Cannot get help! Cause:\n%s", ex.getMessage());
                }
            }
        }
    }
    
    /**
     * Register commands in a directory
     * @param rootDir Path of root directory
     * @param rootPackage  Root package of classes in this directory
     */
    public void RegisterDir(String rootDir, String rootPackage) 
            throws  ClassNotFoundException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    CommandException 
    {
        File root = new File(rootDir);
        if(root.isDirectory()) {
            // Search all class files
            String[] clsFiles = root.list(new FilenameFilter()  {
                                    @Override
                                    public boolean accept(File dir, String name) {
                                        if(name.matches("(?i).*\\.class")) 
                                            return true;
                                        else
                                            return false;
                                    }
                                });
            for(String clsFile : clsFiles) {
                String clsName = clsFile.substring(0,clsFile.lastIndexOf("."));
                if(rootPackage != null && rootPackage.length() > 0) {
                    clsName = rootPackage + "." + clsName;
                }
                Class cls = Class.forName(clsName);
                RegisterClass(cls);
            }
            
            // Search for sub directories
            File[] subDirs = root.listFiles( new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.isDirectory())
                        return true;
                    else
                        return false;
                }
            });
            for(File subDir : subDirs) {
                String subPackage = (rootPackage == null || rootPackage.isEmpty()) ? subDir.getName() : (rootPackage + "." + subDir.getName());
                RegisterDir(subDir.getAbsolutePath(), subPackage);
            }
        }
    }
    
    /**
     * Register all commands in a Jar file
     * @param jarFile
     * @throws IOException 
     */
    public void RegisterJar(String jarFile) 
            throws  IOException,
                    ClassNotFoundException, 
                    InstantiationException, 
                    IllegalAccessException, 
                    CommandException 
    {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> e = jar.entries();
        while(e.hasMoreElements()) {
            JarEntry ent = e.nextElement();
            if(!ent.isDirectory() &&
                    ent.getName().matches("(?i).+\\.class")) {
                String strCls = getClassNameFromFileName(ent.getName());
                Class cls = Class.forName(strCls);
                // Register this class
                RegisterClass(cls);
            }
        }
    }
    
    /**
     * Get class name from file name in Jar
     * @param fileName Path of class file in jar
     * @return class name
     */
    private static String getClassNameFromFileName(String fileName) throws CommandException {
        if(!fileName.matches("(?i).+\\.class")) 
            throw new CommandException("File is not class file!");
        String strCls = fileName.substring(0, fileName.length() - ".class".length()).replace('/', '.');
        return strCls;
    }
    /**
     * Register all command in a class
     * @param cls Class to be registered
     */
    public void RegisterClass(Class cls) 
            throws InstantiationException, IllegalAccessException, CommandException
    {
        // To register a class, we need a instance
        if(!cls.isAnnotationPresent(CommandBase.class))
            return; 
        CommandBase annoCmdBase = (CommandBase)cls.getAnnotation(CommandBase.class);
        if(!annoCmdBase.AutoRegister())
            return; // Don't register this class automatically
        
        if(_console != null) {
            _console.print("Register commands in %s\n", cls.getName());
        }
        Object newInstance = cls.newInstance();
        RegisterObject(newInstance);
    }
    
    /**
     * Register commands in a object
     * @param obj 
     */
    public void RegisterObject(Object obj) throws CommandException {
        // Register all commands in a object
        Class<?> cls = obj.getClass();
        ICommandBase cmdBase = (ICommandBase)obj;
        cmdBase.setConsole(_console);
        Method[] methods = cls.getDeclaredMethods();
        for(Method m : methods) {
            CommandEntry anoMethod = m.getAnnotation(CommandEntry.class);
            if(anoMethod != null) {
                // Register this method
                RegisterMethod(obj, m);
            }
        }
    }

    /**
     * Register a method as command
     * @param obj
     * @param m 
     */
    public void RegisterMethod(Object obj, Method m) throws CommandException {
        // Create a command agent for this method
        CommandAgent agent = new CommandAgent(obj, m);
        // Register this agent
        Register(agent);
    }

    /**
     * Register a command agent to command center
     * @param agent 
     */
    private void Register(CommandAgent agent) throws CommandException {
        String strToken = agent.getCommand();
        if(_Commands.containsKey(strToken)) {
            String msg = String.format("Command %s has already been registered in Command Center!",strToken);
            throw new CommandException(msg);
        }
        
        _Commands.put(strToken, (ICommand)agent);
    }
    
    /**
     * Remove command agent from command center
     * @param cmdToken Command name
     */
    public void Unregister(String cmdToken) {
        if(_Commands.containsKey(cmdToken)) {
            _Commands.remove(cmdToken);
        }
    }
    
    /**
     * Remove all command agents from command center
     */
    public void UnregisterAll() {
        _Commands.clear();;
    }

    /**
     * Run command with command line
     * @param cmd 
     */
    public void doCommand(String cmd) throws CommandException {
        Scanner s = new Scanner(cmd);
        if(!s.hasNext("\\w+")) {
            throw new CommandException("No command! Invalid command line!");
        }
        String token = s.next("\\w+");
        // Search command object
        if(!_Commands.containsKey(token)) {
            String msg = String.format("Unknown command :\"%s\"!",token);
            throw new CommandException(msg);
        }
        ICommand command = _Commands.get(token);
        String params = cmd.substring(token.length()).trim();
        command.Run(params, _console);
    }
    
}

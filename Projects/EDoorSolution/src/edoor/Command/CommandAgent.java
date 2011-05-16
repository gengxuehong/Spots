/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edoor.Command;

import java.lang.reflect.Method;
import java.util.Scanner;

/**
 * Class that used to convert Command execution to method calling
 * @author Geng Xuehong
 */
public class CommandAgent 
    implements ICommand
{
    private Object _obj = null;
    private Method _method = null;
    
    /**
     * Construct a command agent with target object and target method
     * @param obj
     * @param m 
     */
    public CommandAgent(Object obj, Method m) {
        _obj = obj;
        _method = m;
    }

    /**
     * Get command token name
     * @return a string with command name
     */
    public String getCommand() throws CommandException {
        CommandEntry ent = (CommandEntry)_method.getAnnotation(CommandEntry.class);
        if(ent == null) {
            String msg = String.format("Method %s is not a command entry!", _method.getName());
            throw new CommandException(msg);
        }
        return ent.Token();
    }
    
    /**
     * Generate command Syntax description
     * @return a string with command syntax
     */
    public String getSyntax() {
        StringBuilder str = new StringBuilder();
        str.append(_method.getName());
        // Handle parameters
        Class<?>[] paramTypes = _method.getParameterTypes();
        for(Class<?> pt : paramTypes) {
            str.append(" #");
            str.append(pt.getName());
            str.append("#");
        }
        return str.toString();
    }
    
    /**
     * Get help text for this command
     * @return 
     */
    @Override
    public String getHelp() throws CommandException {
        CommandEntry ent = (CommandEntry)_method.getAnnotation(CommandEntry.class);
        if(ent == null) {
            String msg = String.format("Method %s is not a command entry!", _method.getName());
            throw new CommandException(msg);
        }
        String syntax = ent.Syntax();
        if(syntax == null || syntax.length() < 1)
            syntax = getSyntax();
        StringBuilder builder = new StringBuilder();
        builder.append("Syntax:\n");
        builder.append(syntax);
        return builder.toString();
    }

    /**
     * Get command name. In CommandAgent object, the command name are get from 
     * the annotation of method of a command base object.
     * @return a string with command name.
     * @throws CommandException 
     */
    @Override
    public String getName() throws CommandException {
        return getCommand();
    }

    /**
     * Parse command line and run it. 
     * @param cmdLine a string with command line. Command line must not include 
     * the command name and all spaces on left and right side of command line will 
     * be trimed.
     * @param console Console used to output command result.
     * @throws CommandException 
     */
    @Override
    public void Run(String cmdLine, IConsole console) throws CommandException {
        // Trim spaces on both side
        cmdLine = cmdLine.trim();
        // Parse parameters
        Class<?>[] paramTypes = _method.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        Scanner s = new Scanner(cmdLine);
        Integer idx = 0;
        int iValid = 0;
        for(int i=0; i<paramTypes.length; i++) {
            // Find out next parameter
            String strParam = findNextParameter(cmdLine, idx);
            // Parse it
            try {
                params[i] = parseParameter(strParam, paramTypes[i]);
            } catch (Throwable err) {
                String msg = String.format("Cannot parse parameter %d!", i);
                CommandException ex = new CommandException(msg);
                ex.initCause(err);
                throw ex;
            }
            iValid++;
        }
        if(iValid < paramTypes.length)
            throw new CommandException("Argument missing!");
        // Call method
        try {
            _method.invoke(_obj, params);
        } catch (Throwable err) {
            CommandException ex = new CommandException("Invoke method failed!");
            ex.initCause(err);
            throw ex;
        }        
    }

    /**
     * Parse a string to a type of object
     * @param str a string with formatted expression of a object
     * @param type Type of object used to do parsing
     * @return Parsed object
     */
    public static Object parseParameter(String str, Class<?> type) {
        Scanner s = new Scanner(str);
        Object val = null;
        if(s.hasNextBigDecimal()) { val = s.nextBigDecimal(); }
        else if(s.hasNextBigInteger()) { val = s.nextBigInteger(); }
        else if(s.hasNextBigInteger(16)) { val = s.nextBigInteger(16); }
        else if(s.hasNextBoolean()) { val = s.nextBoolean(); }
        else if(s.hasNextByte()) { val = s.nextByte(); }
        else if(s.hasNextByte(16)) { val = s.nextByte(16); }
        else if(s.hasNextDouble()) { val = s.nextDouble(); }
        else if(s.hasNextFloat()) { val = s.nextFloat(); }
        else if(s.hasNextInt()) { val = s.nextInt(); }
        else if(s.hasNextInt(16)) { val = s.nextInt(16); }
        else if(s.hasNextLong()) { val = s.nextLine(); }
        else if(s.hasNextLong(16)) { val = s.nextLong(16); }
        else if(s.hasNextShort()) { val = s.nextShort(); }
        else if(s.hasNextShort(16)) { val = s.nextShort(16); }
        else {
            val = str;
        }
        return type.cast(val);
    }
    /**
     * Check if a char is space
     * @param c Char to be checked
     * @return true if it is a space char
     */
    public static boolean isSpaceChar(char c) {
        if(c == ' ' || c == '\n' || c == '\r' || c== '\t')
            return true;
        else
            return false;
    }
    
    /**
     * Find next parameter string in command line
     * @param cmdLine String contains command line
     * @param iPos Position where search start at. End of parameter when returned.
     * @return Found parameter string
     */
    public static String findNextParameter(String cmdLine, Integer iPos) {
        // Jump to next non space char
        int i = iPos;
        while(i < cmdLine.length() && isSpaceChar(cmdLine.charAt(i)))
            i++;
        int iBegin = i;
        StringBuilder buf = new StringBuilder();
        // Find end of this parameter
        boolean bInString = false;
        while(i < cmdLine.length()) {
            if(cmdLine.charAt(i) == '"') {
                // Found a quotation mark
                if(bInString) { 
                    // This is a string parameter that enclosed with quotation marks
                    if(i-1 <= iBegin || cmdLine.charAt(i-1) != '\\') {
                        // This quotation mark is the end of string
                        bInString = false;
                    } else {
                        // This quotation mark is part of string
                        buf.append('"');
                    }
                } else {
                    // Begin a string
                    bInString = true;
                }
            } else if(isSpaceChar(cmdLine.charAt(i))) {
                // Found a space
                if(!bInString) {
                    // Not in string. Should be end
                    break;
                } else {
                    buf.append(cmdLine.charAt(i));
                }
            } else if(cmdLine.charAt(i) == '\\') {
                // Found a back slash
                if(!bInString) {
                    // Not in string, should be part of parameter
                    buf.append(cmdLine.charAt(i));
                }
            } else {
                // Other chars
                if(bInString && i-1 > iBegin && cmdLine.charAt(i-1) == '\\') {
                    // Make sure back slash been added if it is no used to specify a quotation mark in sring
                    buf.append('\\');
                }
                buf.append(cmdLine.charAt(i));
            }
            i++;
        }
        iPos = i;
        return buf.toString();
    }
    
    /**
     * Run command by using parameters transferred via args.
     * @param console Console used to output command result.
     * @param args a Object array with parameters of command. It must matched to 
     * the argument list of base command method of command base object.
     * @throws CommandException 
     */
    @Override
    public void Run(IConsole console, Object... args) throws CommandException {
        Class<?>[] paramTypes = _method.getParameterTypes();
        // Check the parameters
        if(args.length != paramTypes.length)
            throw new CommandException("Command arguments count is not matched!");
        Object[] params = new Object[args.length];
        for(int i=0; i < args.length; i++) {
            try {
                params[i] = paramTypes[i].cast(args[i]);
            } catch (Throwable err) {
                String msg = String.format("Convert argument %d to %s failed!", i+1, paramTypes[i].getName());
                CommandException ex = new CommandException(msg);
                ex.initCause(err);
                throw ex;
            }
        }
        // Call method
        try {
            _method.invoke(_obj, params);
        } catch (Throwable err) {
            CommandException ex = new CommandException("Invoke method failed!");
            ex.initCause(err);
            throw ex;
        }
    }
}

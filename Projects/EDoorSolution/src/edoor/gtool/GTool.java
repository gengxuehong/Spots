package edoor.gtool;

import edoor.Command.*;
import edoor.Utils.FileSys.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GTool {
        private static CommandCenter _CmdCenter = new CommandCenter();
        private static GToolConsole _console = new GToolConsole();
        
	public static void main(String[] args){
                _CmdCenter.setConsole(_console);
                
		if(!RegisterCommands())
                    return;
                
		if(args.length > 0) {
                    StringBuilder strBuilder = new StringBuilder();
                    for(String item : args) {
                        strBuilder.append(item);
                        strBuilder.append(" ");
                    }
                    DoCmd(strBuilder.toString());
		} else {
                    // Enter interaction mode
                    CmdLoop();
                }
	}
	
        private static boolean RegisterCommands() {
            try {
                String strRoot = "";
                URL resource = GTool.class.getResource("/");
                if(resource == null) {
                    URL location = GTool.class.getProtectionDomain().getCodeSource().getLocation();
                    strRoot = location.getPath();
                } else {
                    strRoot = resource.getPath();
                }
                System.out.printf("Register commands in %s\n", strRoot);
                if(strRoot.toLowerCase().endsWith(".jar")) {
                    _CmdCenter.RegisterJar(strRoot);
                } else {
                    _CmdCenter.RegisterDir(strRoot, "");
                }
            } catch(Throwable err) {
                System.out.printf("Register commands failed!\n Error:\n%s\n", err.getMessage());
                Logger.getLogger(GTool.class.getName()).log(Level.SEVERE, null, err);
                return false;
            }
            return true;
        }
        
        public static void CmdLoop() {
            while(true) {
               System.out.print(">");
               java.util.Scanner s = new java.util.Scanner(System.in);
               String strLine = s.nextLine();
               if(strLine.equalsIgnoreCase("exit")) {
                   break;
               }
               DoCmd(strLine);
            }
        }
        
        public static void DoCmd(String cmd) {
            try {
                _CmdCenter.doCommand(cmd);
            } catch (CommandException ex) {
                //Logger.getLogger(GTool.class.getName()).log(Level.SEVERE, null, ex);
                System.out.printf("Error:%s\n", ex.getMessage());
                ex.printStackTrace();
            }
        }
}

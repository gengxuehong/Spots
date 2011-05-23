package edoor.gtool;

import edoor.Command.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import edoor.Data.*;

public class GTool {
        private static CommandCenter _CmdCenter = new CommandCenter();
        private static GToolConsole _console = new GToolConsole();
        private static DataCenter _dataCenter = null;
        
	public static void main(String[] args){
                _CmdCenter.setConsole(_console);
                
                // Initialize data center
                if(!InitDataCenter())
                    return;
                
                // Register all commands
		if(!RegisterCommands())
                    return;
                
                // Enter interaction mode
                CmdLoop();
	}
	
        private static boolean InitDataCenter() {
            try {
                _dataCenter = new DataCenter("edoor.Data.JDBC.JDBCDataSource");
            } catch (DataException ex) {
                System.out.printf("Initialize Data Center failed!\nError:\n%s", ex.getMessage());
                ex.printStackTrace();
                return false;
            }
            return true;
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
                
                // For data center, we should register it now
                _CmdCenter.RegisterObject(_dataCenter);
                
            } catch(Throwable err) {
                System.out.printf("Register commands failed!\n Error:\n%s\n", err.getMessage());
                Logger.getLogger(GTool.class.getName()).log(Level.SEVERE, null, err);
                return false;
            }
            return true;
        }
        
        public static void CmdLoop() {
            while(true) {
               System.out.print(_CmdCenter.getPrompt());
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

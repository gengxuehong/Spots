package edoor.Utils.FileSys;

import edoor.Command.*;
import java.io.*;
import java.util.*;

/**
 * Class that provide tools for access directories
 * @author Geng Xuehong
 */
@CommandBase
public class DirectoryExplorer extends CmdTarget
{
    /**
     * List all leaf folders into a linked list
     * @param rootPath path of root directory
     * @param lst a linked string list used to hold all leaf folders
     * @return count of leaf folders found
     */
    public static int ListLeafFolders(String rootPath, LinkedList<String> lst) {
            int cnt = 0;
            File root = new File(rootPath);
            if(root.isDirectory())
            {
                    File[] subDirs = root.listFiles( new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                    if(f.isDirectory())
                                            return true;
                                    else
                                            return false;
                            }
                    });

                    if(subDirs == null || subDirs.length < 1){
                            lst.add(root.getPath());
                            cnt ++;
                    } else {
                            for(File f : subDirs) {
                                    cnt += ListLeafFolders(f.getPath(), lst);
                            }
                    }
            }
            return cnt;
    }

    /**
     * Show leaf directories into console
     * @param root path of root directory
     */
    @CommandEntry( Token="ShowLeafDirs", Syntax="ShowLeafDirs [root path]")
    public void ShowLeafDirs(String root) {
            _console.print("List leaf directories for %s\n", root);
            LinkedList<String> lst = new LinkedList<String>();
            int cnt = DirectoryExplorer.ListLeafFolders(root, lst);
            _console.print("Total %d leaf directories.\n", cnt);
            for(String path : lst) {
                    _console.print("%s\n", path);
            }
    }
        
}

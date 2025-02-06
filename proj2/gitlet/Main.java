package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;

import static gitlet.Repository.*;
import static gitlet.Utils.error;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Tim lin
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw error("Please enter a command.");
        }
        if (!Objects.equals(args[0], "init")) {
            setupPersistent();
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // Check if .gitlet/ exist
                // It seems that latter function need to check if the .gitlet exist, feel free to delete it if not
                File git = Utils.join(CWD, ".gitlet");
                boolean gitExist = git.exists();
                if (gitExist) {
                    throw error("A Gitlet version-control system already exists in the current directory.");
                }
                init();
                break;
            case "add":
                checkLength(args, 2, null);
                File target = Utils.join(CWD, args[1]);
                if (!target.exists()) {
                    throw error("File does not exist.");
                } else {
                    add(target, args[1]);
                }
                break;
            case "commit":
                checkLength(args, 2, "Please enter a commit message.");
                new Commit(args[1]);
                break;
            case "rm":
                checkLength(args, 2, null);
                remove(args[1]);
                break;
            case "log":
                checkLength(args, 1, null);
                HashSet<Commit> REFS =  new HashSet<>();
                for (String name : Utils.plainFilenamesIn(REFS_DIR)) {
                    File refs = Utils.join(REFS_DIR, name); 
                    REFS.add(Utils.readObject(refs, Pointer.class).getCommit());
                }
                log(REFS);
                break;
            case "global-log":
                checkLength(args, 1, null);
                globalLog();
                break;
            case "find":
                checkLength(args, 2, null);
                find(args[1]);
                break;
            case "status":
                status();
                break;
            case "checkout":
                switch (args.length) {
                    case 2:
                        checkoutBranch(args[1]);
                        break;
                    case 3:
                        checkoutReplace(args[2]);
                        break;
                    case 4:
                        checkoutSwitch(args[1], args[3]);
                        break;
                    default:
                        throw error("Incorrect operands.");
                }
                break;
            case "branch":
                checkLength(args, 2, null);
                branch(args[1]);
                break;
            case "rm-branch":
                checkLength(args, 2, null);
                removeBranch(args[1]);
                break;
            case "reset":
                checkLength(args, 2, null);
                reset(args[1]);
                break;
            case "merge":

                break;
            case "debug":
                System.out.println(getMaster().getMsg());
                System.out.println(getMasterByCommit().getKeySets());
                break;
            default:
                throw error("No command with that name exists.");
        }
    }
    private static void checkLength(String[] args, int length, String msg) {
        if (args.length != length) {
            if (msg == null) {
                throw error("Incorrect operands.");
            } else {
                throw error(msg);
            }
        }
    }
}

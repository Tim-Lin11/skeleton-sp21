package gitlet;

import javax.crypto.Cipher;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static gitlet.Commit.shaCommit;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Tim lin
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    //private HashMap<String,Commit> commitMap;
    private static Commit head;
    private Commit mergeHead;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /** The blobs' directory. */
    public static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    /** The commits' directory. */
    public static final File COMMITS_DIR = Utils.join(GITLET_DIR, "commits");
    /** The staged directory. */
    public static final File STAGED_DIR = Utils.join(GITLET_DIR, "staged");
    /** The heads of branches. */
    public static final File REFS_DIR = Utils.join(GITLET_DIR, "refs");


    /* TODO: fill in the rest of this class. */

    /** create the running file. */
    public static void setupPersistent() {
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        STAGED_DIR.mkdir();
        REFS_DIR.mkdir();
    }
    /** Start up an initial commit in the commit linked list, set the time to 0. */
    public static void init() {
        head = new Commit("initial commit");
    }

    /** First, check if the file is identical to the existing file in the current Commit
     * if so, don't update.*/
    public static void add(File file, String name) {
        // check if the file is identical the Master's file by using SHA-1 code.
        Commit master = getMaster().getCommit();
        String SHA = SHA(file);
        boolean iden = false;
        File toStaged = Utils.join(STAGED_DIR, name);
        if (master.getFilesMap() != null) {
            for (String code : master.getFilesMap().values()) {
                if (SHA.equals(code)) {
                    iden = true;
                    break;
                }
            }
        }
        // If different, add it to the staged.
        if (!iden) {
            String content = Utils.readContentsAsString(file);
            Utils.writeContents(toStaged, content);
        } else {//if identical, Check if the staged area has the file, if so, remove it.
            for (String fileName : Objects.requireNonNull(plainFilenamesIn(STAGED_DIR))) {
                if (fileName.equals(String.valueOf(file))) {
                    Utils.restrictedDelete(toStaged);
                    break;
                }
            }
        }
    }

    public static void remove(String file) {
        boolean removed = false;
        for (String name : Utils.plainFilenamesIn(STAGED_DIR)) {
            if (file.equals(name)) {
                File removing = Utils.join(STAGED_DIR, name);
                removing.delete();
                removed = true;
            }
        }
        Commit Master = getMaster().getCommit();
        for (String name : Master.getKeySets()) {
            if (file.equals(name)) {
                Master.getFilesMap().remove(name);
                removed = true;
            }
        }
        // if there is no such file to remove, throw error
        if (!removed) {
            throw error("No reason to remove the file.");
        } else {// else, add its name to the REMOVED file
            File remo = Utils.join(GITLET_DIR, "REMOVED");
            if (remo.exists()) {
                String str = Utils.readContentsAsString(remo) + "\n" + file;
                Utils.writeContents(remo, str);
            } else {
                Utils.writeContents(remo, file);
            }
        }
    }

    /** Iterate through all Commit in the order of time. */
    public static void log(HashSet<Commit> Refs) {
        while (!Refs.isEmpty()) {
            Commit latest = getLatest(Refs);
            String newStr = "===\ncommit " + shaCommit(latest) + "\n";
            if (latest.getMergeParent() != null) {
                newStr += "Merge: " + latest.getParent() + " " + latest.getMergeParent() + "\n";
            }
            newStr += "Date: " + latest.getTimeStamp().toString();
            newStr += "\n" + latest.getMessage() + "\n";
            System.out.println(newStr);
        }
    }

    private static Commit getLatest(HashSet<Commit> Refs) {
        Commit Latest = (Commit) Refs.toArray()[0];
        for (Commit commit : Refs) {
            if (commit.getTimeStamp().after(Latest.getTimeStamp())) {
                Latest = commit;
            }
        }
        Refs.remove(Latest);
        if (Latest.getParent() != null) {
            Refs.add(Latest.getCommitParent());
        }
        return Latest;
    }

    /** Iterate through all Commit without an order. */
    public static void globalLog() {
        for (String code : Utils.plainFilenamesIn(COMMITS_DIR)) {
            File name = Utils.join(COMMITS_DIR, code);
            Commit Master = Utils.readObject(name, Commit.class);
            String str ="===" + "\n" + "commit " + shaCommit(Master) + "\nDate: "
                    + Master.getTimeStamp().toString() + "\n" + Master.getMessage() + "\n";
            System.out.println(str);
        }
    }

    /** Return the ID of specific Commit with the specific Commit message. */
    public static void find(String msg) {
        boolean found = false;
        for (String code : Utils.plainFilenamesIn(COMMITS_DIR)) {
            File name = Utils.join(COMMITS_DIR, code);
            Commit Master = Utils.readObject(name, Commit.class);
            if (Objects.equals(msg, Master.getMessage())) {
                System.out.println(shaCommit(Master));
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    /** display the Branches, Staged files, Removed files,
     * Modifications Not Staged For Commit, Untracked Files of current Commit.
     */
    public static void status() {
        //Branches
        String newStr = "=== Branches ===" + "\n*" + getMaster().getMsg() + "\n";
        for (String pointers : Utils.plainFilenamesIn(REFS_DIR)) {
            if (!getMaster().getMsg().equals(pointers)) {
                File REFS = Utils.join(REFS_DIR, pointers);
                Pointer pointer = Utils.readObject(REFS, Pointer.class);
                if (pointer.isBranch()) {
                    newStr += pointer.getMsg() + "\n";
                }
            }
        }
        System.out.println(newStr);
        //Staged Files
        newStr = "=== Staged Files ===\n";
        for (String Staging : Utils.plainFilenamesIn(STAGED_DIR)) {
            newStr += Staging + "\n";
        }
        File remo = Utils.join(GITLET_DIR, "REMOVED");
        System.out.println(newStr);
        //Removed Files
        if (remo.exists()) {
            newStr = "=== Removed Files ===\n" + Utils.readContentsAsString(remo);
        } else {
            newStr = "=== Removed Files ===";
        }
        System.out.println(newStr + "\n");
        newStr = "=== Modifications Not Staged For Commit ===\n";
        System.out.println(newStr);
        System.out.println("=== Untracked Files ===\n");
    }

    /** First, search the REFS directory and find the corresponding branch,
     *  then replace HEAD file. */
    public static void checkoutBranch(String branchName) {
        if (branchName.equals(getMaster().getMsg())) {
            throw error("No need to checkout the current branch.");
        }
        boolean checked = false;
        for (String name : Utils.plainFilenamesIn(REFS_DIR)) {
            if (Objects.equals(name, branchName)) {
                File file = Utils.join(REFS_DIR, name);
                Pointer branch = Utils.readObject(file, Pointer.class);
                if (branch.isBranch()) {
                    File HEAD = Utils.join(GITLET_DIR, "HEAD");
                    Utils.writeObject(HEAD, branch);
                    checked = true;
                }
            }
        }
        if (!checked) {
            throw error("No such branch exists.");
        }
        // Clear all the tracked but not tracked by this branch file
        Commit head = getMasterByCommit();
        for (String name : Utils.plainFilenamesIn(CWD)) {
            File file = Utils.join(CWD, name);
            // if tracked
            if (isTracked(file)) {
                if (head.getKeySets().contains(name)) {
                    // replace
                    File replace = Utils.join(BLOBS_DIR, sha1(Utils.readContents(file)));
                    Utils.writeContents(file, Utils.readContents(replace));
                } else {//delete
                    Utils.restrictedDelete(file);
                }
            }
        }
    }

    /** Check a file is tracked and commited by and Commit. */
    private static boolean isTracked(File file) {
        String code = sha1(Utils.readContents(file));
        File blobFile = Utils.join(BLOBS_DIR, code);
        return blobFile.exists();
    }

    /** Replace a specific file in the CWD with the file in head Commit. */
    public static void checkoutReplace(String name) {
        Commit head = getMasterByCommit();
        String code = head.getFilesMap().get(name);
        if (code !=null) {
            File replacing = Utils.join(BLOBS_DIR, code);
            File replaced = Utils.join(CWD, name);
            Utils.writeContents(replaced, Utils.readContents(replacing));
        } else {
            throw error("File does not exist in that commit.");
        }
    }

    /** Replace a specific file in the CWD with the file in a specific Commit.
     * @param commit is the ID of a Commit,
     * @param name is the name of the file. */
    public static void checkoutSwitch(String commit, String name) {
        Commit head = getCommit(commit);
        String code = head.getFilesMap().get(name);
        if (code !=null) {
            File replacing = Utils.join(BLOBS_DIR, code);
            File replaced = Utils.join(CWD, name);
            Utils.writeContents(replaced, (Object) Utils.readContents(replacing));
        } else {
            throw error("File does not exist in that commit.");
        }
    }

    /** Add a pointer at the REFS directory*/
    public static void branch(String msg) {
        for (String name : Utils.plainFilenamesIn(REFS_DIR)) {
            if (name.equals(msg)) {
                throw error("A branch with that name already exists.");
            }
        }
        Pointer branch = new Pointer(msg, getMaster().getCommit());
        File newBranch = Utils.join(REFS_DIR, msg);
        Utils.writeObject(newBranch, branch);
    }

    /** Set the branch.isBranch false so that it can't be tracked by 'status' and 'checkout'. */
    public static void removeBranch(String msg) {
        if (msg.equals(getMaster().getMsg())) {
            throw error("Cannot remove the current branch.");
        }
        boolean isRemoved = false;
        for (String name : Utils.plainFilenamesIn(REFS_DIR)) {
            File Branch = Utils.join(REFS_DIR, name);
            Pointer branch = Utils.readObject(Branch, Pointer.class);
            if (branch.getMsg().equals(msg)) {
                branch.rmBranch();
                isRemoved = true;
            }
        }
        if (!isRemoved) {
            throw error("A branch with that name does not exist.");
        }
    }

    /** Checks out all the files tracked by the given commit.
     * Removes tracked files that are not present in that commit.
     * Also moves the current branch’s head to that commit node. */
    public static void reset(String code) {
        Commit head = getCommit(code);
        head.saveHead(head);
        for (String name : head.getKeySets()) {
            checkoutSwitch(code, name);
        }
        for (String name : Utils.plainFilenamesIn(CWD)) {
            if (!head.getFilesMap().containsKey(name)) {
                File remove = Utils.join(CWD, name);
                if (isTracked(remove)) {
                    Utils.restrictedDelete(remove);
                }
            }
        }
    }

    /** Return the SHA-1 code of a file, computed by the content of the file. */
    public static String SHA(File file) {
        return Utils.sha1(Utils.readContents(file));
    }

    /** Return the master head of current Commit. */
    public static Pointer getMaster() {
        File head = Utils.join(GITLET_DIR, "HEAD");
        if (head.exists()) {
            return Utils.readObject(head, Pointer.class);
        } else {
            return null;
        }
    }

    public static Commit getMasterByCommit() {
        File head = Utils.join(GITLET_DIR, "HEAD");
        if (head.exists()) {
            return Utils.readObject(head, Pointer.class).getCommit();
        } else {
            return null;
        }
    }

    public static Commit getCommit(String code) {
        if (code.length() == 40) {// The long(40) code
            File commit = Utils.join(COMMITS_DIR, code);
            return Utils.readObject(commit, Commit.class);
        } else {// The short(6) code
            for (String codes : Utils.plainFilenamesIn(COMMITS_DIR)) {
                if (codes.substring(0, 6).equals(code)) {
                    File commit = Utils.join(COMMITS_DIR, codes);
                    return Utils.readObject(commit, Commit.class);
                }
            }
        }
        throw error("No commit with that id exists.");
    }

}

package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 * @Timestamp: the time when the commit is made
 * @
 *  does at a high level.
 *
 *  @author Tim lin
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The Commit directory. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The staged directory. */
    public static final File STAGED_DIR = join(GITLET_DIR, "staged");
    /** The blobs' directory. */
    public static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    /** The heads of branches. */
    public static final File REFS_DIR = Utils.join(GITLET_DIR, "refs");

    /** The date of this commit. */
    private Date timeStamp;
    /** The message of this Commit. */
    private String message;
    /** A Map mapping name to its SHA-1 code of the file */
    private HashMap<String, String> filesMap;
    /** The parent commit of this commit */
    private String parent;
    /** The merge parent of this commit, usually null. */
    private String mergeParent = null;
    /** The SHA-1 code of this commit. */
    private transient String SHA;

    /** When initialized, input the current time.
     * Distinguish between common Commit and init Commit.
     * If it is a common Commit, copy its parent's filesMap*/
    public Commit(String message) {
        this.message = message;
        Commit parent = Repository.getMasterByCommit();
        // If commit has a parent, update its fileMap
        if (parent != null) {
            this.timeStamp = new Date();
            this.SHA = shaCommit(this);
            this.parent = shaCommit(parent);
            this.filesMap = parent.getFilesMap();
            // check the Staged file add to blob automatically and update the fileMap.
            if (Utils.plainFilenamesIn(STAGED_DIR).isEmpty()) {
                throw error("No changes added to the commit.");
            } else {
                for (String name : Objects.requireNonNull(Utils.plainFilenamesIn(STAGED_DIR))) {
                    //add it to the blob directory
                    File commitFile = Utils.join(STAGED_DIR, name);
                    String SHA = Repository.SHA(commitFile);
                    File blob = Utils.join(BLOBS_DIR, SHA);
                    writeContents(blob, Utils.readContents(commitFile));
                    //add it to the fileMap
                    filesMap.put(name, SHA);
                    //clear the staged directory
                    commitFile.delete();
                }
            }
        } else { //else set an init commit
            /* Set the time to 00:00:00 UTC, Thursday, 1 January 1970 when initialized. */
            this.timeStamp = new Date(0);
            this.SHA = shaCommit(this);
            this.parent = null;
            this.filesMap = new HashMap<>();
            Repository.setupPersistent();
        }
        File remo = Utils.join(GITLET_DIR, "REMOVED");
        remo.delete();
        //set the new head as the Master head
        saveHead(this);
        saveCommit(this);
        saveBranch(this);
    }


    /** When merged, add the second parent as its mergeParent. */
    public void setMergeParent(Commit mergeParent) {
        this.mergeParent = shaCommit(mergeParent);
    }

    public Commit getMergeParent() {
        if (mergeParent == null) {
            return null;
        } else {
            File merge = Utils.join(COMMITS_DIR, mergeParent);
            return Utils.readObject(merge, Commit.class);
        }
    }
    public String getMessage() {
        return message;
    }
    public HashMap<String, String> getFilesMap() {
        return this.filesMap;
    }
    public Date getTimeStamp() {
        return this.timeStamp;
    }
    public String getParent() {
        return parent;
    }
    public Collection<String> getKeySets() {
        return getFilesMap().keySet();
    }

    /** Return Parent by the form of Commit, if Commit has no parent, return null. */
    public Commit getCommitParent() {
        if (this.getParent() == null) {
            return null;
        }
        File Parent = Utils.join(COMMITS_DIR, this.getParent());
        return Utils.readObject(Parent, Commit.class);
    }

    /** Save the Commit in File HEAD. */
    public void saveHead(Commit commit) {
        File HEAD = Utils.join(GITLET_DIR, "HEAD");
        Pointer commitParent = Repository.getMaster();
        if (commitParent == null) {
            Pointer head = new Pointer("Master", commit);
            Utils.writeObject(HEAD, head);
        } else {
            Pointer head = new Pointer(commitParent.getMsg(), commit);
            Utils.writeObject(HEAD, head);
        }
    }

    /** Save the Commit in the Commit directory. */
    public void saveCommit(Commit commit) {
        File toCommit = Utils.join(COMMITS_DIR, shaCommit(commit));
        Utils.writeObject(toCommit, commit);
    }

    /** Check the head pointer for its msg first, and check if the msg file
     * exists in the REFS directory, if exists, overwrite it.
     * Otherwise, create a new one. */
    public void saveBranch(Commit commit) {
        Pointer parent = Repository.getMaster();
        File parentPointer = Utils.join(REFS_DIR, parent.getMsg());
        Pointer branch = new Pointer(parent.getMsg(), commit);
        Utils.writeObject(parentPointer, branch);
    }

    /** Compute a Commits SHA code by its timeStamp and Files. */
    public static String shaCommit(Commit commit) {
        String toSHA = commit.timeStamp.toString();
        if (commit.filesMap != null) {
            for (String code : commit.filesMap.values()) {
                toSHA += code;
            }
        }
        return Utils.sha1(toSHA);
    }
}

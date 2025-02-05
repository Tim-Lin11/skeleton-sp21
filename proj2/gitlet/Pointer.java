package gitlet;

import java.io.Serializable;
import java.io.File;

public class Pointer implements Serializable {
    /** represent a commit by its SHA-1 code. */
    private String Commit;
    private String msg;
    private boolean isBranch = true;

    public Pointer(String msg, Commit commit) {
        this.Commit = gitlet.Commit.shaCommit(commit);
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    /** Return the Commit pointer points at in the form of String. */
    public String getCommitbySTring() {
        return this.Commit;
    }

    /** Return the Commit pointer points at. */
    public Commit getCommit() {
        File commit = Utils.join(Repository.COMMITS_DIR, Commit);
        return Utils.readObject(commit, gitlet.Commit.class);
    }

    public boolean isBranch() {
        return isBranch;
    }

    public void rmBranch() {
        isBranch = false;
    }
}

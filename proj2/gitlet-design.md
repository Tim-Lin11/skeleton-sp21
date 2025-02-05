# Gitlet Design Document

**Name**: Tim Lin

## Prologue

This spec aims to describe a outline of the Project 2 Gitlet of CS61B.

Overall, the program should contain following commands.

- [init](https://sp21.datastructur.es/materials/proj/proj2/proj2#init)
- [add](https://sp21.datastructur.es/materials/proj/proj2/proj2#add)
- [commit](https://sp21.datastructur.es/materials/proj/proj2/proj2#commit)
- [rm](https://sp21.datastructur.es/materials/proj/proj2/proj2#rm)
- [log](https://sp21.datastructur.es/materials/proj/proj2/proj2#log)
- [global-log](https://sp21.datastructur.es/materials/proj/proj2/proj2#global-log)
- [find](https://sp21.datastructur.es/materials/proj/proj2/proj2#find)
- [status](https://sp21.datastructur.es/materials/proj/proj2/proj2#status)
- [checkout](https://sp21.datastructur.es/materials/proj/proj2/proj2#checkout)
- [branch](https://sp21.datastructur.es/materials/proj/proj2/proj2#branch)
- [rm-branch](https://sp21.datastructur.es/materials/proj/proj2/proj2#rm-branch)
- [reset](https://sp21.datastructur.es/materials/proj/proj2/proj2#reset)
- [merge](https://sp21.datastructur.es/materials/proj/proj2/proj2#merge)

So it mimic what git can do, but simplify in some extend.

- Merge can only have two parents
- Our metadata only consist of timestamp and log message
- Incorporating trees into commits and not dealing with subdirectories

## Classes and Data Structures

<img src="https://sp21.datastructur.es/materials/proj/proj2/image/commits-and-blobs.png" alt="Two commits and their blobs" style="zoom:150%;" />

### Commit(Implement serialization)

Contains time, file, parent and the commit information. Also have a map of name to the SHA-1 code of its file.

The constructor function automatedly input its parent, message and timestamp

---

`java gitlet.Main checkout [commit id] -- [file name]`

1. Go to the map and get the pointer point at the commit
2. at the commit, use the file name to search find its SHA-1 code
3. go to the blob and use SHA-1 code to replace the current file.

---

#### **commit tree<Commit>**

Is a Linked list of <Commit> at most of the time, when branched, can use variable like mergeParent.

Use SHA-1 code to point at its parents, need a map passed in from Main to return a Commit. Which is also the case in referring to blob/file.

#### Fields

1. parent/ mergeParent
2. list of File
3. message
4. Timestamp

### **Blob**

Use what caper did to store file. Maybe use its SHA-1 code to name it?

#### Fields

1. Its SHA-1 code

### Map? It seems that map can be ignored?

A running map mapping a Commit SHA-1 code to Commit in the Main program.

Also run a map mapping the SHA-1 code to file.

- but how to 

## Algorithms

### Overall

Like what in the caper lab, call helper function in the main function.

- Have a list of Commit which represent the last commit of branch or master
- Have a pointer point at the Master

### Procedure

1. Init, initialize a LinkedList.
2. Add a file, add to the staged(Maybe a string list)
3. If commit, clear all the string in the stage file and add it to the mapping<SHA-name> and add it to the commit file.
4. If branch, use commit variable to tell if it is a branch or not, like a number to tell which number it is in the list.
5. If merge, commit will have two 

 Think about what data structure can implement the _commit tree_



## Persistence

```Tex
/.gitlet
	|
	|--blobs/ #should hold the file, but I don't understand how it works yet
	|
	|--commits/ #contains the chain of commit, each commit will be named by its SHA-1 code
	|--staged/ #Have files named by its filename
	|
	|--HEAD #current head pointer
	|--refs/heads #store the heads of the branches.
```



## 写在后面

1. **一定要通读完设计文档，明确好数据类型后再开工**，否则后来意识到数据类型不行的时候再重构便需要更多时间来修改
2. 先写函数描述再开始写


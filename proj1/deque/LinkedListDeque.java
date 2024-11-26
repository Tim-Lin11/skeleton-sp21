package deque;

public class LinkedListDeque<T>{
    private int size;
    private final Node Sentinel= new Node(null);
    Node recursive_node = Sentinel;

    public class Node{
        public T data;
        public Node prev;
        public Node next;

        public Node(T data){
            this.data=data;
            this.next = null;
            this.prev=null;
        }
    }

    public LinkedListDeque() {
        Sentinel.next = Sentinel;
        Sentinel.prev = Sentinel;
        this.size = 0;
    }


    public void Add_next(Node orig, Node added){
        added.next=orig.next;
        orig.next.prev=added;
        orig.next=added;
        added.prev=orig;
    }

    public void Add_prev(Node added, Node orig ){
        added.prev=orig.prev;
        orig.prev.next=added;
        orig.prev=added;
        added.next=orig;
    }

    public void addFirst(T item){
        Node added_node = new Node(item);
        Add_next(Sentinel,added_node);
        size++;
    }

    public void addLast(T item){
        Node added_node = new Node(item);
        Add_prev(added_node,Sentinel);
        size++;
    }

    public int size() {
        return size;
    }

    public void printDeque(){
        for (Node i=Sentinel.next;i.next!=Sentinel;i=i.next){
            System.out.print(i.data+" ");
        }
        System.out.print("\n");
    }

    /** return the first node and return its data*/
    public T removeFirst(){
        if (size!=0) {
            Node del=Sentinel.next;
            Sentinel.next=Sentinel.next.next;
            Sentinel.next.prev=Sentinel;
            del.next=null;
            del.prev=null;
            size--;
            return del.data;
        } else {
            return null;
        }
    }

    /** remove the last node and return its data if existed, null if isn't*/
    public T removeLast(){
        if (size!=0){
            Node del = Sentinel.prev;
            Sentinel.prev=Sentinel.prev.prev;
            Sentinel.prev.next=Sentinel;
            del.next=null;
            del.prev=null;
            size--;
            return del.data;
        } else {
            return null;
        }
    }

    public T get(int index){
        if (index>size-1){
            return null;
        } else {
            int iterate_Index = 0;
            Node current_node= Sentinel.next;
            while(iterate_Index!=index){
                current_node=current_node.next;
                iterate_Index++;
            }
            return current_node.data;
        }
    }

    public T getRecursive(int index){
        if (index>size-1){
            return null;
        } else if (index==0){
            T re = recursive_node.data;
            recursive_node=Sentinel;
            return re;
        } else {
            recursive_node=recursive_node.next;
            return getRecursive(index-1);
        }
    }
}
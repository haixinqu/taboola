
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.swing.tree.TreeNode;
import java.util.*;

public class MyTreeSerializer<T> implements TreeSerializer{
    public static void main(String[] args) {
        //sample test
        Node<Integer> root = new Node(1);
        Node<Integer>  root2 = new Node(2);
        Node<Integer>  root3 = new Node(3);
        Node<Integer>  root4 = new Node(4);
        root.left = root2;
        root2.left = root3;
        root.right = root4;
        MyTreeSerializer<Integer> myTreeSerializer = new MyTreeSerializer();
        System.out.println(myTreeSerializer.serialize(root));
        System.out.println(myTreeSerializer.deserialize(myTreeSerializer.serialize(root)));

        Node<String> roots = new Node("dd");
        Node<String>  root2s = new Node("ee");
        Node<String>  root3s = new Node("ff");
        Node<String>  root4s = new Node("gg");
        roots.left = root2s;
        root2s.left = root3s;
        roots.right = root4s;
        MyTreeSerializer<String> myTreeSerializer2 = new MyTreeSerializer();
        System.out.println(myTreeSerializer2.serialize(roots));
        System.out.println(myTreeSerializer2.deserialize(myTreeSerializer2.serialize(roots)));
        Node<Student> roott = new Node(new Student("zhansan2", 131));
        Node<Student>  root2t = new Node(new Student("zhansan3", 113));
        Node<Student>  root3t = new Node(new Student("zhansan4", 133));
        Node<Student>  root4t = new Node(new Student("zhansan5", 123));
        roott.left = root2t;
        root2t.left = root3t;
        roott.right = root4t;
        MyTreeSerializer<Student> myTreeSerializer3 = new MyTreeSerializer();
        System.out.println(myTreeSerializer3.serialize(roott));
        System.out.println(myTreeSerializer3.deserialize(myTreeSerializer3.serialize(roott)));
        myTreeSerializer3.inorder(roott);
    }

    void inorder(Node<T> root) {
        if (root ==null) return;
        System.out.println(root.num);
        inorder(root.left);
        inorder(root.right);
    }
    Map<Node, Boolean> mp;
    public MyTreeSerializer() {
        mp = new HashMap<>();
    }
    public String serialize(Node root) {
        mp.clear();
        return dfs(root);
    }

    String dfs(Node<T> root) {
        if (root == null) return "None,";
        if (mp.containsKey(root)) throw new RuntimeException("cycli tree");
        mp.put(root, true);
        String str = root.num.hashCode() + ",";
        str += dfs(root.left);
        str += dfs(root.right);
        return str;
    }
    public Node deserialize(String data) {
        List<String> datas = new LinkedList<String>(Arrays.asList(data.split(",")));
        return dedfs(datas);
    }
    Node dedfs(List<String> datas) {
        if (datas.get(0).equals("None")) {
            datas.remove(0);
            return null;
        }
        Node node = new Node();
        //将json字符串转化为对应JaveBean泛型
        String json = "{val:'"+ datas.get(0) +"'}";
        JavaBean<T> result = JSON.parseObject(json, new TypeReference<JavaBean<T>>() {});
        node.num = result.val;
        datas.remove(0);
        node.left = dedfs(datas);
        node.right = dedfs(datas);
        return node;
    }
}
interface TreeSerializer {
    String serialize(Node root);
    Node deserialize(String str);
}

class Node<T> {
    Node left, right;
    T num;
    public Node( ) {
    }
    public Node(T num) {
        this.num = num;
    }
}
class JavaBean<T>{
    public T val;
}


Sample test
class Student {
    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
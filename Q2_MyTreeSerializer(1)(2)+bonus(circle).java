import java.util.*;

public class MyTreeSerializer2 implements TreeSerializer{
    public static void main(String[] args) {
        Node root = new Node(1);
        Node  root2 = new Node(2);
        Node   root3 = new Node(3);
        Node   root4 = new Node(4);
        root.left = root2;
        root2.left = root3;
        root.right = root4;
        root4.left = root2;
        root4.right = root3;
        MyTreeSerializer2 myTreeSerializer = new MyTreeSerializer2();
        System.out.println(myTreeSerializer.serialize(root));
//        System.out.println(myTreeSerializer.deserialize(myTreeSerializer.serialize(root)));
        Node newRoot = myTreeSerializer.deserialize(myTreeSerializer.serialize(root));
        System.out.println(newRoot.right.left.num);
        System.out.println(newRoot.right.right.num);
    }

    Map<Node, Integer> mp;
    Map<Integer, Node> cnt;
    int cnt_no;
    public MyTreeSerializer2() {
        mp = new HashMap<>();
        cnt = new HashMap<>();
    }
    public String serialize(Node root) {
        cnt_no = 0;
        mp.clear();
        return dfs(root);
    }

    String dfs(Node  root) {
        if (root == null) return "None,";
        if (mp.containsKey(root)) {
            //throw new RuntimeException("cycli tree");
            return "*" + mp.get(root) + "*,";
        }
        mp.put(root, ++cnt_no);
        cnt.put(cnt_no, root);
        String str = root.num + ",";
        str += dfs(root.left);
        str += dfs(root.right);
        return str;
    }
    public Node deserialize(String data) {
        cnt_no = 0;
        cnt.clear();
        List<String> datas = new LinkedList<String>(Arrays.asList(data.split(",")));
        return dedfs(datas);
    }
    Node dedfs(List<String> datas) {
        if (datas.get(0).equals("None")) {
            datas.remove(0);
            return null;
        }
        if (datas.get(0).startsWith("*") && datas.get(0).endsWith("*")){
            String str =  datas.get(0).replace("*", "");
            datas.remove(0);
            return cnt.get(Integer.valueOf(str));
        }
        Node node = new Node();
        //将json字符串转化为对应JaveBean泛型
        node.num = Integer.valueOf(datas.get(0));
        cnt.put(++cnt_no, node);
        datas.remove(0);
        node.left = dedfs(datas);
        node.right = dedfs(datas);
        return node;
    }
}
class Node {
    Node left, right;
    int num;
    public Node( ) {
    }
    public Node(int num) {
        this.num = num;
    }
}
interface TreeSerializer {
    String serialize(Node root);
    Node deserialize(String str);
}
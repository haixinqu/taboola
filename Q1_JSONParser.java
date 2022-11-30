import java.util.HashMap;
import java.util.Map;

public class JSONParser {
    public static void main(String[] args) {
        String json = "{\"debug\": \"on\", \"window\":{\"title\":\"sample\", \"size\":500}}";
        Map<String, Object> mp = parse(json);
        System.out.println(((Map<String, Object>)mp.get("window")).get("title").equals("sample"));
        System.out.println(((Map<String, Object>)mp.get("window")).get("size").equals(500));
    }

    public static Map<String, Object> parse(String json) {
        json = json.replace(" ", "");
        Map<String, Object> map = new HashMap<String, Object>();
        int idx_key = 1, n = json.length();
        while (idx_key < n - 1) {
            String key = "";
            while (json.charAt(idx_key) != ':') {
                key += json.charAt(idx_key);
                idx_key++;
            }
            key = key.replace("\"", "");
            idx_key++;
            Object val = null;
            if (json.charAt(idx_key) == '"') {
                int end = json.indexOf(",", idx_key);
                if (end == -1) end = n;
                val = json.substring(idx_key, end).replace("\"", "");
                idx_key = end + 1;
            } else if (json.charAt(idx_key) == '{') {
                int end = json.indexOf("}", idx_key);
                val = parse(json.substring(idx_key, end + 1));
                idx_key = end + 2;
            } else {
                //数字
                int end = json.indexOf(",", idx_key);
                if (end == -1) end = n - 1;
                val = Integer.valueOf(json.substring(idx_key, end));
                idx_key = end + 1;
            }
            map.put(key, val);
        }
        return map;
    }
}

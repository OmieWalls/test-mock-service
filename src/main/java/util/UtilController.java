package util;

import java.util.Map;

public class UtilController {

    public static String linkedHashMapToJSON(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> property: map.entrySet()) {
            sb.append("\"" + property.getKey() + "\": \"" + property.getValue() + "\",");
        }
        sb.append("}");
        return sb.toString().replace(",}", "}");

    }
}

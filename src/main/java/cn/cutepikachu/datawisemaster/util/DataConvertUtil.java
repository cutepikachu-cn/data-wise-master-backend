package cn.cutepikachu.datawisemaster.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据格式转换工具
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class DataConvertUtil {

    public static List<Map<String, String>> convert(String data) {
        List<Map<String, String>> dataList = new ArrayList<>();
        List<String> row = StrUtil.split(data, '\n');
        List<String> head = StrUtil.split(row.get(0), ',');
        for (int i = 1; i < row.size(); i++) {
            List<String> col = StrUtil.split(row.get(i), ',');
            Map<String, String> map = MapUtil.newHashMap();
            for (int j = 0; j < col.size(); j++) {
                map.put(head.get(j), col.get(j));
            }
            dataList.add(map);
        }
        return dataList;
    }
}

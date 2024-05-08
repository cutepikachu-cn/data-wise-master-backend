package cn.cutepikachu.datawisemaster.util;

import cn.hutool.core.util.StrUtil;

/**
 * SQL 工具
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class SqlUtil {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return 是否合法
     */
    public static boolean validSortField(String sortField) {
        if (StrUtil.isBlank(sortField)) {
            return false;
        }
        return !StrUtil.containsAny(sortField, "=", "(", ")", " ");
    }

}

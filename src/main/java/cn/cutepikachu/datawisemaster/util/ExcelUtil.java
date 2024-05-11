package cn.cutepikachu.datawisemaster.util;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public class ExcelUtil {
    public static String excelToCsvString(MultipartFile file, ExcelTypeEnum type) {
        // File dataFile;
        // try {
        //     dataFile = ResourceUtils.getFile("classpath:data.xlsx");
        // } catch (FileNotFoundException e) {
        //     throw new RuntimeException(e);
        // }
        List<Map<Integer, String>> rows;
        try {
            rows = EasyExcel.read(file.getInputStream())
                    .excelType(type)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "文件读取失败");
        }
        // System.out.println(rows);
        if (CollectionUtil.isEmpty(rows)) {
            return "";
        }
        StringBuilder dataStr = new StringBuilder();
        // 表头
        Map<Integer, String> head = rows.get(0);
        String headRowStr = StrUtil.join(",", head.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
        dataStr.append(headRowStr).append('\n');
        // 数据
        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, String> row = rows.get(i);
            String rowStr = StrUtil.join(",", row.values().stream().filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
            dataStr.append(rowStr).append('\n');
        }
        // System.out.println(dataStr);
        return dataStr.toString();
    }

}

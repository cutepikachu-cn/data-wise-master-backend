package cn.cutepikachu.datawisemaster.mapper;

import cn.cutepikachu.datawisemaster.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 图表信息表 Mapper 接口
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
@Mapper
public interface ChartMapper extends BaseMapper<Chart> {
    void createChartDataTable(Long chartId, Set<String> fieldList);

    boolean insertChartData(Long chartId, List<Map<String, String>> dataList);

    @MapKey("")
    List<Map<String, String>> selectChartData(Long chartId);
}

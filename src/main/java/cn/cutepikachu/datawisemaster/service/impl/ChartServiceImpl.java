package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.mapper.ChartMapper;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.cutepikachu.datawisemaster.util.SqlUtils;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 图表信息表 服务实现类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements IChartService {

    @Override
    public LambdaQueryWrapper<Chart> getLambdaQueryWrapper(ChartQueryRequest chartQueryRequest) {
        LambdaQueryWrapper<Chart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (chartQueryRequest == null) {
            return lambdaQueryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();

        lambdaQueryWrapper.eq(ObjUtil.isNotEmpty(id), Chart::getId, id);
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(goal), Chart::getGoal, goal);
        lambdaQueryWrapper.eq(StrUtil.isNotEmpty(chartType), Chart::getChartType, chartType);

        if (SqlUtils.validSortField(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, Chart::getCreateTime);
                case "charttype" -> lambdaQueryWrapper.orderBy(true, isAsc, Chart::getChartType);
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public ChartVO getChartVO(Chart chart) {
        return chart.toVO(ChartVO.class);
    }

    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage) {
        List<Chart> chartList = chartPage.getRecords();
        Page<ChartVO> chartVOPage = new Page<>(chartPage.getCurrent(), chartPage.getSize(), chartPage.getTotal());
        if (chartList.isEmpty()) {
            return chartVOPage;
        }
        List<ChartVO> chartVOList = chartList.stream().map(chart -> chart.toVO(ChartVO.class)).toList();
        chartVOPage.setRecords(chartVOList);
        return chartVOPage;
    }
}

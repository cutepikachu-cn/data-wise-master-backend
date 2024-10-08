package cn.cutepikachu.datawisemaster.service.impl;

import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.mapper.ChartMapper;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.enums.SortOrder;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.cutepikachu.datawisemaster.util.DataConvertUtil;
import cn.cutepikachu.datawisemaster.util.SqlUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        String name = chartQueryRequest.getName();
        String chartType = chartQueryRequest.getChartType();
        String sortField = chartQueryRequest.getSortField();
        SortOrder sortOrder = chartQueryRequest.getSortOrder();

        lambdaQueryWrapper.eq(ObjUtil.isNotEmpty(id), Chart::getId, id);
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(goal), Chart::getGoal, goal);
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(name), Chart::getName, name);
        lambdaQueryWrapper.eq(StrUtil.isNotEmpty(chartType), Chart::getChartType, chartType);

        if (SqlUtil.validSortField(sortField)) {
            boolean isAsc = sortOrder == SortOrder.ASCENDING;
            switch (sortField.toLowerCase()) {
                case "createtime":
                    lambdaQueryWrapper.orderBy(true, isAsc, Chart::getCreateTime);
                    break;
                case "charttype":
                    lambdaQueryWrapper.orderBy(true, isAsc, Chart::getChartType);
                    break;
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public ChartVO getChartVO(Chart chart) {
        ChartVO chartVO = chart.toVO(ChartVO.class);
        List<Map<String, String>> data = baseMapper.selectChartData(chart.getId());
        chartVO.setData(data);
        return chartVO;
    }

    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage) {
        List<Chart> chartList = chartPage.getRecords();
        Page<ChartVO> chartVOPage = new Page<>(chartPage.getCurrent(), chartPage.getSize(), chartPage.getTotal());
        if (chartList.isEmpty()) {
            return chartVOPage;
        }
        List<ChartVO> chartVOList = chartList.stream().map(this::getChartVO).collect(Collectors.toList());
        chartVOPage.setRecords(chartVOList);
        return chartVOPage;
    }

    @Override
    public boolean saveChart(Chart chart, String data) {
        // 存储图表信息
        boolean saveSuccess = save(chart);
        ThrowUtil.throwIf(!saveSuccess, ResponseCode.OPERATION_ERROR);
        Long chartId = chart.getId();

        // 创建数据表
        List<Map<String, String>> dataList = DataConvertUtil.convert(data);
        baseMapper.createChartDataTable(chartId, dataList.get(0).keySet());

        // 存储数据
        boolean insertSuccess = baseMapper.insertChartData(chartId, dataList);
        ThrowUtil.throwIf(!insertSuccess, ResponseCode.OPERATION_ERROR);

        return true;
    }

}

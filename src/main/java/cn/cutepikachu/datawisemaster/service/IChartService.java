package cn.cutepikachu.datawisemaster.service;

import cn.cutepikachu.datawisemaster.model.dto.chart.ChartQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图表信息表 服务类
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
public interface IChartService extends IService<Chart> {

    /**
     * 获取查询条件
     *
     * @param chartQueryRequest
     * @return
     */
    LambdaQueryWrapper<Chart> getLambdaQueryWrapper(ChartQueryRequest chartQueryRequest);

    /**
     * 获取图表信息封装
     *
     * @param chart
     * @return
     */
    ChartVO getChartVO(Chart chart);

    /**
     * 分页获取图表信息封装
     *
     * @param chartPage
     * @return
     */
    Page<ChartVO> getChartVOPage(Page<Chart> chartPage);

    /**
     * 存储图表信息和数据
     *
     * @param chart
     * @param data
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean saveChart(Chart chart, String data);

}

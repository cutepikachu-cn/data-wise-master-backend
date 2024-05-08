package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.manager.AiManager;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartGenRequest;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ExcelUtil;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 图表信息表 前端控制器
 * </p>
 *
 * @author 笨蛋皮卡丘
 * @since 2024-05-06 17:54:04
 */
@Slf4j
@RestController
@RequestMapping("/chart")
public class ChartController {

    @Resource
    private IChartService chartService;

    @Resource
    private IUserService userService;

    @Resource
    private AiManager aiManager;

    /**
     * 生成图表请求
     *
     * @param dataFile
     * @param chartGenRequest
     */
    @PostMapping("/gen")
    public BaseResponse<ChartVO> genChart(@RequestPart MultipartFile dataFile,
                                    @Valid ChartGenRequest chartGenRequest) {
        Chart chart = new Chart();
        BeanUtil.copyProperties(chartGenRequest, chart);
        User loginUser = userService.getLoginUser();
        chart.setUserId(loginUser.getId());
        String data = ExcelUtil.excelToCSV(dataFile);
        chart.setData(data.trim());

        String goal = chartGenRequest.getGoal();
        String name = chartGenRequest.getName();
        String chartType = chartGenRequest.getChartType();
        // 拼接用户输入
        // {"goal":"分析网站用户增长趋势","name":"用户增长趋势图","chartType":"line"}
        String userInput = "分析需求: \n" + goal + '\n' +
                "图表名称为" + name + '\n' +
                "图表类型为" + chartType + '\n' +
                "原始数据: \n" + data;
        // AI 调用
        String aiGenResult = aiManager.doChat(userInput);
        List<String> splitResult = StrUtil.split(aiGenResult, "*****");
        ThrowUtil.throwIf(splitResult.size() < 2, ResponseCode.OPERATION_ERROR, "生成错误");
        String genChart = splitResult.get(1).trim();
        String genResult = splitResult.get(2).trim();

        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        boolean result = chartService.save(chart);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success(chart.toVO(ChartVO.class));
    }

    /**
     * 删除图表
     *
     * @param deleteRequest
     */
    @PostMapping("/delete")
    public BaseResponse<?> deleteChart(@RequestBody @Valid DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        Chart chart = chartService.getById(id);
        ThrowUtil.throwIf(chart == null, ResponseCode.PARAMS_ERROR, "图表不存在");
        User loginUser = userService.getLoginUser();
        ThrowUtil.throwIf(!chart.getUserId().equals(loginUser.getId()), ResponseCode.NO_AUTH_ERROR);
        boolean result = chartService.removeById(id);
        ThrowUtil.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResponseUtil.success("删除图表成功");
    }

    @GetMapping("/get/vo")
    public BaseResponse<ChartVO> getChartVOById(@RequestParam Long id) {
        Chart chart = chartService.getById(id);
        ThrowUtil.throwIf(chart == null, ResponseCode.PARAMS_ERROR, "图表不存在");
        User loginUser = userService.getLoginUser();
        ThrowUtil.throwIf(!chart.getUserId().equals(loginUser.getId()), ResponseCode.NO_AUTH_ERROR);
        ChartVO chartVO = chartService.getChartVO(chart);
        return ResponseUtil.success(chartVO);
    }

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Chart>> pageChart(@RequestBody @Valid ChartQueryRequest chartQueryRequest) {
        int current = chartQueryRequest.getCurrent();
        int pageSize = chartQueryRequest.getPageSize();
        LambdaQueryWrapper<Chart> lambdaQueryWrapper = chartService.getLambdaQueryWrapper(chartQueryRequest);
        Page<Chart> chartPage = chartService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResponseUtil.success(chartPage);
    }

    @PostMapping("/page/vo/self")
    public BaseResponse<Page<ChartVO>> pageSelfChartVO(@RequestBody @Valid ChartQueryRequest chartQueryRequest) {
        int current = chartQueryRequest.getCurrent();
        int pageSize = chartQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser();
        chartQueryRequest.setUserId(loginUser.getId());
        LambdaQueryWrapper<Chart> lambdaQueryWrapper = chartService.getLambdaQueryWrapper(chartQueryRequest);
        Page<Chart> chartPage = chartService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<ChartVO> chartVOPage = chartService.getChartVOPage(chartPage);
        return ResponseUtil.success(chartVOPage);
    }
}

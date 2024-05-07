package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartAddRequest;
import cn.cutepikachu.datawisemaster.model.dto.chart.ChartQueryRequest;
import cn.cutepikachu.datawisemaster.model.entity.Chart;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.UserRole;
import cn.cutepikachu.datawisemaster.model.vo.ChartVO;
import cn.cutepikachu.datawisemaster.service.IChartService;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ExcelUtils;
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private Validator validator;

    /**
     * 生成图表请求
     *
     * @param dataFile
     * @param demandInfo
     * @param request
     * @return
     */
    @PostMapping("/gen")
    public BaseResponse<?> genChart(@RequestPart MultipartFile dataFile, String demandInfo, HttpServletRequest request) {
        // 参数校验
        ChartAddRequest chartAddRequest = JSONUtil.toBean(demandInfo, ChartAddRequest.class);
        validator.validate(chartAddRequest);
        String data = ExcelUtils.excelToCSV(dataFile);
        String goal = chartAddRequest.getGoal();
        String name = chartAddRequest.getName();
        String chartType = chartAddRequest.getChartType();


        // 用户输入
        StringBuilder userInput = new StringBuilder();
        // {"goal":"分析网站用户增长趋势","name":"用户增长趋势表","chartType":"line"}
        userInput.append("你是一个数据分析师，根据我提供的的分析目标和原始数据，输出分析结论\n");
        userInput.append("分析目标: ").append(goal).append('\n');
        userInput.append("图表名称: ").append(name).append('\n');
        userInput.append("图表类型: ").append(chartType).append('\n');
        userInput.append(data);

        Chart chart = new Chart();
        chart.setData(data);
        BeanUtil.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        // boolean result = chartService.save(chart);
        // ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success("添加图表成功", userInput);
    }

    /**
     * 删除图表
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<?> deleteChart(@RequestBody @Valid DeleteRequest deleteRequest, HttpServletRequest request) {
        Long id = deleteRequest.getId();
        Chart chart = chartService.getById(id);
        ThrowUtils.throwIf(chart == null, ResponseCode.PARAMS_ERROR, "图表不存在");
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!chart.getUserId().equals(loginUser.getId()), ResponseCode.NO_AUTH_ERROR);
        boolean result = chartService.removeById(id);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success("删除图表成功");
    }

    @GetMapping("/get/vo")
    public BaseResponse<ChartVO> getChartVOById(@RequestParam Long id, HttpServletRequest request) {
        Chart chart = chartService.getById(id);
        ThrowUtils.throwIf(chart == null, ResponseCode.PARAMS_ERROR, "图表不存在");
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(!chart.getUserId().equals(loginUser.getId()), ResponseCode.NO_AUTH_ERROR);
        ChartVO chartVO = chartService.getChartVO(chart);
        return ResultUtils.success(chartVO);
    }

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Chart>> pageChart(@RequestBody @Valid ChartQueryRequest chartQueryRequest) {
        int current = chartQueryRequest.getCurrent();
        int pageSize = chartQueryRequest.getPageSize();
        LambdaQueryWrapper<Chart> lambdaQueryWrapper = chartService.getLambdaQueryWrapper(chartQueryRequest);
        Page<Chart> chartPage = chartService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(chartPage);
    }

    @PostMapping("/page/vo/self")
    public BaseResponse<Page<ChartVO>> pageSelfChartVO(@RequestBody @Valid ChartQueryRequest chartQueryRequest, HttpServletRequest request) {
        int current = chartQueryRequest.getCurrent();
        int pageSize = chartQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        LambdaQueryWrapper<Chart> lambdaQueryWrapper = chartService.getLambdaQueryWrapper(chartQueryRequest);
        Page<Chart> chartPage = chartService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<ChartVO> chartVOPage = chartService.getChartVOPage(chartPage);
        return ResultUtils.success(chartVOPage);
    }
}

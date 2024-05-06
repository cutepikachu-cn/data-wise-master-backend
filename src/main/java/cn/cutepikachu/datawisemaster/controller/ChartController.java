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
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResultUtils;
import cn.cutepikachu.datawisemaster.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import cn.cutepikachu.datawisemaster.service.IChartService;

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

    /**
     * 添加图表
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<?> addChart(@RequestBody @Valid ChartAddRequest chartAddRequest, HttpServletRequest request) {
        Chart chart = new Chart();
        BeanUtil.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        return ResultUtils.success("添加图表成功");
    }

    /**
     * 删除图表
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

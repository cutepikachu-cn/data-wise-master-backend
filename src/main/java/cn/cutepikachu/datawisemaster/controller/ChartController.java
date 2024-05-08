package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

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
     * 生成图表请求
     *
     * @param dataFile
     * @param chartGenRequest
     */
    @PostMapping("/gen")
    public BaseResponse<?> genChart(@RequestPart MultipartFile dataFile,
                                    @Valid ChartGenRequest chartGenRequest) {
        // 参数校验
        String data = ExcelUtil.excelToCSV(dataFile);
        String goal = chartGenRequest.getGoal();
        String name = chartGenRequest.getName();
        String chartType = chartGenRequest.getChartType();

        // 用户输入
        StringBuilder userInput = new StringBuilder();
        // {"goal":"分析网站用户增长趋势","name":"用户增长趋势表","chartType":"line"}
        userInput.append("你是一个数据分析师和前端开发专家，根据我提供的的分析目标和原始数据，输出分析图表和结论\n");
        userInput.append("分析需求: \n").append(goal).append('\n');
        // userInput.append("图表名称: \n").append(name).append('\n');
        // userInput.append("图表类型: \n").append(chartType).append('\n');
        userInput.append("原始数据: \n").append(data);
        userInput
                .append("请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n")
                .append("【【【【【\n")
                .append("{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\n")
                .append("【【【【【\n")
                .append("{明确的数据分析结论、越详细越好，不要生成多余的注释}");

        Chart chart = new Chart();
        chart.setData(data);
        BeanUtil.copyProperties(chartGenRequest, chart);
        User loginUser = userService.getLoginUser();
        chart.setUserId(loginUser.getId());
        // boolean result = chartService.save(chart);
        // ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR);
        // System.out.println(userInput);
        return ResponseUtil.success("添加图表成功", userInput);
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

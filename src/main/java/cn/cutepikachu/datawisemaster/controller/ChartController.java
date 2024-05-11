package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.annotation.AuthCheck;
import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.DeleteRequest;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
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
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private RedissonClient redissonClient;

    private static final Map<String, ExcelTypeEnum> VALID_SUFFIX_MAP;

    static {
        VALID_SUFFIX_MAP = new HashMap<>();
        VALID_SUFFIX_MAP.put("xls", ExcelTypeEnum.XLS);
        VALID_SUFFIX_MAP.put("xlsx", ExcelTypeEnum.XLSX);
        VALID_SUFFIX_MAP.put("csv", ExcelTypeEnum.CSV);
    }

    /**
     * 生成图表请求
     *
     * @param dataFile
     * @param chartGenRequest
     */
    @PostMapping("/gen")
    public BaseResponse<?> genChart(@RequestPart MultipartFile dataFile,
                                    @Valid ChartGenRequest chartGenRequest) {
        User loginUser = userService.getLoginUser();
        RLock lock = redissonClient.getLock("chart:gen" + loginUser.getId());
        // 30s 内最多生成一次
        try {
            if (!lock.tryLock(-1, 30, TimeUnit.SECONDS)) {
                return ResponseUtil.error(ResponseCode.OPERATION_ERROR, "操作过于频繁");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 文件类型（后缀）校验
        String originalFilename = dataFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        String suffix = FileUtil.getSuffix(originalFilename).toLowerCase();
        ThrowUtil.throwIf(!VALID_SUFFIX_MAP.containsKey(suffix), ResponseCode.PARAMS_ERROR, "非法文件类型");

        // 文件大小限制
        final long MAX_SIZE = 1024 * 1024 * 5L;
        long size = dataFile.getSize();
        ThrowUtil.throwIf(size > MAX_SIZE, ResponseCode.PARAMS_ERROR, "文件大小超过限制");

        Chart chart = new Chart();
        BeanUtil.copyProperties(chartGenRequest, chart);
        chart.setUserId(loginUser.getId());
        ExcelTypeEnum type = VALID_SUFFIX_MAP.get(suffix);
        String data = ExcelUtil.excelToCsvString(dataFile, type);
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

package cn.cutepikachu.datawisemaster.controller;

import cn.cutepikachu.datawisemaster.common.BaseResponse;
import cn.cutepikachu.datawisemaster.common.ResponseCode;
import cn.cutepikachu.datawisemaster.constant.FileConstant;
import cn.cutepikachu.datawisemaster.exception.BusinessException;
import cn.cutepikachu.datawisemaster.manager.CosManager;
import cn.cutepikachu.datawisemaster.model.dto.file.UploadFileRequest;
import cn.cutepikachu.datawisemaster.model.entity.User;
import cn.cutepikachu.datawisemaster.model.enums.FileUploadBizEnum;
import cn.cutepikachu.datawisemaster.service.IUserService;
import cn.cutepikachu.datawisemaster.util.ResponseUtil;
import cn.cutepikachu.datawisemaster.util.ThrowUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.util.Arrays;

/**
 * 文件 前端控制器
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IUserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     *
     * @param multipartFile     文件
     * @param uploadFileRequest 文件上传参数
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           @Valid UploadFileRequest uploadFileRequest) {
        FileUploadBizEnum biz = uploadFileRequest.getBiz();
        ThrowUtil.throwIf(biz == null, new BusinessException(ResponseCode.PARAMS_ERROR));
        validFile(multipartFile, biz);
        User loginUser = userService.getLoginUser();
        // 文件目录：根据业务、用户来划分
        String uuid = RandomUtil.randomString(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format(FileConstant.PATH_FORMAT, biz.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResponseUtil.success("上传成功", FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile     文件
     * @param fileUploadBizEnum 文件上传业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            ThrowUtil.throwIf(fileSize > ONE_M, new BusinessException(ResponseCode.PARAMS_ERROR, "文件大小不能超过 1M"));
            ThrowUtil.throwIf(!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix), new BusinessException(ResponseCode.PARAMS_ERROR, "文件类型错误"));
        }
    }

}

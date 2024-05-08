package cn.cutepikachu.datawisemaster.manager;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AI 调用管理
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Component
public class AiManager {
    @Resource
    private YuCongMingClient yuCongMingClient;

    public String doChat(String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1788176895188783105L);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        return response.getData() != null ? response.getData().getContent() : null;
    }
}

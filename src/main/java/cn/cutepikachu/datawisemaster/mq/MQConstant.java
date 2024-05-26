package cn.cutepikachu.datawisemaster.mq;

/**
 * 消息队列常量
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-05-26 18:28:00
 */
public interface MQConstant {
    String ANALYSIS_EXCHANGE_NAME = "analysis_exchange";
    String ANALYSIS_ROUTING_KEY = "analysis_routing_key";
    String ANALYSIS_QUEUE_NAME = "analysis_queue";

    String ANALYSIS_DLX_EXCHANGE_NAME = "analysis_dlx_exchange";
    String ANALYSIS_DLX_ROUTING_KEY = "analysis_dlx_routing_key";
    String ANALYSIS_DLX_QUEUE_NAME = "analysis_dlx_queue";
}

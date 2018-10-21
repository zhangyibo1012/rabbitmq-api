package cn.zyblogs.rabbitmq.api.exchange.topic;

/**
 * @Title: Producer4TopicExchange.java
 * @Package cn.zyblogs.rabbitmq.api.exchange.topic
 * @Description: TODO 所有发送到Topic Exchange 的消息被转发都所有关心RouteKey中指定的Topic的Queue上
 *                  Exchange将RouteKey和某Topic进行模糊匹配，此时队列需要绑定一个Topic ，可以使用通配符匹配 #匹配一个或多个词  *匹配一个词
 *                          log.# 可匹配到log.info.oa
 *                          log.* 只可匹配log.error
 * @Author ZhangYB
 * @Version V1.0
 */
public class Producer4TopicExchange {
}

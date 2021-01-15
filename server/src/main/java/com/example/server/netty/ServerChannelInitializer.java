package com.example.server.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

import static com.example.common.contants.Constants.DELIMITER;


/**
 * @author zhangxiaoyu
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private MessageProcessorHandler messageProcessorHandler;

    @Override
    protected void initChannel(SocketChannel channel) {

        final ChannelPipeline pipeline = channel.pipeline();
        // Netty连接日志，默认DEBUG级别，供开发测试阶段使用
        pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("delimiterDecoder",new DelimiterBasedFrameDecoder(1024 * 1024,
                Unpooled.wrappedBuffer(DELIMITER.getBytes(StandardCharsets.UTF_8))));
//        pipeline.addLast("frameLengthDecoder",)
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(this.messageProcessorHandler);
    }

}

package com.example.server.netty;

import com.example.server.config.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author zhangxiaoyu
 */
@Slf4j
@Configuration
public class NettyServer implements ApplicationListener<StartNettyServerEvent> {

    @Resource
    private ServerChannelInitializer serverChannelInitializer;

    @Resource
    private NettyServerProperties serverProperties;


    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;



    @PostConstruct
    public void init(){
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
    }

    @PreDestroy
    public void shutdown(){
        bossGroup.shutdownGracefully().addListener(future -> {
           if(future.isSuccess()){
               log.info("netty boos_group 关闭成功");
           }else {
               log.info("netty boos_group 关闭失败");
           }
        });
        workGroup.shutdownGracefully().addListener(future -> {
            if(future.isSuccess()){
                log.info("netty works_group 关闭成功");
            }else {
                log.info("netty works_group 关闭失败");
            }
        });
    }


    public void start() {
        workGroup = new NioEventLoopGroup();
        bossGroup = new NioEventLoopGroup();
        try {
            //一个对服务端做配置和启动的类
            //类似构造器
            ServerBootstrap bootstrap = new ServerBootstrap()
                    //组的概念
                    .group(bossGroup, workGroup)
                    //NIO类的SocketServer通道
                    .channel(NioServerSocketChannel.class)
                    //拦截器实例化
                    .childHandler(this.serverChannelInitializer)
                    //父通道日志配置
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                    //子通道配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    ;
            //绑定端口，开始接收进来的连接
            ChannelFuture channelFuture = bootstrap.bind(this
            .serverProperties.getPort()).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
                            + "Netty Server成功启动："
                            + serverProperties.getAddress()
                            + ":" + serverProperties.getPort()
                            + "\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"
                    );
                } else {
                    log.error("Netty Server启动失败。");
                }
            });
            log.info("服务已启动，端口为" + serverProperties.getPort());
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void onApplicationEvent(StartNettyServerEvent event) {
        this.start();
    }
}

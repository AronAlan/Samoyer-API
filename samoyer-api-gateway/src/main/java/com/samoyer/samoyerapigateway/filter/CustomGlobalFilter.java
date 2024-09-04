package com.samoyer.samoyerapigateway.filter;

import com.samoyer.samoyerapiclientsdk.utils.SignUtils;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import com.samoyer.samoyerapicommon.model.entity.User;
import com.samoyer.samoyerapicommon.service.InnerInterfaceInfoService;
import com.samoyer.samoyerapicommon.service.InnerUserInterfaceInfoService;
import com.samoyer.samoyerapicommon.service.InnerUserService;
import com.samoyer.samoyerapigateway.enums.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 网关全局过滤器
 *
 * @author: Samoyer
 * @date: 2024-08-27
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    //请求白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.1.0.1");

    //需要转发的接口的uri地址比如8123
    private static final String INTERFACE_HOST = "http://localhost:8123";

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("自定义全局过滤器");
        // 1.用户发送请求到API网关
        // 2.请求日志
        //从路由交换机中拿到request信息
        ServerHttpRequest request = exchange.getRequest();
        //从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String interfaceIdStr = headers.getFirst("interfaceId");
        Integer interfaceId = Integer.valueOf(Objects.requireNonNull(interfaceIdStr));

        //请求路径，方法
        //request.getPath().value()的值是没有uri的，/api/name/...
        //并且这里接收的request的uri是网关的端口8080，path是需要转发的接口的uri比如8123
        String path = INTERFACE_HOST + request.getPath().value();
//        path = removeFirstApi(path);
        String method = request.getMethod().toString();

        log.info("id:{}", request.getId());
        log.info("interfaceId:{}", interfaceId);
        log.info("路径：{}", path);
        log.info("方法：{}", method);
        log.info("参数：{}", request.getQueryParams());
        //请求来源host
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源host：{}", sourceAddress);
        log.info("请求来源地址：{}", request.getRemoteAddress());

        // 3.（黑白名单）
        ServerHttpResponse response = exchange.getResponse();
        if (IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }

        // 4.用户鉴权（判断ak,sk是否合法）

        //鉴权accessKey
        //使用Dubbo远程调用backend中的实现类，实现类中读取数据库获取accessKey的值
        User invokeUser = null;
        try {
            //调用内部服务，根据accessKey获取用户信息
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        //如果根据accessKey（库中唯一值）获取不到调用用户，则未给用户分配accessKey，也就未授权
        if (invokeUser == null) {
            return handleNoAuth(response);
        }

        //随机数nonce不得大于等于5位，因为在
        if (nonce == null || Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }

        //请求时间与当前时间差不得超过5分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        if (timestamp == null || ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES)) {
            return handleNoAuth(response);
        }

        //从获取到的用户中提取用户的secretKey
        String secretKey = invokeUser.getSecretKey();
        //使用secretKey对请求体进行签名
        String serverSign = SignUtils.generateSign(body, secretKey);
        //与请求中的sign对比，不一样的话就是未授权
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }

        // 5.请求的模拟接口是否存在？
        InterfaceInfo interfaceInfo = null;
        try {
            //根据id,尝试从内部接口信息服务获取接口信息
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(interfaceId);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        //如果使用backend中的getInterfaceInfo获取到的接口信息为空，则请求的接口不存在
        if (interfaceInfo == null) {
            return handleInvokeError(response);
        }

        //发送请求前需要确认用户是否还有可用的调用次数
        Integer leftNum = innerUserInterfaceInfoService.getLeftNum(interfaceInfo.getId(), invokeUser.getId());
        if (leftNum <= 0) {
            return handleLeftNumInsufficient(response);
        }

        //真实的第三方请求路径
        String url = interfaceInfo.getUrl();
        // 创建新的请求头
        HttpHeaders newHeaders = new HttpHeaders();
        //加入请求头
        newHeaders.addAll(headers);
        newHeaders.add("url", url);

        // 重写headers，转发请求到接口
        ServerHttpRequest newRequest = null;
        try {
            newRequest = request.mutate().uri(URI.create(path)).headers(httpHeaders -> httpHeaders.addAll(newHeaders)).build();
        } catch (Exception e) {
            throw new RuntimeException("请求发送异常",e);
        }
        // 创建新的 ServerWebExchange 对象
        ServerWebExchange newExchange = exchange.mutate()
                .request(newRequest)
                .build();

        // 789封装在handleResponse中
        // 7.响应日志
        // 8.调用成功，接口调用次数 +1
        // 9.调用失败，返回一个规范的错误码
        return handleResponse(newExchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 无权限 拒绝访问的响应
     *
     * @param response
     * @return
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 接口调用失败
     *
     * @param response
     * @return
     */
    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 接口调用次数不足
     *
     * @param response
     * @return
     */
    public Mono<Void> handleLeftNumInsufficient(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 返回字符串响应
        byte[] bytes = "无调用次数，status:403".getBytes();
        response.getHeaders().add("Content-Length", String.valueOf(bytes.length));
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");

        // 写入响应体
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes))).then();
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfo, long userId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            log.info("响应状态码: {}", statusCode);

            // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,就能根据响应结果做一些自己的处理
                    // 调用完转发的接口的才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("接口调用完毕，执行writeWith方法，处理响应体");
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 8.调用成功，接口调用次数 +1 invokeCount
                                try {
                                    //Dubbo
                                    //使用接口id和用户id调用backend中的方法.实现向数据库中+1的操作(leftNum=leftNum-1,totalNum=totalNum+1)
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfo, userId);
                                } catch (Exception e) {
                                    //远程调用invokeCount失败
                                    log.error("invokeCount error", e);
                                }

                                // 读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                //StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                //sb2.append(data);
                                //打印日志
                                log.info("接口响应结果: {}", data);
                                // 将处理后的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 9.调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            log.info("响应状态码非 200 OK，进行降级处理");
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("网关处理响应异常.\n" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * 删除path中前缀/api
     * @param input
     * @return
     */
    public String removeFirstApi(String input) {
        int index = input.indexOf("/api");
        if (index != -1) {
            return input.substring(0, index) + input.substring(index + 4);
        }
        return input;
    }
}

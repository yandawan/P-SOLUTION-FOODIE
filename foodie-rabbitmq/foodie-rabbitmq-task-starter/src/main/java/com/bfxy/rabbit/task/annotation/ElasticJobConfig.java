package com.bfxy.rabbit.task.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConfig {

    // elasticJob 的名称
    String name();

    // cron表达式
    String cron() default "";

     // 分片数量
    int shardingTotalCount() default 1;

    // 分片参数
    String shardingItemParameters() default "";

    // 参数
    String jobParameter() default "";

    // 开启失败转移
    boolean failover() default false;

    // 错过执行 重新执行
    boolean misfire() default true;

    // 描述
    String description() default "";

    // 是否覆盖
    boolean overwrite() default false;

    // 是否流式任务
    boolean streamingProcess() default false;

    // 脚本
    String scriptCommandLine() default "";

    // 是否开启监控
    boolean monitorExecution() default false;

    // 必填项 监控端口
    public int monitorPort() default -1;

    // 必填项 是否忽略时间差
    public int maxTimeDiffSeconds() default -1;

    // 必填项 分片策略
    public String jobShardingStrategyClass() default "";

    // must
    public int reconcileIntervalMinutes() default 10;

    // must
    public String eventTraceRdbDataSource() default "";

    // must
    public String listener() default "";

    // must
    public boolean disabled() default false;

    public String distributedListener() default "";

    // must
    public long startedTimeoutMilliseconds() default Long.MAX_VALUE;

    // must
    public long completedTimeoutMilliseconds() default Long.MAX_VALUE;

    public String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    public String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";

}

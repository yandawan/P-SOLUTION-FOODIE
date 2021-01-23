package com.bfxy.esjob.taskdemo;

import com.bfxy.rabbit.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

@Component
@ElasticJobConfig(
		    name = "com.bfxy.esjob.taskdemo.DemoJob2",
			cron = "0/5 * * * * ?",
			description = "测试定时任务",
			overwrite = true,
			shardingTotalCount = 5
		)
public class DemoJob2 implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		System.err.println("执行Test job2.");
	}
}

package com.bfxy.esjob.taskdemo;

import com.bfxy.rabbit.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

@Component
@ElasticJobConfig(
			name = "com.bfxy.esjob.taskdemo.DemoJob1",
			cron = "0/10 * * * * ?",
			description = "样例定时任务",
			overwrite = true,
			shardingTotalCount = 2
		)
public class DemoJob1 implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		System.err.println("执行Demo job1.");
	}

}

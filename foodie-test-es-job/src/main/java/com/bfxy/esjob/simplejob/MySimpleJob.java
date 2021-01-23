package com.bfxy.esjob.simplejob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.stereotype.Component;

@Component
public class MySimpleJob implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {

		System.err.println("---------	开始任务 MySimpleJob	---------");

		System.err.println(shardingContext.getJobName());
		System.err.println(shardingContext.getJobParameter());
		System.err.println(shardingContext.getShardingItem());
		System.err.println(shardingContext.getShardingParameter());
		System.err.println(shardingContext.getShardingTotalCount());
		System.err.println("当前线程 : ---------------" + Thread.currentThread().getName());

		System.err.println("----------结束任务------");
	}
}
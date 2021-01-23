package com.bfxy.esjob.simplejob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

/**
 * 任务执行监听
 */
public class SimpleJobListener implements ElasticJobListener {

	private static Logger LOGGER = LoggerFactory.getLogger(SimpleJobListener.class);
	
	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		LOGGER.info("-----------------执行任务之前：{}", JSON.toJSONString(shardingContexts));
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		LOGGER.info("-----------------执行任务之后：{}", JSON.toJSONString(shardingContexts));		
	}
}
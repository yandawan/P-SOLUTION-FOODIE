package com.bfxy.esjob.dataflow;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataflowJobConfig {
    
	@Autowired
    private ZookeeperRegistryCenter regCenter;
    
    @Autowired
    private JobEventConfiguration jobEventConfiguration;
    
    @Bean
    public DataflowJob dataflowJob() {
        return new SpringDataflowJob();
    }
    
    @Bean(initMethod = "init")
    public JobScheduler dataflowJobScheduler(
            final DataflowJob dataflowJob,
            @Value("${dataflowJob.cron}") final String cron,
            @Value("${dataflowJob.shardingTotalCount}") final int shardingTotalCount,
            @Value("${dataflowJob.shardingItemParameters}") final String shardingItemParameters)
    {
    	SpringJobScheduler springJobScheduler = new SpringJobScheduler(
    	        dataflowJob,
                regCenter,
                getLiteJobConfiguration(
                    dataflowJob.getClass(),
                    cron,
                    shardingTotalCount,
                    shardingItemParameters
                ),
                jobEventConfiguration);
    	return springJobScheduler;
    }
    
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends DataflowJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(
        		new DataflowJobConfiguration(
        		        JobCoreConfiguration.newBuilder(
        		                jobClass.getName(),
                                cron,
                                shardingTotalCount
                        )
        		.shardingItemParameters(
        		        shardingItemParameters).build(),
        		        jobClass.getCanonicalName(),
        		false))	//streamingProcess true 实时的抓去数据
        		.overwrite(false)
        		.build();
    }
}

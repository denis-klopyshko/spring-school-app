package com.example.config;

import com.example.sheduled.PopulateTestDataOneTimeJob;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;


@Configuration
public class SchedulerConfig {

    @Value("${spring.quartz.auto-startup}")
    private boolean quartzAutoStartupEnable;

    @Bean
    public JobDetailFactoryBean populateTestDataOneTimeJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PopulateTestDataOneTimeJob.class);
        factoryBean.setDurability(true);
        return factoryBean;

    }

    @Bean
    public SimpleTriggerFactoryBean populateTestDataOneTimeJobDetailTrigger(JobDetail populateTestDataOneTimeJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(populateTestDataOneTimeJobDetail);
        trigger.setRepeatCount(0);
        trigger.setRepeatInterval(1);
        return trigger;
    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job, DataSource quartzDataSource) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactory.setJobFactory(springBeanJobFactory());
        if (quartzAutoStartupEnable) {
            schedulerFactory.setJobDetails(job);
            schedulerFactory.setTriggers(trigger);
        }
        schedulerFactory.setDataSource(quartzDataSource);
        schedulerFactory.setAutoStartup(quartzAutoStartupEnable);
        return schedulerFactory;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory();
    }
}

package br.com.azi.handson.business;

import br.com.azi.handson.config.quartz.SpringBatchJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class SchedulerBO {

    private static final String FIXED_QUARTZ_JOBGROUP = "quartzGroup";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    public void schedule(String jobName, Date start, Map<String, ?> parameters) {
        JobDetail jobDetail = createJobDetail(jobName);
        Trigger trigger = createTrigger(jobName, start);
        setSpringBatchJobName(jobDetail, jobName);
        setParameters(jobDetail, parameters);
        schedule(jobDetail, trigger);
    }

    private JobDetail createJobDetail(String jobName) {
        return JobBuilder.newJob(SpringBatchJob.class)
                .withIdentity(createJobKey(jobName))
                .storeDurably(true)
                .build();
    }

    private Trigger createTrigger(String jobName, Date start) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(createTriggerKey(jobName))
                .startAt(start)
                .build();
    }

    private void setParameters(JobDetail jobDetail, Map<String, ?> parameters) {
        jobDetail.getJobDataMap().putAll(parameters);
    }

    private void setSpringBatchJobName(JobDetail jobDetail, String jobName) {
        jobDetail.getJobDataMap().put(SpringBatchJob.SPRING_BATCH_JOBNAME_PARAM, jobName);
    }

    private void schedule(JobDetail jobDetail, Trigger trigger) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            deletePreviousJob(scheduler, jobDetail);
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("O job {} foi agendado com sucesso", jobDetail.getKey().getName());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private void deletePreviousJob(Scheduler scheduler, JobDetail jobDetail) throws SchedulerException {
        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.deleteJob(jobDetail.getKey());
            logger.info("Removendo o job {}", jobDetail.getKey().getName());
        }
    }

    private JobKey createJobKey(String jobName) {
        return new JobKey(jobName, FIXED_QUARTZ_JOBGROUP);
    }

    private TriggerKey createTriggerKey(String jobName) {
        return new TriggerKey(jobName, FIXED_QUARTZ_JOBGROUP);
    }

}
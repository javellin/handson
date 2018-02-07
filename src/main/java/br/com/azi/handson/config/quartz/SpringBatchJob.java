package br.com.azi.handson.config.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class SpringBatchJob implements org.quartz.Job {

    public final static String SPRING_BATCH_JOBNAME_PARAM = "springBatchJobName";
    private final static String TIMESTAMP_PARAM = "springBatchTimestamp";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JobLauncher jobLauncher;

    @Autowired
    protected JobLocator jobLocator;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String jobName = getJobName(dataMap);
        JobParameters parameters = createParameters(dataMap, jobName);

        try {
            Job job = jobLocator.getJob(jobName);
            JobExecution jobExecution = jobLauncher.run(job, parameters);
            logger.info("Job Quartz {}_{} foi executado com sucesso", job.getName(), jobExecution.getId());

        } catch (Exception e) {
            logger.error("Ocorreu uma excecao do Job Quartz");
            throw new RuntimeException(e);
        }
    }

    private String getJobName(JobDataMap dataMap) {
        return (String) dataMap.get(SPRING_BATCH_JOBNAME_PARAM);
    }

    private JobParameters createParameters(JobDataMap dataMap, String exclusionParameter) {
        JobParametersBuilder builder = new JobParametersBuilder();
        addUniqueTimestamp(builder);
        for (Map.Entry parameter : dataMap.entrySet()) {
            String name = (String) parameter.getKey();
            if (shouldBeExcluded(exclusionParameter, name)) {
                continue;
            }
            addParameter(builder, name, parameter.getValue());
        }
        return builder.toJobParameters();
    }

    private void addUniqueTimestamp(JobParametersBuilder builder) {
        builder.addParameter(TIMESTAMP_PARAM, new JobParameter(new Date()));
    }

    private void addParameter(JobParametersBuilder builder, String name, Object value) {
        if (value instanceof String) {
            builder.addString(name, (String) value);
        }
        else if (value instanceof Date) {
            builder.addDate(name, (Date) value);
        }
        else if (value instanceof Long) {
            builder.addLong(name, (Long) value);
        }
        else if (value instanceof Double) {
            builder.addDouble(name, (Double) value);
        }
        else {
            throw new RuntimeException("Tipo do parametro é inválido");
        }
    }

    private boolean shouldBeExcluded(String exclusion, String name) {
        return exclusion != null && exclusion.equals(name);
    }

}
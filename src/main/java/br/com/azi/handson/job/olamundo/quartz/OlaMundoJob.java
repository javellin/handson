package br.com.azi.handson.job.olamundo.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OlaMundoJob implements Job {

    private static final String PARAM_NAME = "parametro1";

    @Autowired
    @Qualifier("Estoque")
    private org.springframework.batch.core.Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executei o quartz job - Ola mundo");

        String content = context.getMergedJobDataMap().getString(PARAM_NAME);

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString(PARAM_NAME, content);

        try {
            jobLauncher.run(job, builder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}

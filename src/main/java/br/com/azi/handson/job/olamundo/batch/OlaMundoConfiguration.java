package br.com.azi.handson.job.olamundo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class OlaMundoConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private OlaMundoTaskLet taskLet;

    @Autowired
    private OlaMundoProcessor processor;

    @Autowired
    private OlaMundoWriter writer;

    @Bean
    @JobScope
    public OlaMundoReader createReader(@Value("#{jobParameters['parametro1']}") String parametro1) {
        return new OlaMundoReader(parametro1);
    }

    @Bean(name = "OlaMundo")
    public Job job() {
        return jobBuilderFactory.get("OlaMundo")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("OlaMundo.step1")
                .tasklet(taskLet)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("OlaMundo.step2")
                .<String, String>chunk(2)
                .reader(createReader(null))
                .processor(processor)
                .writer(writer)
                .build();
    }
}

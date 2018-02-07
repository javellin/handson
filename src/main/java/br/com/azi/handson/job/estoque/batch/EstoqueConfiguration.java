package br.com.azi.handson.job.estoque.batch;

import br.com.azi.handson.config.batch.JpaQueryProviderImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;

@Configuration
public class EstoqueConfiguration {

    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaPagingItemReader<Estoque> createReader() {
        JpaQueryProviderImpl<Estoque> queryProvider = new JpaQueryProviderImpl<>();
        queryProvider.setQuery("Estoque.all");

        JpaPagingItemReader<Estoque> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryProvider(queryProvider);
        reader.setPageSize(PAGE_SIZE);

        try {
            reader.afterPropertiesSet();
            return reader;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ItemProcessor<Estoque, Estoque> createProcessor() {
        return estoque -> {
            estoque.setSaldo(estoque.getSaldo().add(BigDecimal.TEN));
            return estoque;
        };
    }

    @Bean
    public ItemWriter<Estoque> createWriter() {
        JpaItemWriter<Estoque> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        try {
            writer.afterPropertiesSet();
            return writer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "Estoque")
    public Job job() {
        return jobBuilderFactory
                .get("Estoque")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory
                .get("Estoque.step1")
                .<Estoque, Estoque>chunk(PAGE_SIZE)
                .processor(createProcessor())
                .writer(createWriter())
                .reader(createReader())
                .build();
    }
}

package br.com.azi.handson.job.olamundo.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OlaMundoWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> list) throws Exception {
        list.forEach(item -> log.info("Gravando {}", item));
    }
}

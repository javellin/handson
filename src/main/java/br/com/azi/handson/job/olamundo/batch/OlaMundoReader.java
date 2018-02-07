package br.com.azi.handson.job.olamundo.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@Slf4j
public class OlaMundoReader implements ItemReader<String> {

    private String[] dados = {"Item1", "Item2", "Item3", "Item4", null};

    private Integer index = 0;

    private String parametro1;

    public OlaMundoReader(String parametro1) {
        this.parametro1 = parametro1;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Imprimindo parametro1: {}", parametro1);
        String item = dados[index++];
        log.info("Estou lendo {}", item);
        return item;
    }
}

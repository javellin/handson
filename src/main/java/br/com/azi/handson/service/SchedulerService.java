package br.com.azi.handson.service;

import br.com.azi.handson.business.SchedulerBO;
import br.com.azi.handson.job.estoque.batch.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/jobs/scheduler")
public class SchedulerService {

    @Autowired
    private SchedulerBO schedulerBO;

    @PostMapping
    public void schedule(@RequestBody ScheduleDTO scheduleDTO) {
        schedulerBO.schedule(scheduleDTO.getJobName(), scheduleDTO.getStart(), scheduleDTO.getParameters());
    }

}
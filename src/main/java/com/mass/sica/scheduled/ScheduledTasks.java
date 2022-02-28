/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.scheduled;

import com.mass.sica.utils.services.UtilsService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Ce code a pour objectif d'effectuer les tâc
 *
 *
 * @author sci2m
 */
@Component
@Slf4j
public class ScheduledTasks {
    
    @Autowired
    private UtilsService utilsService;
    
    //@Scheduled(fixedDelay = 10000)
    @Scheduled(cron = "0 0 * * 1 ?")
    public void tasks() {
        utilsService.sendNewsLetters(new Date());
    }
    
}

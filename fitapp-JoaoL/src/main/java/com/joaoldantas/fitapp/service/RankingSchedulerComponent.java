package com.joaoldantas.fitapp.service;

import com.joaoldantas.fitapp.entity.Desafio;
import com.joaoldantas.fitapp.repository.DesafioRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RankingSchedulerComponent {

    private final DesafioRepository desafioRepository;

    public RankingSchedulerComponent(DesafioRepository desafioRepository) {
        this.desafioRepository = desafioRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateRankingOnStart() {
        System.out.println("DEBUG: Aplicação iniciando, atualizando ranking caso necessario.");
        updateChallengesRanking();
    }

    // Vai verificar diariamente a meia-noite se precisa disponibilizar ranking
    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "0/10 * * * * *") // Verifica a cada 10 segundos, para testes apenas
    public void updateRankingDaily() {
        updateChallengesRanking();
    }

    private void updateChallengesRanking() {
        List<Desafio> desafioList = desafioRepository.findAll();

        /*
         * Checar cada desafio se ele terminou
         * Caso tenha terminado ele modifica o 'finalizado' para 'false' travando o desafio
         * E tendo travado o desafio o mapeador começa a disponibilizar a lista de ranking
         */
        if(!desafioList.isEmpty()) {
            for(Desafio d : desafioList) {
                if(d.getEndAt().isBefore(LocalDate.now()) && !d.isFinalizado()) {
                    System.out.println("DEBUG: Atualizando ranking.");
                    d.setFinalizado(true);
                    desafioRepository.save(d);
                }
            }
        }
    }
}

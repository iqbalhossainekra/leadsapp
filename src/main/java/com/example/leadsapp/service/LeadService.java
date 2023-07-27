package com.example.leadsapp.service;

import com.example.leadsapp.model.Lead;
import com.example.leadsapp.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    @Autowired
    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public void uploadLeadsFromCsv(String filePath) {
        List<Lead> leads = readLeadsFromCsv(filePath);

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (Lead lead : leads) {
            executorService.execute(() -> leadRepository.save(lead));
        }

        executorService.shutdown();
    }

    private List<Lead> readLeadsFromCsv(String filePath) {
        List<Lead> leads = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    Lead lead = new Lead(data[0], data[1], data[2], data[3]);
                    leads.add(lead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return leads;
    }
}

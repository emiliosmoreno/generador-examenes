package com.example.service;

import com.example.model.Verbo;
import com.example.repository.VerboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VerboService {
    @Autowired
    private VerboRepository verboRepository;

    public List<Verbo> obtenerTodos() {
        return verboRepository.findAll();
    }

    public Optional<Verbo> obtenerPorId(Integer id) {
        return verboRepository.findById(id);
    }
}

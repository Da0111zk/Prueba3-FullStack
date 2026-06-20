package com.example.ubicaciones.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ubicaciones.modelo.Ubicacion;
import com.example.ubicaciones.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    @Transactional(readOnly = true)
    public List<Ubicacion> obtenerUbicaciones() {
        return ubicacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ubicacion> obtenerPorId(Long ubicacionId) {
        return ubicacionRepository.findById(ubicacionId);
    }

    @Transactional
    public Ubicacion guardar(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public Optional<Ubicacion> actualizar(Long ubicacionId, Ubicacion ubicacion) {
        return ubicacionRepository.findById(ubicacionId)
                .map(ubicacionExistente -> {
                    ubicacionExistente.setRazonSocial(ubicacion.getRazonSocial());
                    ubicacionExistente.setPasillo(ubicacion.getPasillo());
                    ubicacionExistente.setTelefono(ubicacion.getTelefono());
                    ubicacionExistente.setRut(ubicacion.getRut());
                    ubicacionExistente.setActivo(ubicacion.getActivo());
                    ubicacionExistente.setEmail(ubicacion.getEmail());

                    return ubicacionRepository.save(ubicacionExistente);
                });
    }

    @Transactional
    public boolean eliminar(Long ubicacionId) {
        if (!ubicacionRepository.existsById(ubicacionId)) {
            return false;
        }

        ubicacionRepository.deleteById(ubicacionId);
        return true;
    }
}
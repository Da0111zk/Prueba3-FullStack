package com.example.ms_ubicaciones.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_ubicaciones.dto.DtoResponseUbi;
import com.example.ms_ubicaciones.dto.DtoUbi;
import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;

//mapeo
    private DtoResponseUbi mapToDto(Ubicacion ubicacion){
        return new DtoResponseUbi(
                ubicacion.getUbicacionId(),
                ubicacion.getRazonSocial(),
                ubicacion.getPasillo(),
                ubicacion.getTelefono(),
                ubicacion.getRut(),
                ubicacion.getActivo(),
                ubicacion.getEmail()

        );
    }

    public List<DtoResponseUbi> obtenerUbicaciones() {
        return ubicacionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    //verifica si el servicio existe
    public Optional<DtoResponseUbi> obtenerPorId(Long ubicacionId) {
        return ubicacionRepository.findById(ubicacionId).map(this::mapToDto );
    }

    public DtoResponseUbi guardar(DtoUbi dtoUbi){
        Ubicacion ubicacion = new Ubicacion(
                dtoUbi.getUbicacionId(),
                dtoUbi.getRazonSocial(),
                dtoUbi.getPasillo(),
                dtoUbi.getTelefono(),
                dtoUbi.getRut(),
                dtoUbi.getActivo(),
                dtoUbi.getEmail());
        return mapToDto(ubicacionRepository.save(ubicacion));
    }

    public Optional<DtoResponseUbi> actualizar(Long ubicacionId, DtoUbi dtoUbi) {
        return ubicacionRepository.findById(ubicacionId).map(ubicacion -> {
            ubicacion.setRazonSocial(dtoUbi.getRazonSocial());
            ubicacion.setPasillo(dtoUbi.getPasillo());
            ubicacion.setTelefono(dtoUbi.getTelefono());
            ubicacion.setRut(dtoUbi.getRut());
            ubicacion.setActivo(dtoUbi.getActivo());
            ubicacion.setEmail(dtoUbi.getEmail());
            return mapToDto(ubicacionRepository.save(ubicacion));
        });
    }



    public void eliminar(Long ubicacionId) {
        ubicacionRepository.deleteById(ubicacionId);
    }


}

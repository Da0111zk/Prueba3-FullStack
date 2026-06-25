package com.example.ms_categorias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_categorias.Dto.DtoCategoria;
import com.example.ms_categorias.Dto.DtoResponseCategoria;
import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioCategoria {
    private final CategoriaRepository categoriaRepository;

    //mapeo
    private DtoResponseCategoria mapToDto(Categoria categoria){
        return new DtoResponseCategoria(
                categoria.getCategoriaId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado()
        );
    }

    
    public List<DtoResponseCategoria> obtenerCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(java.util.stream.Collectors.toList());
    }
//verifica si el servicio existe
    public Optional<DtoResponseCategoria> obtenerPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId).map(this::mapToDto);
    }

    public DtoResponseCategoria guardar(DtoCategoria dtoCategoria){
        Categoria categoria = new Categoria(
                dtoCategoria.getCategoriaId(),
                dtoCategoria.getNombre(),
                dtoCategoria.getDescripcion(),
                dtoCategoria.getEstado());
        return mapToDto(categoriaRepository.save(categoria));
    }

    public Optional<DtoResponseCategoria> actualizar(Long categoriaId, DtoCategoria dtoCategoria) {
        return categoriaRepository.findById(categoriaId).map(categoria -> {
            categoria.setNombre(dtoCategoria.getNombre());
            categoria.setDescripcion(dtoCategoria.getDescripcion());
            categoria.setEstado(dtoCategoria.getEstado());
            return mapToDto(categoriaRepository.save(categoria));
        });
    }


    public void eliminar(Long categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }
}
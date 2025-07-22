package vn.aptech.java.services;

import vn.aptech.java.dtos.CreateModelDTO;
import vn.aptech.java.dtos.UpdateModelDTO;
import vn.aptech.java.models.Model;

import java.util.List;
import java.util.Optional;

public interface ModelService {
    List<Model> getModels(String keyword);
    Optional<Model> getModelById(Long id);
    Model createModel(CreateModelDTO createModelDTO);
    Model updateModel(UpdateModelDTO updateModelDTO);
    void deleteModel(Long id);
}

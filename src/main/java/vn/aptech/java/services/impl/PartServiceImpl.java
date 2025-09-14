package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.java.dtos.admin.CreatePartDTO;
import vn.aptech.java.dtos.admin.UpdatePartDTO;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.models.Part;
import vn.aptech.java.models.PartType;
import vn.aptech.java.repositories.PartRepository;
import vn.aptech.java.services.LaptopService;
import vn.aptech.java.services.PartService;
import vn.aptech.java.services.PartTypeService;

import java.util.List;
import java.util.Optional;
@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private PartTypeService partTypeService;
    @Autowired
    private LaptopService laptopService;

    @Override
    public List<Part> getParts(String name, Long partTypeId, Long laptopId) {
        if (name == null || name.isEmpty()) {
            name = null;
        }
        if (partTypeId == null || partTypeId <= 0) {
            partTypeId = null;
        }
        if (laptopId == null || laptopId <= 0) {
            laptopId = null;
        }
        return partRepository.filterParts(name, partTypeId, laptopId);

    }

    @Override
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }

    @Override
    public void createPart(CreatePartDTO createPartDTO) {
        Part part = new Part();
        Optional<PartType> partTypeOpt = partTypeService.getPartTypeById(createPartDTO.getPartTypeId());
        if (partTypeOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy loại linh kiện có ID: " + createPartDTO.getPartTypeId());
        }
        part.setPartType(partTypeOpt.get());
        if(createPartDTO.getLaptopId() != null) {
            Optional<Laptop> laptopOpt = laptopService.getLaptopById(createPartDTO.getLaptopId());
            if (laptopOpt.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy laptop có ID: " + createPartDTO.getLaptopId());
            }
            part.setLaptop(laptopOpt.get());
        }
        part.setName(createPartDTO.getName());
        part.setPrice(createPartDTO.getPrice());
        part.setQuantity(createPartDTO.getQuantity());
        part.setWarrantyPeriod(createPartDTO.getWarrantyPeriod());
        part.setImgUrl(createPartDTO.getImgUrl());
        partRepository.save(part);
    }

    @Override
    public void updatePart(UpdatePartDTO updatePartDTO) {
        Optional<Part> partOpt = partRepository.findById(updatePartDTO.getId());
        if(partOpt.isEmpty()){
            throw new IllegalArgumentException("Không tìm thấy linh kiện với ID: " + updatePartDTO.getId());
        }
        Part part = partOpt.get();
        Optional<PartType> partTypeOpt = partTypeService.getPartTypeById(updatePartDTO.getPartTypeId());
        if (partTypeOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy loại linh kiện có ID: " + updatePartDTO.getPartTypeId());
        }
        if(updatePartDTO.getLaptopId() != null) {
            Optional<Laptop> laptopOpt = laptopService.getLaptopById(updatePartDTO.getLaptopId());
            if (laptopOpt.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy laptop có ID: " + updatePartDTO.getLaptopId());
            }
            part.setLaptop(laptopOpt.get());
        }else {
            part.setLaptop(null);
        }
        part.setPartType(partTypeOpt.get());
        part.setName(updatePartDTO.getName());
        part.setPrice(updatePartDTO.getPrice());
        part.setQuantity(updatePartDTO.getQuantity());
        part.setWarrantyPeriod(updatePartDTO.getWarrantyPeriod());
        part.setImgUrl(updatePartDTO.getImgUrl());
        partRepository.save(part);
    }

    @Override
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Optional<Part> lockPartById(Long id) {
        return partRepository.findByIdWithLock(id);
    }

    @Override
    public long count() {
        return partRepository.count();
    }
}

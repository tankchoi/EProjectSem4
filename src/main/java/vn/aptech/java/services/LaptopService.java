package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.Laptop;
import vn.aptech.java.repositories.LaptopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LaptopService {
    @Autowired
    private LaptopRepository laptopRepository;
    public List<Laptop> getAllLaptops(){
        return laptopRepository.findAll();
    }
    public void addLaptop(Laptop laptop){
        laptopRepository.save(laptop);
    }
    public void updateLaptop(Laptop laptop){
        Optional<Laptop> oldLaptop = laptopRepository.findById(laptop.getId());
        laptop.setCreatedAt(oldLaptop.get().getCreatedAt());
        laptopRepository.save(laptop);
    }
    public void deleteLaptop(Long id){
        laptopRepository.deleteById(id);
    }
    public Optional<Laptop> findLaptopById(Long id){
        return laptopRepository.findById(id);
    }
    public List<Laptop> findLaptopsByName(String name){
        return laptopRepository.findByNameContaining(name);
    }
    public List<Laptop> findLaptopsByModelId(Long id){
        return laptopRepository.findByModelId(id);
    }

}

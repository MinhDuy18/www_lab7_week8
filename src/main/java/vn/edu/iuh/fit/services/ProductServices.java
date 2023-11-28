package vn.edu.iuh.fit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.models.Customer;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.CustomerRepository;
import vn.edu.iuh.fit.repositories.ProductRepository;


import java.util.List;

@Service
public class ProductServices {
    @Autowired
    private ProductRepository repository;
    public Page<Product> findPaginate(int pageNo,int pageSize,String sortBy, String sortDirection)
    {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        return repository.findAll(pageable);
    }
}

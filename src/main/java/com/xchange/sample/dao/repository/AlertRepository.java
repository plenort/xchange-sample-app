package com.xchange.sample.dao.repository;

import com.xchange.sample.dao.domain.Alert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertRepository extends CrudRepository<Alert, Long>{

    Optional<Alert> findByPairAndLimit(String pair, Long limit);

}

package com.br.keyroom.repository;

import com.br.keyroom.domain.Secret;
import com.br.keyroom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretRepository extends JpaRepository<Secret, Integer> {
    List<Secret> findAllByUser(User user);
}

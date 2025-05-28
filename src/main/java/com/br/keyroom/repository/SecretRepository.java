package com.br.keyroom.repository;

import com.br.keyroom.domain.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretRepository extends JpaRepository<Secret, Integer> {
}

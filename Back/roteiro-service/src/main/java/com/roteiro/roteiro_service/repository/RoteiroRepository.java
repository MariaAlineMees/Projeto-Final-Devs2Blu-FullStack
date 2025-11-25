package com.roteiro.roteiro_service.repository;

import com.roteiro.roteiro_service.model.Roteiro;
import com.roteiro.roteiro_service.model.User; // Importar User
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Importar List

public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {
    // Método para encontrar todos os roteiros de um usuário específico
    List<Roteiro> findByUser(User user);
}

package com.example.eventnotify.customer.repository;

import com.example.eventnotify.customer.entity.MultiKey;
import com.example.eventnotify.customer.entity.MultiKeyEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiKeyEntityRepository extends JpaRepository<MultiKeyEntity, MultiKey> {}

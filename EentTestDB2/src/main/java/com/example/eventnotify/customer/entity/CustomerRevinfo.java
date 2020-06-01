package com.example.eventnotify.customer.entity;

import javax.persistence.Entity;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity(name = "CUSTOMER_REVINFO")
@RevisionEntity
public class CustomerRevinfo extends DefaultRevisionEntity {}

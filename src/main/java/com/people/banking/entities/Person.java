package com.people.banking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "document_id")
    private Document document;
    private String country;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate bornDate;
    @OneToOne(cascade=CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "father_id")
    private Person father;
}
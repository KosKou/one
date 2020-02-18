package com.example.one.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

//@Data  //@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "cardbins")
public class CardBin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int binNumber;
    @NotEmpty
    private String binType;

    @JsonIgnore //The flow follows one way -> from cardBin to attributes
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//EAGER gets all db records
    private List<Attribute> attributes;

    @NotEmpty
    private String state;
}

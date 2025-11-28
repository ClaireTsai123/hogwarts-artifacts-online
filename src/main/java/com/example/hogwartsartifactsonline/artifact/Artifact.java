package com.example.hogwartsartifactsonline.artifact;

import com.example.hogwartsartifactsonline.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
public class Artifact implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String imgUrl;

    @ManyToOne
    private Wizard owner;



}

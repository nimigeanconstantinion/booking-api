package com.example.book_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name="Programare")
@Table(name="programari")
public class Programare implements Comparable<Programare> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String client;
    private String section;
    private LocalDateTime data1,data2;
    private String obs;

    public Programare(String client, String section, LocalDateTime data1, LocalDateTime data2,String obs) {
        this.client = client;
        this.section = section;
        this.data1 = data1;
        this.data2 = data2;
        this.obs=obs;
    }

    public boolean equals(Object p){
        Programare np=(Programare) p;
        if(np.getSection().equals(np.getSection())&&(((np.getData1().isAfter(data1)||np.getData1().isEqual(data1))&&
            (np.getData1().isBefore(data2)||np.getData1().isEqual(data2)))||
            ((np.getData2().isAfter(data1)||np.getData2().isEqual(data1))&&
            (np.getData2().isBefore(data2)||np.getData2().isEqual(data2))))){
            return true;
        }
        return false;
    }
    public int compareTo(Programare p){
        if(section.compareTo(p.section)<0){
            return -1;
        }else if(section.compareTo(p.section)>0){
            return 1;
        }else{
            return data1.compareTo(p.data1);

        }
    }
}

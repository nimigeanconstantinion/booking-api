package com.example.book_management.controller;

import com.example.book_management.model.Programare;
import com.example.book_management.repository.BookRepository;
import com.example.book_management.tools.CompareDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/booking")
@CrossOrigin
public class BookController {
    BookRepository bookrepo;
    @Autowired
    public BookController(BookRepository bookrepo){
        this.bookrepo=bookrepo;
    }

    @ResponseStatus
    @GetMapping
    public List<Programare> getAllProgramari(){
        return this.bookrepo.findAll();
    }

    @ResponseStatus
    @GetMapping("/{zi}/{sectiune}")
    public List<Programare> getDay(@PathVariable String zi,@PathVariable String sectiune){
        DateTimeFormatter format=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDateTime day=LocalDateTime.parse(zi,format);
        CompareDate comparare=new CompareDate();
        //&&
        //                java.sql.Timestamp.valueOf(a.getData1())==java.sql.Timestamp.valueOf(day)
        //comparare.compare(a.getData1(),day)==0
        return this.bookrepo.findAll().stream().filter(a->a.getSection().equals(sectiune)&&comparare.compare(a.getData1(),day)==0).collect(Collectors.toList());
    }

    @ResponseStatus
    @PostMapping
    public Programare addProgramare(@RequestBody Programare p){
        List<Programare> filtered=new ArrayList<>();
        filtered=this.bookrepo.findAll().stream().filter(b->b.equals(p)).sorted().collect(Collectors.toList());

        if (filtered.size() == 0) {
            this.bookrepo.save(p);
            return p;
        }
        return null;
    }

    @ResponseStatus
    @PostMapping("/mkday/{section}/{start}/{fin}/{interval}/{pause}")
    public void mkSlots(@PathVariable String section,
                        @PathVariable String start,
                        @PathVariable String fin,
                        @PathVariable int interval,@PathVariable int pause){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime dstart = LocalDateTime.parse(start, formatter);
        LocalDateTime dfin = LocalDateTime.parse(fin, formatter);
        System.out.println("-----------------------------------");
        System.out.println(dstart);

        long minutes= Duration.between(dstart,dfin).toMinutes();
        int nrIntervale=(int)(minutes/(interval+pause));
        System.out.println(nrIntervale);
        List<Programare> prg=new ArrayList<>();
        Programare qRef=new Programare("",section,dstart,dstart.plusMinutes(interval+pause),"");
        prg=this.bookrepo.findAll().stream().filter(b->b.equals(qRef)).collect(Collectors.toList());
        LocalDateTime tmp=dstart;
        if(prg.size()==0){
            for(int i=0;i<nrIntervale;i++){
               Programare pRef=new Programare("",section,tmp,tmp.plusMinutes(interval),"");
                System.out.println(pRef);
                this.bookrepo.save(pRef);
                tmp=tmp.plusMinutes(interval+pause).plusSeconds(1);
            }
        }
    }

    @ResponseStatus
    @PutMapping("/upd/{id}")
    public void updateSlot(@RequestBody Programare newP,@PathVariable Long id) throws Exception{
        this.bookrepo.findById(id).map(p->{
            p.setClient(newP.getClient());
            p.setObs(newP.getObs());
            return this.bookrepo.save(p);
        }).orElseThrow(()->new Exception("Eroare de update"));
    }

    @ResponseStatus
    @DeleteMapping("/{id}")
    public void deleteSlot(@PathVariable Long id){
        Optional opt=this.bookrepo.findById(id);
        opt.ifPresent(s->{
            this.bookrepo.deleteById(id);
        });
    }
}

package com.example.book_management.tools;

import java.time.LocalDateTime;
import java.util.Comparator;

public  class CompareDate implements Comparator<LocalDateTime> {

    @Override
    public int compare(LocalDateTime d1,LocalDateTime d2){
        if(d1.getYear()<d2.getYear()){
            return -1;
        }else if(d1.getYear()>d2.getYear()){
            return 1;
        }else{
            if(d1.getDayOfYear()<d2.getDayOfYear()){
                return -1;
            }else if(d1.getDayOfYear()>d2.getDayOfYear()){
                return 1;
            }else{
                return 0;
            }
        }
    }


}

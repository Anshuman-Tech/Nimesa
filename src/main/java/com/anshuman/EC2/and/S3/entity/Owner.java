package com.anshuman.EC2.and.S3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
@Builder
public class Owner {

    @Id
    private String ownerId;

    private String ownerName;

    public Owner(){
    }

    public Owner(String ownerId, String ownerName){
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public String getOwnerId(){
        return ownerId;
    }
    public String getOwnerName(){
        return ownerName;
    }

    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }
    public void setOwnerName(String ownerName){
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "ownerId='" + ownerId + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}

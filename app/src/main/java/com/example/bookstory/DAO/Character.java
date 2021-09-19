package com.example.bookstory.DAO;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Set;

@Entity
public class Character {
    @PrimaryKey(autoGenerate = false)
    public String characterName;

    public Set<String> pseudonyms;
}

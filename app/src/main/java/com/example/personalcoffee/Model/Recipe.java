package com.example.personalcoffee.Model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Recipe implements Serializable {
    String recipeId;
    String espresso;
    String water;
    String syrup;
    String name;
    String love;
}

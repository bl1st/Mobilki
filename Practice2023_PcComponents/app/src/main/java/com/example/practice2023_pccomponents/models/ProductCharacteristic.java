package com.example.practice2023_pccomponents.models;

public class ProductCharacteristic {
    public int Id;
    public int CharacteristicId;
    public String CharacteristicName;
    public String CharacteristicValue;
    public String Unit;


    public ProductCharacteristic(int id, int charId, String charName, String charValue, String unit)
    {
        this.Id = id;
        this.CharacteristicId = charId;
        this.CharacteristicName = charName;
        this.CharacteristicValue = charValue;
        this.Unit = unit;
    }


    public String toString()
    {
        String res = CharacteristicName + ": " + CharacteristicValue;
        if (Unit != "null")
            res += " " + Unit;
        return res;
    }

}

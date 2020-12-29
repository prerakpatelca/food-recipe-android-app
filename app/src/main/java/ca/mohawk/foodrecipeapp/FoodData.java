package ca.mohawk.foodrecipeapp;

public class FoodData {

    private String itemName;
    private String itemDescription;
    private String itemPrice;
    private String itemImage;
    private String key;
    private String recipeKey;

    public FoodData() {

    }

    public FoodData(String itemName, String itemDescription, String itemPrice, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
    }

    public FoodData(String itemName, String itemDescription, String itemPrice, String itemImage, String key,String recipeKey){
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.key = key;
        this.recipeKey = recipeKey;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

}

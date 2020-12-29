# Food recipe android application
This Android application will allow anybody to add their unique recipe and will be able browse through the foods from different ethnicity. Logged in user will have their favourite recipes page. Each recipe will show ingredients needed and directions to cook on click.

Problem the application solves and who will use it:
There are so many apps in the market right now for recipe manager for Healthy foods or foods from different country, but there is no app for “Indian cuisines”. So, I am filling that gap by designing an Android App. 
And, the application would be useful to people who likes to cook Indian cuisines.


Technologies

To design Recipe Manager Android App, I would use following technologies:
- Java / Android Library
-	XML
-	JSON (database)
-	Git / GitHub (VCS – Version Control System)
-	Android Studio (Platform)

*******************
Summary of Functionality
*******************

Login Page:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture1.png)

This is a login screen. At the top of the screen, it has an App Name then just below that there is Logo and a tagline. It is the first page that user will see when they download the app. This page allows user to either login with their existing account or create a new one. This page would be responsible to authenticate credentials. User would also be able to login in as Guest user.

Sign Up Page:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture2.png)

This page allows to create an account for new user when user clicks on Sign Up on Login page (Figure 1). Full Name, Phone, an email and a password is necessary to create an account. This page will ensure that email is unique than ones from the database, password matches required criteria. It will also ensure that email address entered is a real email address. Upon entering all required information user can hit Create User to create a new account.

Home Page:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture3.png)

Home page is a default page once user is logged in or presses Guest Login in Figure 1. It shows the feed of all recipes from the database. Customer would be able to browse all the recipes available from the database by scrolling up and down, and also search recipes. From this page, user can go to Dashboard Page, Saved Recipe Page and Profile Page is accessible on one click from Navbar and it is only accessible to logged in user.

Recipe Page:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture4.png)

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture5.png)

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture6.png)

On clicking an image or recipe name on Home Page (Figure 3), it will take user to Recipe page (Figure 4) where it will allow user to save recipe as Favourite and show detailed instructions and the list of ingredients.

Saved Recipes Page:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture7.png)
![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture8.png)

Upon clicking on Favorites from the Navbar, it will take user to the Saved Recipes Page (Figure 5). On this page user has access to the list of their Favorites recipes. User has a option to remove a recipe from the Favorite list by clicking little heart icon in second figure.

Add Recipe:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture9.png)
![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture10.png)

Upon clicking on My Recipes from the Navbar Add button will appear on the screen, it takes user to Add Recipe Page (Figure 6) second figure. On this page user can add their own recipe. In order to add a recipe to the Recipes Database, user will require to add Recipe name, ingredients, directions under description, cook time and upload a picture of the recipe.

Search by Recipes:

![alt text](https://github.com/prerakpatelca/food-recipe-android-app/blob/master/Picture11.png)


Here we see the search Recipe functionality, where users can search for the recipes Recipe Name. It pull ups all related search results from the database. And upon clicking any single recipe it displays all the details of the recipe.

*******************
Development Stack, Frameworks, Plug-ins
*******************
To design Recipe Manager Android App, I used following technologies:
-	Java / Android Library
-	XML
-	Firebase (database)
-	Android Studio (Platform)

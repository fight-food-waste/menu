# Fight Food Waste Menu Generator

Requirements:

- Intellij
- JavaFX

Follow [this guide](https://openjfx.io/openjfx-docs/) to link JavaFX with Intellij.

The following libraries are being used:

- [Gson](https://github.com/google/gson) form JSON parsing
- [Unirest](http://kong.github.io/unirest-java/) for HTTP requests
- [Dom4j](https://dom4j.github.io/) for XML parsing

They will be downloaded from Maven by Intellij.

3 APIs are being used:

- [FightFoodWaste](../ffw-api) to get products from the stock
- [OpenFoodFacts](https://en.wiki.openfoodfacts.org/API) for the products information
- [BigOver v1](http://api.bigoven.com/) for recipes
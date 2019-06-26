package ffw_menu;

import kong.unirest.Unirest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class Recipe {

    static HashMap<String, ArrayList<String>> search(String keyword) throws RecipeApiException {

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());

            // Get recipies as XML from BigOven
            String recipesXml = Unirest.get("http://api.bigoven.com/recipes")
                    .queryString("title_kw", encodedKeyword)
                    .queryString("pg", "1") // page number
                    .queryString("rpp", "20") // results per page
                    .queryString("api_key", "glFUKikehWjLW900etpS564VgIzOWSW5")
                    .asString().getBody();

            ArrayList<String> recipesTitle = new ArrayList<>();
            ArrayList<String> recipesUrl = new ArrayList<>();

            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(new StringReader(recipesXml)); // Can throw DocumentException
                Element recipeSearchResult = document.getRootElement();

                // Iterate down the XML to extract title and URL for each recipe
                // RecipeSearchResult -> Results -> RecipeInfo -> Title/WebURL
                Iterator<Element> recipeSearchResultItr = recipeSearchResult.elementIterator();
                while (recipeSearchResultItr.hasNext()) {
                    Element recipeSearchResultElement = recipeSearchResultItr.next();
                    if (recipeSearchResultElement.getName().equals("Results")) {
                        Iterator<Element> resultsItr = recipeSearchResultElement.elementIterator();
                        while (resultsItr.hasNext()) {
                            // We will iterate trough multiple recipes
                            Element resultsElement = resultsItr.next();
                            if (resultsElement.getName().equals("RecipeInfo")) {
                                // We're in a recipe here
                                Iterator<Element> recipeInfoItr = resultsElement.elementIterator();
                                String title = "", webUrl = "";
                                while (recipeInfoItr.hasNext()) {
                                    Node recipeInfoNode = recipeInfoItr.next();
                                    if (recipeInfoNode.getName().equals("Title")) {
                                        title = recipeInfoNode.getText();
                                    } else if (recipeInfoNode.getName().equals("WebURL")) {
                                        webUrl = recipeInfoNode.getText();
                                    }
                                }
                                recipesTitle.add(title);
                                recipesUrl.add(webUrl);
                            }
                        }
                    }
                }

                // Return the two ArrayList as a Hashmap (can't return 2 variables)
                HashMap<String, ArrayList<String>> map = new HashMap<>();
                map.put("recipesTitle", recipesTitle);
                map.put("recipesUrl", recipesUrl);
                return map;
            } catch (DocumentException e) {
                e.printStackTrace();
                throw new RecipeApiException();
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}

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
import java.util.*;

class Recipe {

    static HashMap<String, ArrayList<String>> search(String keyword) throws RecipeApiException {

        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());

            // Get recipies as XML from BigOven
            String url = "http://api.bigoven.com/recipes?title_kw=" + encodedKeyword + "&pg=1&rpp=20&api_key=glFUKikehWjLW900etpS564VgIzOWSW5";
            String recipesXml = Unirest.get(url).asString().getBody();

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
                                    Node recipeInfoElement = recipeInfoItr.next();
                                    if (recipeInfoElement.getName().equals("Title")) {
                                        title = recipeInfoElement.getText();
                                    } else if (recipeInfoElement.getName().equals("WebURL")) {
                                        webUrl = recipeInfoElement.getText();
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

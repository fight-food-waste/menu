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

public class Recipe {

    public static HashMap<String, ArrayList<String>> search(String keyword) throws RecipeApiException {

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
                Element rootElement = document.getRootElement();

                // Iterate down the XML to extract title and URL for each recipe
                Iterator<Element> itr = rootElement.elementIterator();
                while (itr.hasNext()) {
                    Element element = itr.next();
                    if (element.getName().equals("Results")) {
                        Iterator<Element> itr2 = element.elementIterator();
                        while (itr2.hasNext()) {
                            Element element2 = itr2.next();
                            if (element2.getName().equals("RecipeInfo")) {
                                // We're in a recipe here
                                Iterator<Element> itr3 = element2.elementIterator();
                                String title = "", webUrl = "";
                                while (itr3.hasNext()) {
                                    Node node = itr3.next();
                                    if (node.getName().equals("Title")) {
                                        title = node.getText();
                                    } else if (node.getName().equals("WebURL")) {
                                        webUrl = node.getText();
                                    }
                                }
                                recipesTitle.add(title);
                                recipesUrl.add(webUrl);
                            }
                        }
                    }
                }

                // Return the two ArrayList as a Hashmap
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

package com.ePurchase.utils;

import com.ePurchase.domain.Product;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Get It on 9/22/2017.
 */
public class ItemLookupUtility {

    private static final String ACCESS_KEY_ID = "AKIAISSI7WQ6DFP35QSA";
    private static final String SECRET_KEY = "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";
    private static final String ENDPOINT = "webservices.amazon.in";
    private static final String ITEM_ID = "0545010225";


    public String getRequestUrl(String browseNodeId) {

        SignedRequestsHelper helper = null;

        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        String requestUrl = null;
        String title = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", ACCESS_KEY_ID);
        params.put("AssociateTag", "vijender9423-21");
        params.put("BrowseNodeId", "1968120031");
        params.put("SearchIndex","Apparel");
        params.put("Keywords","Apparel");
        params.put("ResponseGroup", "Images,ItemAttributes,Offers");
        requestUrl = helper.sign(params);

/*        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "BrowseNodeLookup");
        params.put("AWSAccessKeyId", ACCESS_KEY_ID);
        params.put("AssociateTag", "vijender9423-21");
        params.put("BrowseNodeId", "976390031");
        params.put("ResponseGroup", "BrowseNodeInfo");
        requestUrl = helper.sign(params);*/
        System.out.println("URL*** "+ requestUrl);
        return requestUrl;
    }





    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    public List<Product> fetchTitle(String requestUrl) {
        String title = null;
        List<Product> productList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            NodeList nList  = doc.getElementsByTagName("Item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Product product = new Product();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :"+temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String productUrl = eElement.getElementsByTagName("DetailPageURL").item(0).getTextContent();
                    product.setImageUrl(productUrl);
                    String imageUrl = eElement.getElementsByTagName("LargeImage").item(0).getTextContent();
                    product.setImageUrl(imageUrl);
                    if (eElement.getElementsByTagName("Price").getLength() > 0) {
                      Element priceElement = (Element) eElement.getElementsByTagName("Price").item(0);
                        if (priceElement.getElementsByTagName("FormattedPrice").getLength() > 0) {
                            String price = priceElement.getElementsByTagName("FormattedPrice").item(0).getTextContent();
                            product.setAmount(price);
                        }
                    }
                    setProductFeatures(product, eElement);
                    }
                    productList.add(product);
                }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return productList;
    }

    private void setProductFeatures(Product product, Element eElement) {
        int i=0;
        while(i<eElement.getElementsByTagName("Feature").getLength()){
            String feature = eElement.getElementsByTagName("Feature").item(i).getTextContent();
            product.getFeature().add(feature);
            i++;
        }
    }
}

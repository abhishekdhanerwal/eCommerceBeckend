package com.ePurchase.serviceImpl;

import com.ePurchase.Repository.UserRepository;
import com.ePurchase.domain.Product;
import com.ePurchase.domain.SingleProduct;
import com.ePurchase.domain.User;
import com.ePurchase.enums.Role;
import com.ePurchase.exception.BadClientDataException;
import com.ePurchase.service.UserService;
import com.ePurchase.utils.ItemLookupUtility;
import com.ePurchase.utils.ObjectMerger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Get It on 9/8/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<String> findIfUserAlreadyExist(User user) {

        List<String> verifyUserList = new ArrayList<>();
        User user1 = userRepository.findByMobile(user.getMobile());
        if (user1 != null) {
            verifyUserList.add("user with mobile " + user.getMobile() + " already exist.");
        }
        user1 = userRepository.findByEmail(user.getEmail());
        if (user1 != null) {
            verifyUserList.add("user with email " + user.getEmail() + " already exist.");
        }
        return verifyUserList;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public String checkForEmailReadOnlyAccess(User user) {

        User savedUser = userRepository.findOne(user.getId());
        if (savedUser != null) {
            if (savedUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                return "Email can't be changed.";
            }
            return "";
        } else {
            return "user with id " + user.getId() + " does not exist";
        }
    }

    @Override
    public List<String> userValidation(User user) {


        List<String> validatonErrorList = new ArrayList<>();
        if (isEmpty(user.getEmail())) {
            validatonErrorList.add("email cannot be null or empty");
        }
        if (isEmpty(user.getMobile())) {
            validatonErrorList.add("mobile cannot be null or empty");
        }
        return validatonErrorList;
    }

    @Override
    public User updateUser(User user) {
        User savedUser = userRepository.findOne(user.getId());
        ObjectMerger.mergeClientEditableFields(user, savedUser);
        return userRepository.save(savedUser);
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findOne(userId);
        return user;
    }

    @Override
    public String setUserPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String findIfUserIsAuthorizedToCreateUser(User user) {
        User loginedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((loginedUser.getRole().equals(Role.SUPER_ADMIN) && !user.getRole().equals(Role.ADMIN)) ||
                (loginedUser.getRole().equals(Role.ADMIN) && !user.getRole().equals(Role.USER))) {
            return "you are not authorized for this action";
        }
        return "";
    }

    @Override
    public User addItemToCart(String userId, String itemId) {

        if (isEmpty(userId)) {
            throw new BadClientDataException("Invalid user id");
        }
        if (isEmpty(itemId)) {
            throw new BadClientDataException("Invalid item id");
        }
        User user = userRepository.findOne(userId);
        boolean flag = checkIfItemIsAlreadyInCart(user.getItemId(), itemId);
        if (!flag) {
            user.getItemId().add(itemId);
            userRepository.save(user);
        } else {
            throw new BadClientDataException("Item already in cart");
        }
        return user;
    }

    @Override
    public List<Product> getCartItems(String userId) {
        String url = null;
        List<Product> productList = new ArrayList<>();
        if (isEmpty(userId)) {
            throw new BadClientDataException("Invalid user id.");
        } else {
            User user = userRepository.findOne(userId);
            if (!user.getItemId().isEmpty()) {
                for (String id : user.getItemId()) {
                    url = ItemLookupUtility.getRequestUrlUsingAsinId(id);
                    productList.addAll(ItemLookupUtility.fetchTitle(url));
                }
            }
        }
        return productList;
    }

    @Override
    public List<Product> getProductsNavigation(String nodeId, String page, String searchIndex) {
        String url = null;
        Map<String, String> map = new HashMap<>();
        List<Product> productList = new ArrayList<>();
        if (isEmpty(nodeId)) {
            throw new BadClientDataException("Invalid node Id.");
        }
        if (isEmpty(page)) {
            throw new BadClientDataException("Invalid page no.");
        }

        if (isEmpty(searchIndex)) {
            throw new BadClientDataException("Invalid searchIndex.");
        }

        url = ItemLookupUtility.getRequestUrlForSearchIndexId(nodeId, page, searchIndex);
      /*  totalPages = ItemLookupUtility.getTotalPage(url);
        if(Integer.parseInt(totalPages)!=0) {
            int categoryPages = Integer.parseInt(totalPages);
            int pagesToBeTraversed = Integer.parseInt(page);
            pagesToBeTraversed *= 2;
            while(pagesToBeTraversed <= categoryPages) {*/
       /* url = ItemLookupUtility.getRequestUrlForSearchIndexId(nodeId, page,
                searchIndex);*/
          /*      productList.addAll(ItemLookupUtility.fetchTitle(url));
            }

        }
        else{
            throw new NotFoundException("Presently no product present.");
        }

        int pagesToBeTraversed = Integer.parseInt(page);
        pagesToBeTraversed *= 2;
        while(pagesToBeTraversed<=Integer.parseInt(totalPages)) {


            totalPages = ItemLookupUtility.getTotalPage(url);
        }


        ItemLookupUtility.getTotalPage(url);
        totalPages = ItemLookupUtility.getTotalPage(url);
*/
        productList.addAll(ItemLookupUtility.fetchTitle(url));
        return productList;

    }

    private boolean checkIfItemIsAlreadyInCart(List<String> itemIds, String itemId) {

        if (itemIds.contains(itemId))
            return true;
        else
            return false;
    }

    @Override
    public Map<String, Object> getAllVariationProducts(String asinId) {

        SingleProduct product = null;
        String requestUrl = null;
        if (isEmpty(asinId)) {
            throw new BadClientDataException("Invalid product ASIN Id.");
        }
        List<SingleProduct> productList = new ArrayList<>();
        requestUrl = ItemLookupUtility.getRequestURlToGetAllVariationASINs(asinId);
        Map<String, Object> allVariationASINs = getAllVariationASINs(requestUrl);
        if(allVariationASINs.containsKey("msg")){
            requestUrl = ItemLookupUtility.getRequestURlUsingAsin(asinId);
            product = getDetailsOfProducts(requestUrl);
            productList.add(product);
            allVariationASINs.put("product", productList);
            allVariationASINs.remove("msg");
        }
        else if(allVariationASINs.containsKey("asinList")) {
            for (String itreableAsinId : (List<String>) allVariationASINs.get("asinList")) {
                requestUrl = ItemLookupUtility.getRequestURlUsingAsin(itreableAsinId);
                product = getDetailsOfProducts(requestUrl);
                productList.add(product);
            }
            allVariationASINs.put("productList", productList);
        }

        return allVariationASINs;
    }

    private SingleProduct getDetailsOfProducts(String requestUrl) {

        SingleProduct singleProduct = new SingleProduct();
        String productInfo = "";
        BigDecimal tempFormattedPrice = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            productInfo = doc.getElementsByTagName("DetailPageURL").item(0).getTextContent();
            singleProduct.setDetailUrl(productInfo);
            productInfo = doc.getElementsByTagName("ASIN").item(0).getTextContent();
            singleProduct.setaSIN(productInfo);
            productInfo = doc.getElementsByTagName("Title").item(0).getTextContent();
            singleProduct.setTitle(productInfo);
            if (doc.getElementsByTagName("Size").getLength() > 0) {
                productInfo = doc.getElementsByTagName("Size").item(0).getTextContent();
                singleProduct.setSize(productInfo);
            }
            if (doc.getElementsByTagName("Color").getLength() > 0) {
                productInfo = doc.getElementsByTagName("Color").item(0).getTextContent();
                singleProduct.setColor(productInfo);
            }
            if(doc.getElementsByTagName("IFrameURL").getLength() > 0){
                productInfo = doc.getElementsByTagName("IFrameURL").item(0).getTextContent();
                singleProduct.setReviewUrl(productInfo);
            }
            productInfo = doc.getElementsByTagName("Brand").item(0).getTextContent();
            singleProduct.setBrand(productInfo);
            if (doc.getElementsByTagName("SalePrice").getLength() > 0) {
                Element eElement = (Element) doc.getElementsByTagName("SalePrice");
                productInfo = eElement.getElementsByTagName("Amount").item(0).getTextContent();
                tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                singleProduct.setSalePrice(tempFormattedPrice.toString());
            } else if (doc.getElementsByTagName("ListPrice").getLength() > 0) {
                Element eElement = (Element) doc.getElementsByTagName("ListPrice").item(0);
                productInfo = eElement.getElementsByTagName("Amount").item(0).getTextContent();
                tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                singleProduct.setSalePrice(tempFormattedPrice.toString());
            }
            if (doc.getElementsByTagName("ImageSet").getLength() > 0) {
                int i = 0;
                while (i < doc.getElementsByTagName("ImageSet").getLength()) {
                    String category = doc.getElementsByTagName("ImageSet").item(i).getAttributes().item(0).getTextContent();
                    if (!category.equals("primary")) {
                        Element imageSet = (Element) doc.getElementsByTagName("ImageSet").item(i);
                        productInfo = imageSet.getElementsByTagName("LargeImage").item(0).getTextContent();
                        singleProduct.getImages().add(productInfo);
                    }
                    i++;
                }
            }
            setSingleProductFeatures(singleProduct, doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return singleProduct;
    }

    private void setSingleProductFeatures(SingleProduct product, Document doc) {
        int i = 0;
        while (i < doc.getElementsByTagName("Feature").getLength()) {
            String feature = doc.getElementsByTagName("Feature").item(i).getTextContent();
            product.getFeatures().add(feature);
            i++;
        }
    }

    private Map<String, Object> getAllVariationASINs(String requestUrl) {

        List<String> variationASINsList = new ArrayList<>();
        List<String> variationDimensionList = new ArrayList<>();
        Map<String, Object> variationASINsAndValidationMessageList = new HashMap<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            if (doc.getElementsByTagName("Variations").getLength() > 0) {
                if (doc.getElementsByTagName("TotalVariations").item(0).getTextContent().equals("0")) {
                    variationASINsAndValidationMessageList.put("msg", "No Variations Found.");
                    return variationASINsAndValidationMessageList;
                }
                if (doc.getElementsByTagName("VariationDimensions").getLength() > 0) {
                    int i = 0;
                    while (i < doc.getElementsByTagName("VariationDimension").getLength()) {
                        variationDimensionList.add(doc.getElementsByTagName("VariationDimension").item(i).getTextContent());
                        i++;
                    }
                }
                NodeList nList = doc.getElementsByTagName("Item");
                for (int temp = 1; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String asinId = eElement.getElementsByTagName("ASIN").item(0).getTextContent();
                        variationASINsList.add(asinId);
                    }
                }
            } else {
                variationASINsAndValidationMessageList.put("msg", "No Variations Found.");

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!variationASINsList.isEmpty()) {
            variationASINsAndValidationMessageList.put("asinList", variationASINsList);
        }
        if(!variationDimensionList.isEmpty()){
            variationASINsAndValidationMessageList.put("variationDimension", variationDimensionList);
        }
        return variationASINsAndValidationMessageList;
    }


    @Override
    public List<Product> getAllProductsForNode(String nodeId, String pageNo, String sort, String searchIndex) {

        String requestUrl = null;
        List<Product> productList = new ArrayList<>();
        if (isEmpty(nodeId)) {
            throw new BadClientDataException("Invalid node Id.");
        }
        if (isEmpty(pageNo)) {
            throw new BadClientDataException("Invalid page no.");
        }
        if (isEmpty(sort)) {
            throw new BadClientDataException("Invalid sort parameter");
        }
        requestUrl = ItemLookupUtility.getRequestUrlForAllNodeProducts(nodeId, pageNo, sort, searchIndex);
        productList = fetchListForAllProducts(requestUrl);

        return productList;
    }

    public static List<Product> fetchListForAllProducts(String requestUrl) {
        String productInfo = null;
        BigDecimal tempFormattedPrice = null;
        List<Product> productList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            NodeList nList = doc.getElementsByTagName("Item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Product product = new Product();
                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    productInfo = eElement.getElementsByTagName("DetailPageURL").item(0).getTextContent();
                    product.setItemURL(productInfo);
                    productInfo = eElement.getElementsByTagName("Title").item(0).getTextContent();
                    product.setTitle(productInfo);
                    productInfo = eElement.getElementsByTagName("ASIN").item(0).getTextContent();
                    product.setAsinId(productInfo);
                    if (eElement.getElementsByTagName("Brand").getLength() > 0) {
                        productInfo = eElement.getElementsByTagName("Brand").item(0).getTextContent();
                        product.setBrand(productInfo);
                    }
                    if (eElement.getElementsByTagName("LargeImage").getLength() > 0) {
                        Element imageUrlElement = (Element) eElement.getElementsByTagName("LargeImage").item(0);
                        productInfo = imageUrlElement.getElementsByTagName("URL").item(0).getTextContent();
                        product.setImageUrl(productInfo);
                    }
                    Element variationSummaryElement = (Element) eElement.getElementsByTagName("VariationSummary")
                            .item(0);
                    productInfo = variationSummaryElement.getElementsByTagName("Amount").item(0).getTextContent();
                    tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                    product.setLowestPrice(tempFormattedPrice.toString());
                    if (variationSummaryElement.getElementsByTagName("HighestSalePrice").getLength() > 0) {
                        Element highestPriceElement = (Element) variationSummaryElement.getElementsByTagName
                                ("HighestSalePrice").item(0);
                        productInfo = highestPriceElement.getElementsByTagName("Amount").item(0).getTextContent();
                        tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                        product.setHighestPrice(tempFormattedPrice.toString());
                    }
                    if (variationSummaryElement.getElementsByTagName("HighestPrice").getLength() > 0) {
                        Element highestPriceElement = (Element) variationSummaryElement.getElementsByTagName
                                ("HighestPrice").item(0);
                        productInfo = highestPriceElement.getElementsByTagName("Amount").item(0).getTextContent();
                        tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                        product.setHighestPrice(tempFormattedPrice.toString());
                    }
                    if (variationSummaryElement.getElementsByTagName("SalePrice").getLength() > 0){
                        Element highestPriceElement = (Element) variationSummaryElement.getElementsByTagName
                                ("SalePrice").item(0);
                        productInfo = highestPriceElement.getElementsByTagName("Amount").item(0).getTextContent();
                        tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                        product.setSalePrice(tempFormattedPrice.toString());
                    }
                    if(!isEmpty(product.getSalePrice()) && variationSummaryElement.getElementsByTagName("ListPrice")
                            .getLength() > 0){
                        Element highestPriceElement = (Element) variationSummaryElement.getElementsByTagName
                                ("ListPrice").item(0);
                        productInfo = highestPriceElement.getElementsByTagName("Amount").item(0).getTextContent();
                        tempFormattedPrice = new BigDecimal(productInfo).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_UP);
                        product.setSalePrice(tempFormattedPrice.toString());
                    }
                }
                productList.add(product);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return productList;


    }
}

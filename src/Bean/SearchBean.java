package Bean;

public class SearchBean {
    public static String product_for_id="product_for_id";
    public static String product_for_barcode="product_for_barcode";
    public static String product_for_like="product_for_like";
    public String searchType;
    public String json;

    public SearchBean(){}
    public SearchBean(String searchType, String json){
        this.searchType=searchType;
        this.json = json;
    }

    public static class S2Product{
        public String likeOr;
        public String FIsPurchase;
        public String FIsSale;
        public String FIsInventory;
        public String FIsProduce;
        public String FIsSubContract;
        public String FIsAsset;

        public S2Product(){}

    }


}

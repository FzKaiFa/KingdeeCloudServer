package Server.ProductSearch;

import Bean.DownloadReturnBean;
import Bean.ProductReturnBean;
import Utils.CommonJson;
import Utils.JDBCUtil;
import Utils.getDataBaseUrl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by NB on 2017/8/7.
 */
@WebServlet(urlPatterns = "/ProductSearchLike")
public class ProductSearchLike extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("json");
        String version = request.getParameter("version");
        String SQL = "";
        Gson gson = new Gson();
        Connection conn = null;
        PreparedStatement sta = null;
        ResultSet rs = null;
        ArrayList<DownloadReturnBean.product> container = new ArrayList<>();
        System.out.println(parameter);
        if (parameter != null) {
            try {
                System.out.println(request.getParameter("sqlip") + " " + request.getParameter("sqlport") + " " + request.getParameter("sqlname") + " " + request.getParameter("sqlpass") + " " + request.getParameter("sqluser"));
                conn = JDBCUtil.getConn(getDataBaseUrl.getUrl(request.getParameter("sqlip"), request.getParameter("sqlport"), request.getParameter("sqlname")), request.getParameter("sqlpass"), request.getParameter("sqluser"));
//                if (version.equals("500116")  || version.equals("500115"))

//                    SQL = "select top 50 FSecCoefficient,FSecUnitID,FIsSnManage,FItemID,FISKFPeriod,convert(INT,FKFPeriod) as FKFPeriod,FNumber,FModel,FName,FFullName,FUnitID,FUnitGroupID,FDefaultLoc,isnull(FProfitRate,0) as FProfitRate,isnull(FTaxRate,1) as FTaxRate,isnull(FOrderPrice,0) as FOrderPrice,isnull(FSalePrice,0) as FSalePrice,isnull(FPlanPrice,0) as FPlanPrice,'' as FBarcode,FSPID,FBatchManager from t_ICItem where FErpClsID not in (6,8) and FDeleted = 0 and (FNumber like '%"+parameter+"%' or FName like '%"+parameter+"%') order by FNumber";//k3 rise 12.3
                SQL="select top 50 t6.FPRODUCEUNITID as 生产单位ID,t5.FPURCHASEUNITID as  采购单位ID,t5.FPURCHASEPRICEUNITID as 采购计价单位ID,t4.FSALEUNITID as 销售单位ID,t4.FSALEPRICEUNITID as 销售计价单位ID,FSTOREUNITID as 库存单位ID,FAUXUNITID as 辅助单位ID,FSTOCKID as 默认仓库ID,FSTOCKPLACEID as 默认仓位ID,FISBATCHMANAGE as 是否启用批号管理,FISKFPERIOD as 是否开启保质期管理,FEXPPERIOD as 保质期,FEXPUNIT as 保质期单位,t2.FISPURCHASE as 允许采购,t2.FISSALE as 允许销售,t2.FISINVENTORY as 允许库存," +
                        "t2.FISPRODUCE as 允许生产,t2.FISSUBCONTRACT as 允许委外,t2.FISASSET as 允许资产,t2.FBASEUNITID as 基本单位ID,t2.FWEIGHTUNITID as 重量单位ID,t2.FVOLUMEUNITID as 尺寸单位ID,t2.FBARCODE as 条码,t2.FGROSSWEIGHT as 毛重,t2.FNETWEIGHT as 净重,t2.FLENGTH as 长,t2.FWIDTH as 宽,t2.FHEIGHT as 高,t2.FVOLUME as 体积,t1.FMaterialid as 物料内码,t0.FNumber as 编码,t0.FOLDNUMBER as 旧物料编码,t1.FName as 商品名称,t1.FSPECIFICATION as 规格型号,t0.FMNEMONICCODE as 助记码 from T_BD_MATERIAL t0 " +
                        "left join t_bd_material_l t1 on (t0.fmaterialid=t1.fmaterialid AND t1.FLocaleId = 2052) left join t_BD_MaterialBase t2 on t2.fmaterialid=t0.fmaterialid  left join T_BD_MATERIALSTOCK t3 on t3.fmaterialid=t0.fmaterialid left join T_BD_MATERIALSALE t4 on t4.fmaterialid=t0.fmaterialid left join T_BD_MATERIALPURCHASE t5 on t5.fmaterialid=t0.fmaterialid left join T_BD_MATERIALPRODUCE t6 on t6.fmaterialid=t0.fmaterialid  where t0.FUSEORGID = 1 AND (t0.FDOCUMENTSTATUS = 'C' AND t0.FFORBIDSTATUS = 'A') AND t0.FFORBIDSTATUS = 'A' and (t0.FNumber like '%"+parameter+"%' or t1.FName like '%"+parameter+"%')";

                sta = conn.prepareStatement(SQL);
                System.out.println("SQL:"+SQL);
                rs = sta.executeQuery();
                DownloadReturnBean downloadReturnBean = new DownloadReturnBean();
                if(rs!=null){
                    int i = rs.getRow();
                    System.out.println("rs的长度"+i);
                    while (rs.next()) {
                        DownloadReturnBean.product productBean = downloadReturnBean.new product();
                        productBean.FProduceUnitID                = rs.getString("生产单位ID");
                        productBean.FPurchaseUnitID               = rs.getString("采购单位ID");
                        productBean.FPurchasePriceUnitID          = rs.getString("采购计价单位ID");
                        productBean.FSaleUnitID                   = rs.getString("销售单位ID");
                        productBean.FSalePriceUnitID              = rs.getString("销售计价单位ID");
                        productBean.FStoreUnitID                  = rs.getString("库存单位ID");
                        productBean.FAuxUnitID                    = rs.getString("辅助单位ID");
                        productBean.FStockID                      = rs.getString("默认仓库ID");
                        productBean.FStockPlaceID                 = rs.getString("默认仓位ID");
                        productBean.FIsBatchManage                = rs.getString("是否启用批号管理");
                        productBean.FIsKFperiod                   = rs.getString("是否开启保质期管理");
                        productBean.FExpperiod                    = rs.getString("保质期");
                        productBean.FExpUnit                      = rs.getString("保质期单位");
                        productBean.FIsPurchase                   = rs.getString("允许采购");
                        productBean.FIsSale                       = rs.getString("允许销售");
                        productBean.FIsInventory                  = rs.getString("允许库存");
                        productBean.FIsProduce                    = rs.getString("允许生产");
                        productBean.FIsSubContract                = rs.getString("允许委外");
                        productBean.FIsAsset                      = rs.getString("允许资产");
                        productBean.FBaseUnitID                   = rs.getString("基本单位ID");
                        productBean.FFWeightUnitID                = rs.getString("重量单位ID");
                        productBean.FVolumeUnitID                 = rs.getString("尺寸单位ID");
                        productBean.FBarcode                      = rs.getString("条码");
                        productBean.FGrossWeight                  = rs.getString("毛重");
                        productBean.FNetWeight                    = rs.getString("净重");
                        productBean.FLenght                       = rs.getString("长");
                        productBean.FWidth                        = rs.getString("宽");
                        productBean.FHeight                       = rs.getString("高");
                        productBean.FVolume                       = rs.getString("体积");
                        productBean.FMaterialid                   = rs.getString("物料内码");
                        productBean.FNumber                       = rs.getString("编码");
                        productBean.FoldNumber                    = rs.getString("旧物料编码");
                        productBean.FName                         = rs.getString("商品名称");
                        productBean.FModel                        = rs.getString("规格型号");
                        productBean.FMnemoniccode                 = rs.getString("助记码");
                        container.add(productBean);
                    }
                    downloadReturnBean.products = container;
                    response.getWriter().write(CommonJson.getCommonJson(true,gson.toJson(downloadReturnBean)));
                }else{
                    response.getWriter().write(CommonJson.getCommonJson(false,"未查询到数据"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write(CommonJson.getCommonJson(false,"数据库错误\r\n----------------\r\n错误原因:\r\n"+e.toString()));

            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

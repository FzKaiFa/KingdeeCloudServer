if (exists (select * from sys.objects where name = 'proc_K3RetPushSaleRet'))
drop proc proc_K3RetPushSaleRet
go
create proc proc_K3RetPushSaleRet
(
  @mainStr nvarchar(1000)--主表参数
 
)
as 
set nocount on     --开启一个模式，也就是不再刷新多少行受影响的信息，可以提高性能
set xact_abort on  --事务选项设置为出错全部回滚
begin tran declare
          @FDate varchar(20),            --单据日期   
          @FBillerID varchar(20),        --制单人ID 
          @FOrderID varchar(50),--PDA单据编号
          @FPDAID varchar(50),  --PDA序列号 
          @FInterID int,--单据内码
          @FBillNo varchar(128) --单据编号
        
          set @FBillerID=dbo.getString(@mainStr,'|',1)   -- 制单人ID 
          set @FBillNo=dbo.getString(@mainStr,'|',2)     --回单时返回的单据编号
          set @FOrderID=dbo.getString(@mainStr,'|',3)  --PDA单据编号
          set @FPDAID=dbo.getString(@mainStr,'|',4)      --PDA序号
          select @FInterID=FID from T_SAL_RETURNSTOCK where FBillNo=@FBillNo 
          set @Fdate = convert(varchar(20),GETDATE(),23)                                       
 
  update t_PDABarCodeSign  set  FStockID=t.FStockID,FStockPlaceID=t.FStockPlaceID,FInterIDIn=@FInterID,FDateInStore=@fdate,FIsInStore='已入库',FUserInStore=@FBillerID,FRemark1='退货通知单下推销售退货单'  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
  --update t_PDABarCodeSign  set FInterIDOut=@FInterID,FDateOutStore=@fdate,FIsOutStore='未出库',FUserOutStore=@FBillerID,FRemark2='退货通知单下推销售退货单',FQtyOut=isnull(FQtyOut,0)-t.FQty  from (select a.* from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID) as t where t_PDABarCodeSign.FBarCode=t.FBarCode
 --insert into t_PDABarCodeSign_Red(FBarCode,FInterID,FROB,FRemark,FInterID_Before,FBillNO,FStatus,FQtyOut)select a.FBarCode,@FInterID,0,'退货通知单下推销售退货单',b.FInterIDOut,@FBillNO,1,- a.FQty from a_DetailsTable a left join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID 
 insert into t_PDABarCodeSign_In(FInterID,FBarCode,FBillNo,FRemark,FStatus)select @FInterID,a.FBarCode,@FBillNo,'退货通知单下推销售退货单',1 from a_DetailsTable  a inner join t_PDABarCodeSign b on a.FBarCode=b.FBarCode where FOrderID=@FOrderID and FPDAID=@FPDAID
  delete from a_DetailsTable where FOrderID=@FOrderID and FPDAID=@FPDAID
   
commit tran 
return;
if(0<>@@error)
	goto error1
error1:
	rollback tran;
package com.irisking;
/**
 * 按接口文档《云平台算法服务接口》定义
 * 编译路径 /Users/yuantao/Project/playground/com/irisking/IKAServer.java
 * @author yuantao
 *
 */
public class IKAServer {
    /**
     * 向算法服务添加特征值
     * @author yuantao
     * @param feature 特征值结构的字节数组
     * @param app_id 应用id
     * @param eyeMode 眼睛模式 1 左眼 2 右眼
     * @return 特征值在算法服务中的序号(大于等于0), -1 表示特征值无法使用 -2 其他异常
     */
    public native long enrollFeature(byte[] feature, int app_id, int eyeMode);
    
    /**
     * 从算法服务中删除指定序号的特征值，会用算法服务中的某一个特征填补被删除特征的位置
     * 比如打算删除index=6的特征，本方法在json中返回了index=12，那么调用者需要把原来
     * index=12的特征值对应的index，在本地更新为index=6.
     * @author yuantao
     * @param app_id 应用id
     * @param eyeMode 眼睛模式 1 左眼 2 右眼
     * @param targetIndex
     * @return 用来补位的特征值原来的index(大于等于0), -1 异常
     */
    public native long[] removeFeature(int app_id,int eyeMode, long[] targetIndex);
    
    /**
     * 返回当前特征值占用的内存大小
     * @return 特征占用的内存开销，单位KB(大于等于0)
     */
    public native long featureStackSize();
    
    /**
     * 1:1 比对特征
     * @author yuantao
     * @param  feature     待比对的特征
     * @param  app_id      应用id
     * @param  eyeMode     眼睛模式 1 左眼 2 右眼
     * @param  targetIndex 样本特征的索引
     * @return 0 成功
     */
    public native int match(byte[]feature, int app_id, int eyeMode, Long[] targetIndex);
    
    /**
     * 1:1 比对特征
     * @author yuantao
     * @param feature       待比对特征
     * @param app_id        应用id
     * @param eyeMode       眼睛模式  0任意 1 左眼 2右眼
     * @param targetFeature 样本特征
     * @return 0 成功
     */
    public native int match(byte[]feature, int app_id, int eyeMode, byte[] targetFeature);
    
    /**
     * 1:n 匹配特征
     * @param feature 待检索特征
     * @param eyeMode 眼睛模式 1 左眼 2右眼
     * @return 0 成功
     */
    public native long idel(byte[]feature, int app_id, int eyeMode);
 
    /**
     * 装载特征值，算法服务启动的时候需要用到
     * @param feature
     * @param eytMode
     * @param featureCount
     * @return 特征对应的索引数组
     */
    public native Long[] loadFeature(byte[] feature, int eytMode, int featureCount);
}

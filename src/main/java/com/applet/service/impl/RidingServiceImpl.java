package com.applet.service.impl;

import com.applet.annotation.SystemServerLog;
import com.applet.entity.LockRequest.EndOrderRequest;
import com.applet.entity.LockRequest.QueryRidingStatusRequest;
import com.applet.entity.LockResponse.QueryRidingStatusResponse;
import com.applet.enums.ResultEnums;
import com.applet.mapper.TransRecordTempMapper;
import com.applet.mapper.UserInfoMapper;
import com.applet.model.FeedbackInfo;
import com.applet.model.TransRecordTemp;
import com.applet.model.UserInfo;
import com.applet.service.RidingService;
import com.applet.utils.AppletResult;
import com.applet.utils.ResultUtil;
import com.applet.utils.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.UUID;

@Service
public class RidingServiceImpl implements RidingService{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TransRecordTempMapper transRecordTempMapper;

    private static final Logger LOGGER= LoggerFactory.getLogger(RidingServiceImpl.class);

    @Override
    @SystemServerLog(funcionExplain = "查询骑行状态")
    public AppletResult queryRidingStatus(QueryRidingStatusRequest queryRidingStatusRequest){
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(queryRidingStatusRequest.getUserId());
        if(userInfo != null){
            QueryRidingStatusResponse queryRidingStatusResponse = new QueryRidingStatusResponse();
            queryRidingStatusResponse.setRidingFlag(userInfo.getmBorrowBicycle());
            Date currentTime = new Date();
            long ridingTime;
            if(currentTime.getTime() > userInfo.getmBorrowBicycleDate().getTime()){
                ridingTime = (currentTime.getTime() - userInfo.getmBorrowBicycleDate().getTime())/1000/60;
            }else{
                ridingTime = 0;
            }
            queryRidingStatusResponse.setRidingTime(ridingTime);
            queryRidingStatusResponse.setRidingCost(0);
            return ResultUtil.success(queryRidingStatusResponse);
        }else{
            return ResultUtil.error(ResultEnums.INVALID_USER);
        }
    }

    @Override
    @SystemServerLog(funcionExplain = "故障报修")
    public AppletResult endOrder(EndOrderRequest endOrderRequest){
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(endOrderRequest.getUserId());
        if(userInfo != null){
            String bicycleNo = CommonUtils.DecodeBarcode(endOrderRequest.getBarcode());
            if(!bicycleNo.equals("0")){
                String uuid = UUID.randomUUID().toString();
                FeedbackInfo feedbackInfo = new FeedbackInfo();
                feedbackInfo.setId(uuid);
                feedbackInfo.setAddtime(new Date());
                feedbackInfo.setBicycleNum(Integer.parseInt(bicycleNo));
                feedbackInfo.setDescription(endOrderRequest.getDescription());
                feedbackInfo.setPicurl(endOrderRequest.getPicurl());
                feedbackInfo.setSonType(endOrderRequest.getSonType());
                feedbackInfo.setType(endOrderRequest.getType());
                feedbackInfo.setUserId(endOrderRequest.getUserId());
                feedbackInfo.setPlatform(3);
                if(Integer.parseInt(endOrderRequest.getSonType()) == 1 && endOrderRequest.getType() == 2){
                    TransRecordTemp transRecordTemp = transRecordTempMapper.selectByUserIdAndTransFlag(endOrderRequest.getUserId());
                    if(transRecordTemp != null){
                        transRecordTemp.setUserId(endOrderRequest.getUserId());
                        transRecordTemp.setTransFlag(1);
                        transRecordTemp.setState(1);
                        transRecordTemp.setRecessionDateTime(new Date());
                        transRecordTempMapper.updateByUserIdAndBorrowFlag(transRecordTemp);
                        userInfo.setmBorrowBicycle(0);
                        userInfo.setId(endOrderRequest.getUserId());
                        userInfoMapper.updateBorrowFlagById(userInfo);
                        feedbackInfo.setTransId(transRecordTemp.getId());
                    }else{
                        return ResultUtil.error(ResultEnums.SCAVENING_UNLOCK_ERRORTRANSRECORD);
                    }
                }
                if(!CommonUtils.isEmptyString(endOrderRequest.getLongitude())){
                    feedbackInfo.setBikeLng(endOrderRequest.getLongitude());
                }
                if(!CommonUtils.isEmptyString(endOrderRequest.getLatitude())){
                    feedbackInfo.setBikeLat(endOrderRequest.getLatitude());
                }
                return ResultUtil.success();
            }else{
                return ResultUtil.error(ResultEnums.SCAVENING_UNLOCK_ERRORBARCODE);
            }
        }else{
            return ResultUtil.error(ResultEnums.INVALID_USER);
        }
    }
}

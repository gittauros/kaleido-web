package com.tauros.kaleido.web.controller.fitness;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tauros.kaleido.core.algorithm.fitness.FoodServ;
import com.tauros.kaleido.core.algorithm.fitness.FoodSet;
import com.tauros.kaleido.core.model.Result;
import com.tauros.kaleido.core.service.FitnessService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 2017/11/5.
 */
@Controller
@RequestMapping("fit")
public class FitController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private FitnessService fitnessService;

    @RequestMapping("food/index")
    public String foodIndex() {
        return "fitness/foodPlan";
    }

    @RequestMapping(value = "food/calculate", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> foodCalculate(JSONObject json) {
        Result<String> result = new Result<>();
        try {
            JSONArray foodListJSON = json.getJSONArray("foodList");
            JSONObject targetJSON = json.getJSONObject("target");
            String maxResultStr = json.getString("maxResult");
            String maxKickedCountStr = json.getString("maxKickedCount");

            int maxResult = 20;
            if (StringUtils.isNotBlank(maxResultStr)) {
                maxResult = Integer.parseInt(maxResultStr);
            }
            int maxKickedCount = 0;
            if (StringUtils.isNotBlank(maxKickedCountStr)) {
                maxKickedCount = Integer.parseInt(maxKickedCountStr);
            }
            FoodSet target = JSONObject.parseObject(targetJSON.toJSONString(), FoodSet.class);
            List<FoodServ> foodServList = new ArrayList<>();
            for (int i = 0; i < foodListJSON.size(); i++) {
                JSONObject object = foodListJSON.getJSONObject(i);
                foodServList.add(JSONObject.parseObject(object.toJSONString(), FoodServ.class));
            }

            String id = fitnessService.generateFoodPlanCalculator(foodServList, target, maxResult, maxKickedCount);
            fitnessService.asyncFoodPlanCalculatorSearch(id);

            result.setSuccess(true);
            result.setModel(id);
        } catch (Exception e) {
            logger.error("foodCalculate error", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "food/calculate/process")
    @ResponseBody
    public Result<String> foodCalculateProcess(String id) {
        Result<String> result = new Result<>();
        try {
            BigDecimal process = fitnessService.getFoodPlanCalculatorProcess(id);

            result.setSuccess(true);
            result.setModel(process.toPlainString() + "%");
        } catch (Exception e) {
            logger.error("foodCalculateProcess error", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "food/calculate/result")
    @ResponseBody
    public Result<JSONArray> foodCalculateResult(String id) {
        Result<JSONArray> result = new Result<>();
        try {
            JSONArray resultArray = fitnessService.getFoodPlanCalculatorResult(id);

            result.setSuccess(true);
            result.setModel(resultArray);
        } catch (Exception e) {
            logger.error("foodCalculateResult error", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}

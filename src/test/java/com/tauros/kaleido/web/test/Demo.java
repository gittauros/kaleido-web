package com.tauros.kaleido.web.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tauros on 2016/4/9.
 */
public class Demo implements Serializable {
    private static final long serialVersionUID = 7929884469842370895L;

    static class Food {
        private String     name;
        private BigDecimal protein;
        private BigDecimal fat;
        private BigDecimal carbohydrate;
        private BigDecimal calorie;
        private BigDecimal dietaryFiber;
        private BigDecimal firstServ;
        private BigDecimal perServ;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getProtein() {
            return protein;
        }

        public void setProtein(BigDecimal protein) {
            this.protein = protein;
        }

        public BigDecimal getFat() {
            return fat;
        }

        public void setFat(BigDecimal fat) {
            this.fat = fat;
        }

        public BigDecimal getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(BigDecimal carbohydrate) {
            this.carbohydrate = carbohydrate;
        }

        public BigDecimal getCalorie() {
            return calorie;
        }

        public void setCalorie(BigDecimal calorie) {
            this.calorie = calorie;
        }

        public BigDecimal getDietaryFiber() {
            return dietaryFiber;
        }

        public void setDietaryFiber(BigDecimal dietaryFiber) {
            this.dietaryFiber = dietaryFiber;
        }

        public BigDecimal getFirstServ() {
            return firstServ;
        }

        public void setFirstServ(BigDecimal firstServ) {
            this.firstServ = firstServ;
        }

        public BigDecimal getPerServ() {
            return perServ;
        }

        public void setPerServ(BigDecimal perServ) {
            this.perServ = perServ;
        }

        public Food obtain() {
            Food food = new Food();
            food.name = name;
            food.protein = protein;
            food.fat = fat;
            food.carbohydrate = carbohydrate;
            food.calorie = calorie;
            food.dietaryFiber = dietaryFiber;
            food.firstServ = firstServ;
            food.perServ = perServ;
            return food;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Food food = (Food) o;

            return name.equals(food.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    static class FoodSet {
        private final Map<String, BigDecimal> maxWeight = new HashMap<>();
        private final BigDecimal protein;
        private final BigDecimal fat;
        private final BigDecimal carbohydrate;
        private final BigDecimal calorie;
        private final BigDecimal factRatio;

        public FoodSet(BigDecimal protein, BigDecimal fat, BigDecimal carbohydrate, BigDecimal calorie, BigDecimal factRatio) {
            this.protein = protein;
            this.fat = fat;
            this.carbohydrate = carbohydrate;
            this.calorie = calorie;
            this.factRatio = factRatio;
            //TODO assert 热量5%误差
        }

        public static boolean isLegal(BigDecimal value, BigDecimal expect, double ratio) {
            BigDecimal max = expect.multiply(new BigDecimal(ratio));
            return max.compareTo(value) >= 0;
        }

        public static BigDecimal calorieCalculate(BigDecimal protein, BigDecimal fat, BigDecimal carbohydrate) {
            //TODO 校验
            return protein.add(carbohydrate).multiply(new BigDecimal(4)).add(fat.multiply(new BigDecimal(9)));
        }

        public BigDecimal getProtein() {
            return protein;
        }

        public BigDecimal getFat() {
            return fat;
        }

        public BigDecimal getCarbohydrate() {
            return carbohydrate;
        }

        public BigDecimal getCalorie() {
            return calorie;
        }

        public BigDecimal getFactRatio() {
            return factRatio;
        }

        public void setMaxWeight(String name, BigDecimal weight) {
            maxWeight.put(name, weight);
        }

        public BigDecimal getMaxWeight(String name) {
            return maxWeight.get(name);
        }
    }

    static class FoodPlan {
        private List<FoodNode> foodNodes;
        private BigDecimal     protein;
        private BigDecimal     fat;
        private BigDecimal     carbohydrate;
        private BigDecimal     calorie;
        private BigDecimal     dietaryFiber;
        private BigDecimal     fact;
        private BigDecimal     factRatio;
        private String         tag;

        public FoodPlan() {
            this.foodNodes = new ArrayList<>();
            this.calculate();
        }

        private void calculate() {
            this.protein = BigDecimal.ZERO;
            this.fat = BigDecimal.ZERO;
            this.carbohydrate = BigDecimal.ZERO;
            this.calorie = BigDecimal.ZERO;
            this.dietaryFiber = BigDecimal.ZERO;
            BigDecimal avaWeight = BigDecimal.ZERO;
            for (FoodNode foodNode : foodNodes) {
                avaWeight = avaWeight.add(foodNode.getWeight());
                this.protein = this.protein.add(foodNode.transWeight().multiply(foodNode.getFood().getProtein()));
                this.fat = this.fat.add(foodNode.transWeight().multiply(foodNode.getFood().getFat()));
                this.carbohydrate = this.carbohydrate.add(foodNode.transWeight().multiply(foodNode.getFood().getCarbohydrate()));
                this.calorie = this.calorie.add(foodNode.transWeight().multiply(foodNode.getFood().getCalorie()));
                this.dietaryFiber = this.dietaryFiber.add(foodNode.transWeight().multiply(foodNode.getFood().getDietaryFiber()));
            }
            if (this.foodNodes.size() > 1) {
                avaWeight = avaWeight.divide(new BigDecimal(foodNodes.size()), 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal fact = BigDecimal.ZERO;
                for (FoodNode foodNode : foodNodes) {
                    fact = fact.add(foodNode.getWeight().subtract(avaWeight).divide(new BigDecimal(100)).pow(2));
                }
                fact = fact.divide(new BigDecimal(foodNodes.size()), 4, BigDecimal.ROUND_HALF_UP);
                this.fact = fact;
            }
        }

        public int findFood(String name) {
            for (int i = 0; i < this.foodNodes.size(); i++) {
                if (this.foodNodes.get(i).getFood().getName().equals(name)) {
                    return i;
                }
            }
            return -1;
        }

        public boolean contains(String name) {
            return findFood(name) != -1;
        }

        public void addFood(Food food, BigDecimal weight) {
            int index = findFood(food.getName());
            if (index == -1) {
                this.foodNodes.add(new FoodNode(food, weight));
            } else {
                this.foodNodes.get(index).addWeight(weight);
            }
            this.calculate();
        }

        public FoodPlan obtain() {
            FoodPlan foodPlan = new FoodPlan();
            foodPlan.factRatio = this.factRatio;
            foodPlan.foodNodes = new ArrayList<>();
            for (int i = 0; i < this.foodNodes.size(); i++) {
                FoodNode foodNode = this.foodNodes.get(i);
                foodPlan.foodNodes.add(foodNode.obtain());
            }
            foodPlan.calculate();
            return foodPlan;
        }

        public BigDecimal calculateScore(FoodSet target) {
            this.calculate();
            boolean isLegal = FoodSet.isLegal(this.calorie, target.getCalorie(), 1.02);
            if (!isLegal) {
                return BigDecimal.ZERO;
            }
            BigDecimal score = BigDecimal.ZERO;
            score = score.add(calculateDistance(this.protein, target.getProtein(), 7))
                         .add(calculateDistance(this.calorie, target.getCalorie(), 8))
                         .add(calculateDistance(this.carbohydrate, target.getCarbohydrate(), 5))
                         .add(calculateDistance(this.fat, target.getFat(), 1));

            if (score.equals(BigDecimal.ZERO)) {
                score = new BigDecimal(9999);
            }
            score = new BigDecimal(1000).divide(score, 9, BigDecimal.ROUND_HALF_UP);
            if (this.fact != null) {
                score = score.subtract(this.fact.divide(this.factRatio, 4, BigDecimal.ROUND_HALF_UP));
            }
            double minDietary = 25;
            double targetCarbohydrate = target.getCarbohydrate().doubleValue();
            double dietaryFiberVal = this.dietaryFiber.doubleValue();
            double expectDietaryFiber = Math.max(minDietary, mid(mid((targetCarbohydrate / 4), minDietary), minDietary));
            double dietaryScore = expectDietaryFiber - (Math.abs(dietaryFiberVal - expectDietaryFiber));
            return score.add(new BigDecimal(dietaryScore / 10)).setScale(14, BigDecimal.ROUND_HALF_UP);
        }

        private double mid(double a, double b) {
            return (a + b) / 2;
        }

        private static BigDecimal calculateDistance(BigDecimal value, BigDecimal target, double weight) {
            boolean isLegal = FoodSet.isLegal(value, target, 1.05);
            if (!isLegal) {
                return new BigDecimal(99999999);
            }
            if (value.compareTo(target) >= 0) {
                return BigDecimal.ZERO;
            }
            return target.subtract(value).multiply(new BigDecimal(weight));
        }

        static class FoodNode {
            private Food       food;
            private BigDecimal weight;

            public FoodNode(Food food, BigDecimal weight) {
                this.food = food;
                this.weight = weight;
            }

            public BigDecimal transWeight() {
                return weight.divide(new BigDecimal(100));
            }

            public Food getFood() {
                return food;
            }

            public void setFood(Food food) {
                this.food = food;
            }

            public BigDecimal getWeight() {
                return weight;
            }

            public void setWeight(BigDecimal weight) {
                this.weight = weight;
            }

            public void addWeight(BigDecimal weight) {
                this.weight = this.weight.add(weight);
            }

            public FoodNode obtain() {
                return new FoodNode(this.food.obtain(), this.weight);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                FoodNode foodNode = (FoodNode) o;

                if (food != null ? !food.getName().equals(foodNode.food.getName()) : foodNode.food != null)
                    return false;
                return weight != null ? weight.equals(foodNode.weight) : foodNode.weight == null;
            }

            @Override
            public int hashCode() {
                int result = food != null ? food.hashCode() : 0;
                result = 31 * result + (weight != null ? weight.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "{\"" + food.getName() + "\":\"" + weight + "g\"}";
            }
        }

        public List<FoodNode> getFoodNodes() {
            return foodNodes;
        }

        public void setFoodNodes(List<FoodNode> foodNodes) {
            this.foodNodes = foodNodes;
        }

        public BigDecimal getProtein() {
            return protein;
        }

        public void setProtein(BigDecimal protein) {
            this.protein = protein;
        }

        public BigDecimal getFat() {
            return fat;
        }

        public void setFat(BigDecimal fat) {
            this.fat = fat;
        }

        public BigDecimal getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(BigDecimal carbohydrate) {
            this.carbohydrate = carbohydrate;
        }

        public BigDecimal getCalorie() {
            return calorie;
        }

        public void setCalorie(BigDecimal calorie) {
            this.calorie = calorie;
        }

        public BigDecimal getDietaryFiber() {
            return dietaryFiber;
        }

        public void setDietaryFiber(BigDecimal dietaryFiber) {
            this.dietaryFiber = dietaryFiber;
        }

        public BigDecimal getFactRatio() {
            return factRatio;
        }

        public void setFactRatio(BigDecimal factRatio) {
            this.factRatio = factRatio;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FoodPlan foodPlan = (FoodPlan) o;

            boolean p1 = true;
            for (FoodNode foodNode : foodNodes) {
                if (!((FoodPlan) o).foodNodes.contains(foodNode)) {
                    p1 = false;
                    break;
                }
            }
            boolean p2 = true;
            for (FoodNode foodNode : ((FoodPlan) o).foodNodes) {
                if (!foodNodes.contains(foodNode)) {
                    p2 = false;
                    break;
                }
            }
            return foodNodes != null ? p1 && p2 : foodPlan.foodNodes == null;
        }

        @Override
        public int hashCode() {
            return foodNodes != null ? foodNodes.hashCode() : 0;
        }

        @Override
        public String toString() {
            JSONObject object = new JSONObject();
            JSONArray nodeArray = new JSONArray();
            for (FoodNode foodNode : foodNodes) {
                nodeArray.add(JSONObject.parseObject(foodNode.toString()));
            }
            object.put("foodNodes", nodeArray);
            object.put("protein", protein.toPlainString());
            object.put("fat", fat.toPlainString());
            object.put("carbohydrate", carbohydrate.toPlainString());
            object.put("calorie", calorie.toPlainString());
            object.put("dietaryFiber", dietaryFiber.toPlainString());
            object.put("fact", fact.toPlainString());
            return object.toJSONString();
        }

        public String getTag() {
            if (this.tag != null) {
                return this.tag;
            }
            StringBuilder sb = new StringBuilder();
            this.foodNodes.sort(new Comparator<FoodNode>() {
                @Override
                public int compare(FoodNode o1, FoodNode o2) {
                    return o1.getFood().getName().hashCode() - o2.getFood().getName().hashCode();
                }
            });
            for (FoodNode foodNode : this.foodNodes) {
                sb.append(foodNode.getFood().getName())
                  .append(foodNode.getWeight());
            }
            this.tag = sb.toString();
            return this.tag;
        }
    }

    static class FoodCalculator {

        private List<Food>                foodList;
        private Set<String>               tagSet;
        private PriorityQueue<SearchNode> planQueue;
        private List<SearchNode>          resultList;
        private FoodSet                   target;
        private int                       maxResult;
        private int                       maxKickedCount;
        private BigDecimal                minCalorie;

        public FoodCalculator(List<Food> foodList, FoodSet target, int maxResult) {
            this.foodList = foodList;
            this.target = target;
            this.planQueue = new PriorityQueue<>(new Comparator<SearchNode>() {
                @Override
                public int compare(SearchNode o1, SearchNode o2) {
                    return o2.score.compareTo(o1.score);
                }
            });
            this.resultList = new ArrayList<>();
            this.tagSet = new TreeSet<>();
            this.maxResult = maxResult;
            this.minCalorie = BigDecimal.ZERO;
        }

        public void searchResult() {
            FoodPlan initPlan = new FoodPlan();
            initPlan.setFactRatio(target.getFactRatio());
            planQueue.add(new SearchNode(initPlan, calculateScore(initPlan)));
            SearchNode top;
            int kickedCount = 0;
            while (!planQueue.isEmpty()) {
                top = planQueue.poll();
                if (top == null) {
                    break;
                }
                boolean allIllegal = true;
                for (Food food : foodList) {
                    FoodPlan newPlan = top.foodPlan.obtain();
                    if (!newPlan.contains(food.getName())) {
                        newPlan.addFood(food, food.getFirstServ());
                    } else {
                        newPlan.addFood(food, food.getPerServ());
                    }
                    SearchNode newNode = new SearchNode(newPlan, calculateScore(newPlan));
                    if (!tagSet.contains(newPlan.getTag()) && !resultList.contains(newNode) && newNode.score.compareTo(BigDecimal.ZERO) >= 0 && isPlanLegal(newPlan)) {
                        planQueue.offer(newNode);
                        tagSet.add(newPlan.getTag());
                        allIllegal = false;
                    }
                }
                if (allIllegal && !planQueue.contains(top) && !resultList.contains(top)) {
                    if (this.minCalorie != null && top.getFoodPlan().getCalorie().compareTo(this.minCalorie) < 0) {
                        continue;
                    }
                    resultList.add(top);
                    resultList.sort(new Comparator<SearchNode>() {
                        @Override
                        public int compare(SearchNode o1, SearchNode o2) {
                            return o2.score.compareTo(o1.score);
                        }
                    });
                    if (resultList.size() > maxResult) {
                        resultList.remove(resultList.size() - 1);
                        kickedCount++;
                        if (maxKickedCount <= 0 || kickedCount > maxKickedCount) {
                            break;
                        }
                    }
                }
            }
        }

        public void clear() {
            this.planQueue.clear();
            this.tagSet.clear();
            this.resultList.clear();
        }

        private boolean isPlanLegal(FoodPlan foodPlan) {
            boolean p1 = FoodSet.isLegal(foodPlan.getCalorie(), target.getCalorie(), 1.02) &&
                         FoodSet.isLegal(foodPlan.getProtein(), target.getProtein(), 1.05) &&
                         FoodSet.isLegal(foodPlan.getFat(), target.getFat(), 1) &&
                         FoodSet.isLegal(foodPlan.getCarbohydrate(), target.getCarbohydrate(), 1.02);
            boolean p2 = true;
            for (FoodPlan.FoodNode foodNode : foodPlan.getFoodNodes()) {
                BigDecimal maxWeight = target.getMaxWeight(foodNode.getFood().getName());
                if (maxWeight != null && foodNode.getWeight().compareTo(maxWeight) > 0) {
                    p2 = false;
                    break;
                }
            }
            return p1 && p2;
        }

        private BigDecimal calculateScore(FoodPlan foodPlan) {
            return foodPlan.calculateScore(this.target);
        }

        private static class SearchNode {
            private FoodPlan   foodPlan;
            private BigDecimal score;

            public SearchNode(FoodPlan foodPlan, BigDecimal score) {
                this.foodPlan = foodPlan;
                this.score = score;
            }

            public FoodPlan getFoodPlan() {
                return foodPlan;
            }

            public BigDecimal getScore() {
                return score;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                SearchNode that = (SearchNode) o;

                if (foodPlan != null ? !foodPlan.equals(that.foodPlan) : that.foodPlan != null) return false;
                return score != null ? score.equals(that.score) : that.score == null;
            }

            @Override
            public int hashCode() {
                int result = foodPlan != null ? foodPlan.hashCode() : 0;
                result = 31 * result + (score != null ? score.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                JSONObject object = new JSONObject();
                object.put("foodPlan", JSONObject.parseObject(foodPlan.toString()));
                object.put("score", score.toPlainString());
                return object.toJSONString();
            }
        }

        public int getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(int maxResult) {
            this.maxResult = maxResult;
        }

        public int getMaxKickedCount() {
            return maxKickedCount;
        }

        public void setMaxKickedCount(int maxKickedCount) {
            this.maxKickedCount = maxKickedCount;
        }

        public FoodSet getTarget() {
            return target;
        }

        public void setTarget(FoodSet target) {
            this.target = target;
        }

        public List<Food> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<Food> foodList) {
            this.foodList = foodList;
        }

        public List<SearchNode> getResultList() {
            return resultList;
        }

        public JSONArray getResultJSONArray() {
            JSONArray resultArray = new JSONArray();
            for (FoodCalculator.SearchNode node : resultList) {
                resultArray.add(JSON.parseObject(node.toString()));
            }
            return resultArray;
        }

        public String getResultString() {
            return getResultJSONArray().toJSONString();
        }

        public BigDecimal getMinCalorie() {
            return minCalorie;
        }

        public void setMinCalorie(BigDecimal minCalorie) {
            this.minCalorie = minCalorie;
        }
    }

    public static void main(String[] args) {
        String str = "[{\"food\":{\"protein\":\"6.5\",\"carbohydrate\":\"46.3\",\"fat\":\"2.1\",\"name\":\"辣椒酱\",\"calorie\":\"232\",\"dietaryFiber\":\"0\"},\"firstServ\":\"5\",\"perServ\":\"5\"},{\"food\":{\"protein\":\"8\",\"carbohydrate\":\"76.8\",\"fat\":\"2.9\",\"name\":\"糙米\",\"calorie\":\"372.4\",\"dietaryFiber\":\"3.2\"},\"firstServ\":\"60\",\"perServ\":\"5\"},{\"food\":{\"protein\":\"8.4\",\"carbohydrate\":\"77.2\",\"fat\":\"0.7\",\"name\":\"白米\",\"calorie\":\"335\",\"dietaryFiber\":\"3.5\"},\"firstServ\":\"60\",\"perServ\":\"5\"},{\"food\":{\"protein\":\"19.9\",\"carbohydrate\":\"2\",\"fat\":\"4.2\",\"name\":\"牛肉\",\"calorie\":\"125\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"9.3\",\"carbohydrate\":\"0\",\"fat\":\"59\",\"name\":\"猪五花肉\",\"calorie\":\"568\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"17\",\"carbohydrate\":\"0\",\"fat\":\"28\",\"name\":\"猪后肘\",\"calorie\":\"320\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"20.3\",\"carbohydrate\":\"1.5\",\"fat\":\"6.2\",\"name\":\"猪瘦肉\",\"calorie\":\"143\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"20.2\",\"carbohydrate\":\"0.7\",\"fat\":\"7.9\",\"name\":\"猪里脊\",\"calorie\":\"155\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"19.4\",\"carbohydrate\":\"2.5\",\"fat\":\"5\",\"name\":\"鸡胸肉\",\"calorie\":\"133\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"10.4\",\"carbohydrate\":\"0\",\"fat\":\"0.7\",\"name\":\"虾仁\",\"calorie\":\"48\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"20.4\",\"carbohydrate\":\"0.5\",\"fat\":\"0.5\",\"name\":\"鳕鱼\",\"calorie\":\"88\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"17.2\",\"carbohydrate\":\"0\",\"fat\":\"7.8\",\"name\":\"三文鱼\",\"calorie\":\"139\",\"dietaryFiber\":\"0\"},\"firstServ\":\"50\",\"perServ\":\"25\"},{\"food\":{\"protein\":\"4.1\",\"carbohydrate\":\"2.7\",\"fat\":\"0.6\",\"name\":\"西兰花\",\"calorie\":\"36\",\"dietaryFiber\":\"1.6\"},\"firstServ\":\"100\",\"perServ\":\"50\"},{\"food\":{\"protein\":\"1.2\",\"carbohydrate\":\"3.3\",\"fat\":\"0.2\",\"name\":\"芹菜茎\",\"calorie\":\"22\",\"dietaryFiber\":\"1.2\"},\"firstServ\":\"100\",\"perServ\":\"50\"},{\"food\":{\"protein\":\"2.7\",\"carbohydrate\":\"2.6\",\"fat\":\"0.2\",\"name\":\"毛毛菜\",\"calorie\":\"23\",\"dietaryFiber\":\"2.1\"},\"firstServ\":\"100\",\"perServ\":\"50\"},{\"food\":{\"protein\":\"1.6\",\"carbohydrate\":\"2.8\",\"fat\":\"0.3\",\"name\":\"木耳菜\",\"calorie\":\"23\",\"dietaryFiber\":\"1.5\"},\"firstServ\":\"100\",\"perServ\":\"50\"},{\"food\":{\"protein\":\"1.1\",\"carbohydrate\":\"8.1\",\"fat\":\"0.2\",\"name\":\"洋葱\",\"calorie\":\"40\",\"dietaryFiber\":\"0.9\"},\"firstServ\":\"100\",\"perServ\":\"50\"}]";
        JSONArray array = JSONArray.parseArray(str);
        List<Food> foodList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject foodJSON = object.getJSONObject("food");
            Food food = new Food();
            food.setName(foodJSON.getString("name"));
            food.setProtein(new BigDecimal(foodJSON.getString("protein")));
            food.setCarbohydrate(new BigDecimal(foodJSON.getString("carbohydrate")));
            food.setFat(new BigDecimal(foodJSON.getString("fat")));
            food.setCalorie(new BigDecimal(foodJSON.getString("calorie")));
            food.setDietaryFiber(new BigDecimal(foodJSON.getString("dietaryFiber")));
            food.setFirstServ(new BigDecimal(object.getString("firstServ")));
            food.setPerServ(new BigDecimal(object.getString("perServ")));
            int[] selection = new int[]{1, 2, 4, 6, 7, 8, 16};
            for (int j = 0; j < selection.length; j++) {
                if (selection[j] == i) {
                    foodList.add(food);
                    break;
                }
            }
        }
        FoodSet foodSet = new FoodSet(new BigDecimal(46), new BigDecimal(13), new BigDecimal(77),
                                      new BigDecimal(610), new BigDecimal(2));
        foodSet.setMaxWeight("芹菜茎", new BigDecimal(500));
        foodSet.setMaxWeight("毛毛菜", new BigDecimal(1000));
        foodSet.setMaxWeight("木耳菜", new BigDecimal(500));
        foodSet.setMaxWeight("西兰花", new BigDecimal(500));
        foodSet.setMaxWeight("糙米", new BigDecimal(150));
        foodSet.setMaxWeight("白米", new BigDecimal(150));
        foodSet.setMaxWeight("辣椒酱", new BigDecimal(20));
        foodSet.setMaxWeight("洋葱", new BigDecimal(500));
        FoodCalculator calculator = new FoodCalculator(foodList, foodSet, 30);
        calculator.setMinCalorie(new BigDecimal(400));
        calculator.setMaxKickedCount(10000);
        long time = System.currentTimeMillis();
        calculator.searchResult();
        System.out.println(System.currentTimeMillis() - time);
        List<FoodCalculator.SearchNode> resultList = calculator.getResultList();
        resultList.sort(new Comparator<FoodCalculator.SearchNode>() {
            @Override
            public int compare(FoodCalculator.SearchNode o1, FoodCalculator.SearchNode o2) {
                return o2.getFoodPlan().getDietaryFiber().compareTo(o1.getFoodPlan().getDietaryFiber());
            }
        });
        JSONArray resultArray = new JSONArray();
        for (FoodCalculator.SearchNode node : resultList) {
            resultArray.add(JSON.parseObject(node.toString()));
        }
        System.out.println(resultArray.toJSONString());
    }
}

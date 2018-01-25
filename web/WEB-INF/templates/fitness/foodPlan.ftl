<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>food plan</title>
    <script src="${(contextPath)!}/js/jquery.min.js"></script>
    <style>
        html, body, p {
            font-family: sans-serif;
        }
    </style>
</head>
<body style="margin: 0; padding: 0 0;">
<div style="width: 100%; height: auto; position: fixed; background-color: white; z-index: 99;">

</div>
</body>
<script>

    $(function () {
        function findFood(name) {
            for (var i = 0; i < foodData.length; i++) {
                if (foodData[i].food.name === name) {
                    return foodData[i];
                }
            }
            return null;
        }



    });

    var foodData = [{
        "food": {
            "protein": "6.5",
            "carbohydrate": "46.3",
            "fat": "2.1",
            "name": "辣椒酱",
            "calorie": "232",
            "dietaryFiber": "0"
        }, "firstServ": "5", "perServ": "5"
    }, {
        "food": {
            "protein": "8",
            "carbohydrate": "76.8",
            "fat": "2.9",
            "name": "糙米",
            "calorie": "372.4",
            "dietaryFiber": "3.2"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "8.4",
            "carbohydrate": "77.2",
            "fat": "0.7",
            "name": "白米",
            "calorie": "335",
            "dietaryFiber": "3.5"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "19.9",
            "carbohydrate": "2",
            "fat": "4.2",
            "name": "牛肉",
            "calorie": "125",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "9.3",
            "carbohydrate": "0",
            "fat": "59",
            "name": "猪五花肉",
            "calorie": "568",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "17",
            "carbohydrate": "0",
            "fat": "28",
            "name": "猪后肘",
            "calorie": "320",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "20.3",
            "carbohydrate": "1.5",
            "fat": "6.2",
            "name": "猪瘦肉",
            "calorie": "143",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "20.2",
            "carbohydrate": "0.7",
            "fat": "7.9",
            "name": "猪里脊",
            "calorie": "155",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "19.4",
            "carbohydrate": "2.5",
            "fat": "5",
            "name": "鸡胸肉",
            "calorie": "133",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "10.4",
            "carbohydrate": "0",
            "fat": "0.7",
            "name": "虾仁",
            "calorie": "48",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "20.4",
            "carbohydrate": "0.5",
            "fat": "0.5",
            "name": "鳕鱼",
            "calorie": "88",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "17.2",
            "carbohydrate": "0",
            "fat": "7.8",
            "name": "三文鱼",
            "calorie": "139",
            "dietaryFiber": "0"
        }, "firstServ": "50", "perServ": "25"
    }, {
        "food": {
            "protein": "4.1",
            "carbohydrate": "2.7",
            "fat": "0.6",
            "name": "西兰花",
            "calorie": "36",
            "dietaryFiber": "1.6"
        }, "firstServ": "100", "perServ": "50"
    }, {
        "food": {
            "protein": "1.2",
            "carbohydrate": "3.3",
            "fat": "0.2",
            "name": "芹菜茎",
            "calorie": "22",
            "dietaryFiber": "1.2"
        }, "firstServ": "100", "perServ": "50"
    }, {
        "food": {
            "protein": "2.7",
            "carbohydrate": "2.6",
            "fat": "0.2",
            "name": "毛毛菜",
            "calorie": "23",
            "dietaryFiber": "2.1"
        }, "firstServ": "100", "perServ": "50"
    }, {
        "food": {
            "protein": "1.6",
            "carbohydrate": "2.8",
            "fat": "0.3",
            "name": "木耳菜",
            "calorie": "23",
            "dietaryFiber": "1.5"
        }, "firstServ": "100", "perServ": "50"
    }, {
        "food": {
            "protein": "1.1",
            "carbohydrate": "8.1",
            "fat": "0.2",
            "name": "洋葱",
            "calorie": "40",
            "dietaryFiber": "0.9"
        }, "firstServ": "100", "perServ": "50"
    }];
</script>
</html>
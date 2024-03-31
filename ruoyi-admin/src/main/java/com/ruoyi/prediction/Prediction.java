package com.ruoyi.prediction;

import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class Prediction {

    /**
     * 预测下一周每天的累计会员数。
     *
     * @param lastWeekMembers 上一周每天的累计会员数列表。
     * @return 下一周每天的累计会员数预测列表。
     * @throws Exception 如果模型训练或预测过程中出现错误。
     */
    public static List<Integer> predictNextWeek(List<Integer> lastWeekMembers) throws Exception {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("previousDayMembers"));
        attributes.add(new Attribute("currentDayMembers"));

        // 创建Instances对象，准备数据
        Instances dataset = new Instances("DailyMembership", attributes, lastWeekMembers.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // 将数据添加到Instances对象，跳过第一项，因为它没有前一天的数据
        for (int i = 1; i < lastWeekMembers.size(); i++) {
            double[] instanceValue = new double[]{
                    lastWeekMembers.get(i - 1),
                    lastWeekMembers.get(i)
            };
            dataset.add(new DenseInstance(1.0, instanceValue));
        }

        // 使用线性回归模型
        LinearRegression model = new LinearRegression();
        model.buildClassifier(dataset); // 训练模型

        List<Integer> predictions = new ArrayList<>();
        double predictionForNextDay = lastWeekMembers.get(lastWeekMembers.size() - 1);
        for (int i = 0; i < 7; i++) {
            DenseInstance nextDayInstance = new DenseInstance(2);
            nextDayInstance.setValue(attributes.get(0), predictionForNextDay);
            nextDayInstance.setDataset(dataset);
            predictionForNextDay = model.classifyInstance(nextDayInstance);
            predictions.add((int) Math.round(predictionForNextDay));
        }

        return predictions;
    }

    public static void main(String[] args) throws Exception {
        // 上一周每天的累计会员数
        List<Integer> lastWeekMembers = new ArrayList<>();
        lastWeekMembers.add(2);
        lastWeekMembers.add(2);
        lastWeekMembers.add(2);
        lastWeekMembers.add(5);
        lastWeekMembers.add(5);
        lastWeekMembers.add(6);
        lastWeekMembers.add(7);
        List<Integer> nextWeekMembers = predictNextWeek(lastWeekMembers);
        System.out.println("预测下一周每天的累计数据: " + nextWeekMembers);
    }
}

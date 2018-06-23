package com.tauros.kaleido.web.test;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.apache.poi.ss.usermodel.CellType.*;

/**
 * Created by zhy on 2018/1/25.
 */
public class FitProgramDemo {

    private static final Map<String, Map<String, Map<String, Object>>> VALIDATIONS             = new HashMap<>();
    private static final String                                        filePath                = "/Users/tauros/Downloads/力量训练计划753.xlsx";
    private static final String                                        filePath2               = "/Users/tauros/Downloads/GZCLP+BOTB训练计划.xlsx";
    private static final int                                           BENCH_BASE              = 0;
    private static final int                                           SQUAT_BASE              = 4;
    private static final Map<Training, String>                         TRAINING_MAX_POSITION   = new HashMap<>();
    private static final Training[]                                    HIGH_FREQUENCY_SCHEDULE = new Training[]{Training.卧推, Training.硬拉, null, Training.直立杠铃推举, Training.深蹲, null};
    private static final Training[][]                                  TRAINING_SEQUENCE       = new Training[][]{{Training.卧推, Training.硬拉, Training.直立杠铃推举, Training.深蹲},
                                                                                                                  {Training.深蹲, Training.卧推, Training.硬拉, Training.直立杠铃推举}};
    private static final Integer[][]                                   MODE_DAYS_SEQUENCE      = new Integer[][]{{0, 1, null, 2, null, 3, null},
                                                                                                                 {0, 1, null, 2, 3, null, null},
                                                                                                                 {0, null, 1, null, 2, 3, null}};


    private static final TrainingSupport.SupportCalculator SECOND_SUPPORT_CALCULATOR = new TrainingSupport.SupportCalculator() {
        @Override
        public TrainingParam calculate(TrainingParam mainParam) {
            switch (mainParam.getTraining()) {
                case 直立杠铃推举:
                    return new TrainingParam(Training.卧推, calSets(mainParam.getTraining(), mainParam.getSets()), calReps(mainParam.getTraining(), mainParam.getSets(), mainParam.getPeriod()), calWeight(mainParam.getWeight(), mainParam.getPeriod()), mainParam.getPeriod());
                case 深蹲:
                    return new TrainingParam(Training.硬拉, calSets(mainParam.getTraining(), mainParam.getSets()), calReps(mainParam.getTraining(), mainParam.getSets(), mainParam.getPeriod()), calWeight(mainParam.getWeight(), mainParam.getPeriod()), mainParam.getPeriod());
                case 卧推:
                    return new TrainingParam(Training.直立杠铃推举, calSets(mainParam.getTraining(), mainParam.getSets()), calReps(mainParam.getTraining(), mainParam.getSets(), mainParam.getPeriod()), calWeight(mainParam.getWeight(), mainParam.getPeriod()), mainParam.getPeriod());
                case 硬拉:
                    return new TrainingParam(Training.深蹲, calSets(mainParam.getTraining(), mainParam.getSets()), calReps(mainParam.getTraining(), mainParam.getSets(), mainParam.getPeriod()), calWeight(mainParam.getWeight(), mainParam.getPeriod()), mainParam.getPeriod());
            }
            return null;
        }

        private String calWeight(String mainWeight, int period) {
            BigDecimal mainWeightNum = new BigDecimal(mainWeight);
            if (mainWeightNum.compareTo(new BigDecimal("0.65")) > 0) {
                switch (period) {
                    case 1:
                        return "0.65";
                    case 2:
                        return "0.66";
                    case 3:
                        return "0.67";
                    case 4:
                        return "0.68";
                    case 5:
                        return "0.60";
                }
                return "0.65";
            }
            return "0.60";
        }

        private int calSets(Training mainTraining, int mainSets) {
            boolean lowerBody = mainTraining == Training.深蹲 || mainTraining == Training.硬拉;
            switch (mainSets) {
                case 4:
                    return lowerBody ? 4 : 5;
                case 5:
                    return lowerBody ? 3 : 4;
                case 7:
                    return lowerBody ? 2 : 3;
                case 10:
                    return lowerBody ? 0 : 2;
            }
            return 0;
        }

        private int calReps(Training mainTraining, int mainSets, int period) {
            if (calSets(mainTraining, mainSets) <= 0) {
                return 0;
            }
            switch (period) {
                case 1:
                    return 9;
                case 2:
                    return 9;
                case 3:
                    return 8;
                case 4:
                    return 8;
                case 5:
                    return 7;
            }
            return 10;
        }
    };
    private static final List<List<Map<Training, TrainingParam>>>            CYCLE_LIST                   = new ArrayList<List<Map<Training, TrainingParam>>>() {{
        add(new ArrayList<Map<Training, TrainingParam>>(){{
            Map<Training, TrainingParam> loop1 = new HashMap<>();
            loop1.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 4, 9, "0.70", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.深蹲, new TrainingParam(Training.深蹲, 4, 9, "0.70", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.卧推, new TrainingParam(Training.卧推, 4, 9, "0.70", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.硬拉, new TrainingParam(Training.硬拉, 4, 9, "0.70", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop2 = new HashMap<>();
            loop2.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 5, 7, "0.75", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.深蹲, new TrainingParam(Training.深蹲, 5, 7, "0.75", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.卧推, new TrainingParam(Training.卧推, 5, 7, "0.75", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.硬拉, new TrainingParam(Training.硬拉, 5, 7, "0.75", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop3 = new HashMap<>();
            loop3.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 7, 5, "0.80", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.深蹲, new TrainingParam(Training.深蹲, 7, 5, "0.80", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.卧推, new TrainingParam(Training.卧推, 7, 5, "0.80", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.硬拉, new TrainingParam(Training.硬拉, 7, 5, "0.80", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop4 = new HashMap<>();
            loop4.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 10, 3, "0.85", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.深蹲, new TrainingParam(Training.深蹲, 10, 3, "0.85", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.卧推, new TrainingParam(Training.卧推, 10, 3, "0.85", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.硬拉, new TrainingParam(Training.硬拉, 10, 3, "0.85", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop5 = new HashMap<>();
            loop5.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.深蹲, new TrainingParam(Training.深蹲, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.卧推, new TrainingParam(Training.卧推, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.硬拉, new TrainingParam(Training.硬拉, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            add(loop1);
            add(loop2);
            add(loop3);
            add(loop4);
            add(loop5);
        }});
        add(new ArrayList<Map<Training, TrainingParam>>(){{
            Map<Training, TrainingParam> loop1 = new HashMap<>();
            loop1.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 5, 7, "0.75", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.深蹲, new TrainingParam(Training.深蹲, 10, 3, "0.85", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.卧推, new TrainingParam(Training.卧推, 7, 5, "0.80", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop1.put(Training.硬拉, new TrainingParam(Training.硬拉, 4, 9, "0.70", 1, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop2 = new HashMap<>();
            loop2.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 4, 9, "0.70", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.深蹲, new TrainingParam(Training.深蹲, 7, 5, "0.80", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.卧推, new TrainingParam(Training.卧推, 10, 3, "0.85", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop2.put(Training.硬拉, new TrainingParam(Training.硬拉, 5, 7, "0.75", 2, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop3 = new HashMap<>();
            loop3.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 7, 5, "0.80", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.深蹲, new TrainingParam(Training.深蹲, 4, 9, "0.70", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.卧推, new TrainingParam(Training.卧推, 5, 7, "0.75", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop3.put(Training.硬拉, new TrainingParam(Training.硬拉, 10, 3, "0.85", 3, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop4 = new HashMap<>();
            loop4.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 10, 3, "0.85", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.深蹲, new TrainingParam(Training.深蹲, 5, 7, "0.75", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.卧推, new TrainingParam(Training.卧推, 4, 9, "0.70", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop4.put(Training.硬拉, new TrainingParam(Training.硬拉, 7, 5, "0.80", 4, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            Map<Training, TrainingParam> loop5 = new HashMap<>();
            loop5.put(Training.直立杠铃推举, new TrainingParam(Training.直立杠铃推举, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.深蹲, new TrainingParam(Training.深蹲, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.卧推, new TrainingParam(Training.卧推, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            loop5.put(Training.硬拉, new TrainingParam(Training.硬拉, 4, 8, "0.60", 5, new TrainingSupport(SECOND_SUPPORT_CALCULATOR)));
            add(loop1);
            add(loop2);
            add(loop3);
            add(loop4);
            add(loop5);
        }});
    }};

    interface CycleCalculator {

        Training getTraining(int index, int loops, Training training);

        String getWeight(int index, int loops, Training training);

        int getSets(int index, int loops, Training training);

        int getReps(int index, int loops, Training training);
    }

    enum CycleCalculatorInstance implements CycleCalculator {

        FIRST_TRAIN(new CycleCalculator() {
            @Override
            public Training getTraining(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getTraining();
            }

            @Override
            public String getWeight(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getWeight();
            }

            @Override
            public int getSets(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getSets();
            }

            @Override
            public int getReps(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getReps();
            }
        }),
        SECOND_TRAIN(new CycleCalculator() {
            @Override
            public Training getTraining(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getTrainingSupport().getTrainingParam().getTraining();
            }

            @Override
            public String getWeight(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getTrainingSupport().getTrainingParam().getWeight();
            }

            @Override
            public int getSets(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getTrainingSupport().getTrainingParam().getSets();
            }

            @Override
            public int getReps(int index, int loops, Training training) {
                return CYCLE_LIST.get(index).get(loops).get(training).getTrainingSupport().getTrainingParam().getReps();
            }
        });

        private CycleCalculator calculator;

        CycleCalculatorInstance(CycleCalculator calculator) {
            this.calculator = calculator;
        }

        @Override
        public Training getTraining(int index, int loops, Training training) {
            return this.calculator.getTraining(index, loops, training);
        }

        @Override
        public String getWeight(int index, int loops, Training training) {
            return this.calculator.getWeight(index, loops, training);
        }

        @Override
        public int getSets(int index, int loops, Training training) {
            return this.calculator.getSets(index, loops, training);
        }

        @Override
        public int getReps(int index, int loops, Training training) {
            return this.calculator.getReps(index, loops, training);
        }
    }

    public static void main(String[] args) throws Exception {
        if (1 + 1 == 3) {
            XSSFWorkbook workbook = new XSSFWorkbook(new File(filePath2));
            XSSFCell cell = workbook.getSheetAt(1).getRow(12).getCell(1);
            System.out.println(cell.getRawValue());
            System.out.println(cell.getCellTypeEnum());
            System.out.println(cell.getDateCellValue());
            System.out.println(cell.getCellFormula());
            System.out.println(cell.getCellStyle());
//            List<XSSFDataValidation> validations = workbook.getSheetAt(1).getDataValidations();
//            for (XSSFDataValidation validation : validations) {
//                System.out.println("---------");
//                System.out.println("*********");
//                CellRangeAddressList rangeAddressList = validation.getRegions();
//                for (int i = 0; i < rangeAddressList.countRanges(); i++) {
//                    CellRangeAddress rangeAddress = rangeAddressList.getCellRangeAddress(i);
//                    System.out.print("range" + (i + 1) + ":");
//                    System.out.println(rangeAddress.formatAsString());
//                }
//                System.out.println("*********");
//                XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) validation.getValidationConstraint();
//                System.out.println(JSON.toJSONString(constraint.getExplicitListValues()));
//                System.out.println(constraint.getValidationType());
//                System.out.println(constraint.getOperator());
//                System.out.println(constraint.getFormula1());
//                System.out.println(constraint.getFormula2());
//            }
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFFont baseFont = workbook.createFont();
        baseFont.setFontHeight(14);
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeight(14);

        XSSFSheet sheet1 = workbook.createSheet("753 Program");
        for (int i = 0; i < 20; i++) {
            sheet1.setColumnWidth(i, 18 * 256);
        }

        XSSFRow row1 = sheet1.createRow(0);
        XSSFCellStyle row1Style1 = workbook.createCellStyle();
        row1Style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        row1Style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        row1Style1.setBorderLeft(BorderStyle.THIN);
        row1Style1.setBorderRight(BorderStyle.THIN);
        row1Style1.setBorderTop(BorderStyle.THIN);
        row1Style1.setBorderBottom(BorderStyle.THIN);
        row1Style1.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setRightBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setTopBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setAlignment(HorizontalAlignment.CENTER);
        row1Style1.setVerticalAlignment(VerticalAlignment.CENTER);
        row1Style1.setFont(baseFont);

        XSSFCell cellA1 = row1.createCell(0);
        modifyCell(cellA1, new CellOption().setStyle(row1Style1).setValue("单位"));
        XSSFCell cellB1 = row1.createCell(1);
        modifyCell(cellB1, new CellOption().setStyle(row1Style1).setValue("训练安排"));
        XSSFCell cellC1 = row1.createCell(2);
        modifyCell(cellC1, new CellOption().setStyle(row1Style1).setValue("训练模式"));
        XSSFCell cellD1 = row1.createCell(3);
        modifyCell(cellD1, new CellOption().setStyle(row1Style1).setValue("增重(上/下肢)"));
        XSSFCell cellE1 = row1.createCell(4);
        modifyCell(cellE1, new CellOption().setStyle(row1Style1).setValue("周期"));
        XSSFCell cellF1 = row1.createCell(5);
        modifyCell(cellF1, new CellOption().setStyle(row1Style1).setValue("起始日期"));
        XSSFCell cellG1 = row1.createCell(6);
        modifyCell(cellG1, new CellOption().setStyle(row1Style1).setValue("已跳过deload次数"));
        XSSFCell cellH1 = row1.createCell(7);
        modifyCell(cellH1, new CellOption().setStyle(row1Style1).setValue("本次deload跳过"));

        XSSFRow row2 = sheet1.createRow(1);
        CellStyle row2Style1 = workbook.createCellStyle();
        row2Style1.cloneStyleFrom(row1Style1);
        row2Style1.setFont(boldFont);
        row2Style1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        CellStyle row2Style2 = workbook.createCellStyle();
        row2Style2.cloneStyleFrom(row1Style1);
        row2Style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        CellStyle row2Style3 = sheet1.getWorkbook().createCellStyle();
        row2Style3.cloneStyleFrom(row2Style1);
        row2Style3.setDataFormat(workbook.createDataFormat().getFormat("m/d/yy"));

        XSSFCell cellA2 = row2.createCell(0);
        modifyCell(cellA2, new CellOption().setStyle(row2Style1).setEnumClass(WeightUnit.class).setValue(WeightUnit.kg.name()));
        XSSFCell cellB2 = row2.createCell(1);
        modifyCell(cellB2, new CellOption().setStyle(row2Style1).setEnumClass(TrainingSchedule.class).setValue(TrainingSchedule._12x3x4x.name()));
        XSSFCell cellC2 = row2.createCell(2);
        modifyCell(cellC2, new CellOption().setStyle(row2Style1).setEnumClass(TrainingMode.class).setValue(TrainingMode.Normal.name()));
        XSSFCell cellD2 = row2.createCell(3);
        modifyCell(cellD2, new CellOption().setStyle(row2Style2).setType(FORMULA).setValue("IF(A2=\"kg\", \"2.5/5\", \"5/10\")"));
        XSSFCell cellE2 = row2.createCell(4);
        modifyCell(cellE2, new CellOption().setStyle(row2Style1).setType(NUMERIC).setValue("1"));
        XSSFCell cellF2 = row2.createCell(5);
        modifyCell(cellF2, new CellOption().setStyle(row2Style3).setValue("2018/6/5"));
        XSSFCell cellG2 = row2.createCell(6);
        modifyCell(cellG2, new CellOption().setStyle(row2Style1).setType(NUMERIC).setValue("0"));
        XSSFCell cellH2 = row2.createCell(7);
        modifyCell(cellH2, new CellOption().setStyle(row2Style1).setEnumClass(YorN.class).setValue(YorN.N.name()));

        String periodCell = "E2";
        String startDateCell = "F2";
        String deloadSkipCell = "G2";
        String thisCycleDeloadSkipCell = "H2";

        XSSFRow row3 = sheet1.createRow(2);
        CellStyle row3Style = workbook.createCellStyle();
        row3Style.cloneStyleFrom(row1Style1);

        XSSFCell cellA3 = row3.createCell(0);
        modifyCell(cellA3, new CellOption().setStyle(row3Style).setValue("锻炼"));
        XSSFCell cellB3 = row3.createCell(1);
        modifyCell(cellB3, new CellOption().setStyle(row3Style).setValue("初始1RM"));
        XSSFCell cellC3 = row3.createCell(2);
        modifyCell(cellC3, new CellOption().setStyle(row3Style).setValue("本周期1RM"));
        XSSFCell cellD3 = row3.createCell(3);
        modifyCell(cellD3, new CellOption().setStyle(row3Style).setValue("本周期训练MAX"));
        XSSFCell cellE3 = row3.createCell(4);
        modifyCell(cellE3, new CellOption().setStyle(row3Style).setValue("增重停止次数"));

        String weightRollbackCell1 = "E4";
        String weightRollbackCell2 = "E5";
        String weightRollbackCell3 = "E6";
        String weightRollbackCell4 = "E7";
        String periodCalculateFormula1 = String.format("IF(%s-1-%s<0, 0, %s-1-%s)", periodCell, weightRollbackCell1, periodCell, weightRollbackCell1);
        String periodCalculateFormula2 = String.format("IF(%s-1-%s<0, 0, %s-1-%s)", periodCell, weightRollbackCell2, periodCell, weightRollbackCell2);
        String periodCalculateFormula3 = String.format("IF(%s-1-%s<0, 0, %s-1-%s)", periodCell, weightRollbackCell3, periodCell, weightRollbackCell3);
        String periodCalculateFormula4 = String.format("IF(%s-1-%s<0, 0, %s-1-%s)", periodCell, weightRollbackCell4, periodCell, weightRollbackCell4);

        XSSFRow row4 = sheet1.createRow(3);
        CellStyle row4Style1 = workbook.createCellStyle();
        row4Style1.cloneStyleFrom(row1Style1);
        row4Style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        CellStyle row4Style2 = workbook.createCellStyle();
        row4Style2.cloneStyleFrom(row1Style1);
        row4Style2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        row4Style2.setFont(boldFont);

        XSSFCell cellA4 = row4.createCell(0);
        modifyCell(cellA4, new CellOption().setStyle(row4Style1).setValue(Training.深蹲.name()));
        XSSFCell cellB4 = row4.createCell(1);
        modifyCell(cellB4, new CellOption().setStyle(row4Style2).setValue("BAR"));
        XSSFCell cellC4 = row4.createCell(2);
        modifyCell(cellC4, new CellOption().setStyle(row4Style1).setType(FORMULA).setValue(String.format("IF(%s<=1, IF(A2=\"kg\", IF(B4=\"BAR\", 20, IF(B4 < 20, 20, B4)), IF(B4=\"BAR\", 45, IF(B4 < 45, 45, B4))), IF(A2=\"kg\", IF(B4=\"BAR\", 20+%s*5, B4+%s*5), IF(B4=\"BAR\", 45+%s*10, B4+%s*10)))", periodCell, periodCalculateFormula1, periodCalculateFormula1, periodCalculateFormula1, periodCalculateFormula1)));
        XSSFCell cellD4 = row4.createCell(3);
        modifyCell(cellD4, new CellOption().setStyle(row4Style1).setType(FORMULA).setValue("IF(A2=\"kg\", IF(C4*0.9 < 20, 20, C4*0.9), IF(C4*0.9 < 45, 45, C4*0.9))"));
        XSSFCell cellE4 = row4.createCell(4);
        modifyCell(cellE4, new CellOption().setStyle(row4Style2).setType(NUMERIC).setValue("0"));

        XSSFRow row5 = sheet1.createRow(4);
        CellStyle row5Style1 = workbook.createCellStyle();
        row5Style1.cloneStyleFrom(row1Style1);
        row5Style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        CellStyle row5Style2 = workbook.createCellStyle();
        row5Style2.cloneStyleFrom(row1Style1);
        row5Style2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        row5Style2.setFont(boldFont);

        XSSFCell cellA5 = row5.createCell(0);
        modifyCell(cellA5, new CellOption().setStyle(row5Style1).setValue(Training.卧推.name()));
        XSSFCell cellB5 = row5.createCell(1);
        modifyCell(cellB5, new CellOption().setStyle(row5Style2).setValue("BAR"));
        XSSFCell cellC5 = row5.createCell(2);
        modifyCell(cellC5, new CellOption().setStyle(row5Style1).setType(FORMULA).setValue(String.format("IF(%s<=1, IF(A2=\"kg\", IF(B5=\"BAR\", 20, IF(B5 < 20, 20, B5)), IF(B5=\"BAR\", 45, IF(B5 < 45, 45, B5))), IF(A2=\"kg\", IF(B5=\"BAR\", 20+%s*2.5, B5+%s*2.5), IF(B5=\"BAR\", 45+%s*5, B5+%s*5)))", periodCell, periodCalculateFormula2, periodCalculateFormula2, periodCalculateFormula2, periodCalculateFormula2)));
        XSSFCell cellD5 = row5.createCell(3);
        modifyCell(cellD5, new CellOption().setStyle(row5Style1).setType(FORMULA).setValue("IF(A2=\"kg\", IF(C5*0.9 < 20, 20, C5*0.9), IF(C5*0.9 < 45, 45, C5*0.9))"));
        XSSFCell cellE5 = row5.createCell(4);
        modifyCell(cellE5, new CellOption().setStyle(row5Style2).setType(NUMERIC).setValue("0"));

        XSSFRow row6 = sheet1.createRow(5);
        CellStyle row6Style1 = workbook.createCellStyle();
        row6Style1.cloneStyleFrom(row1Style1);
        row6Style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        CellStyle row6Style2 = workbook.createCellStyle();
        row6Style2.cloneStyleFrom(row1Style1);
        row6Style2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        row6Style2.setFont(boldFont);

        XSSFCell cellA6 = row6.createCell(0);
        modifyCell(cellA6, new CellOption().setStyle(row6Style1).setValue(Training.硬拉.name()));
        XSSFCell cellB6 = row6.createCell(1);
        modifyCell(cellB6, new CellOption().setStyle(row6Style2).setValue("BAR"));
        XSSFCell cellC6 = row6.createCell(2);
        modifyCell(cellC6, new CellOption().setStyle(row6Style1).setType(FORMULA).setValue(String.format("IF(%s<=1, IF(A2=\"kg\", IF(B6=\"BAR\", 20, IF(B6 < 20, 20, B6)), IF(B6=\"BAR\", 45, IF(B6 < 45, 45, B6))), IF(A2=\"kg\", IF(B6=\"BAR\", 20+%s*5, B6+%s*5), IF(B6=\"BAR\", 45+%s*10, B6+%s*10)))", periodCell, periodCalculateFormula3, periodCalculateFormula3, periodCalculateFormula3, periodCalculateFormula3)));
        XSSFCell cellD6 = row6.createCell(3);
        modifyCell(cellD6, new CellOption().setStyle(row6Style1).setType(FORMULA).setValue("IF(A2=\"kg\", IF(C6*0.9 < 20, 20, C6*0.9), IF(C6*0.9 < 45, 45, C6*0.9))"));
        XSSFCell cellE6 = row6.createCell(4);
        modifyCell(cellE6, new CellOption().setStyle(row6Style2).setType(NUMERIC).setValue("0"));

        XSSFRow row7 = sheet1.createRow(6);
        CellStyle row7Style1 = workbook.createCellStyle();
        row7Style1.cloneStyleFrom(row1Style1);
        row7Style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        CellStyle row7Style2 = workbook.createCellStyle();
        row7Style2.cloneStyleFrom(row1Style1);
        row7Style2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        row7Style2.setFont(boldFont);

        XSSFCell cellA7 = row7.createCell(0);
        modifyCell(cellA7, new CellOption().setStyle(row7Style1).setValue(Training.直立杠铃推举.name()));
        XSSFCell cellB7 = row7.createCell(1);
        modifyCell(cellB7, new CellOption().setStyle(row7Style2).setValue("BAR"));
        XSSFCell cellC7 = row7.createCell(2);
        modifyCell(cellC7, new CellOption().setStyle(row7Style1).setType(FORMULA).setValue(String.format("IF(%s<=1, IF(A2=\"kg\", IF(B7=\"BAR\", 20, IF(B7 < 20, 20, B7)), IF(B7=\"BAR\", 45, IF(B7 < 45, 45, B7))), IF(A2=\"kg\", IF(B7=\"BAR\", 20+%s*2.5, B7+%s*2.5), IF(B7=\"BAR\", 45+%s*5, B7+%s*5)))", periodCell, periodCalculateFormula4, periodCalculateFormula4, periodCalculateFormula4, periodCalculateFormula4)));
        XSSFCell cellD7 = row7.createCell(3);
        modifyCell(cellD7, new CellOption().setStyle(row7Style1).setType(FORMULA).setValue("IF(A2=\"kg\", IF(C7*0.9 < 20, 20, C7*0.9), IF(C7*0.9 < 45, 45, C7*0.9))"));
        XSSFCell cellE7 = row7.createCell(4);
        modifyCell(cellE7, new CellOption().setStyle(row7Style2).setType(NUMERIC).setValue("0"));

        XSSFCell cellF3 = row3.createCell(5);
        CellStyle row3Style2 = workbook.createCellStyle();
        row3Style2.cloneStyleFrom(row1Style1);
        row3Style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        row3Style2.setWrapText(true);
        for (int i = 2; i <= 6; i++) {
            for (int j = 5; j <= 7; j++) {
                sheet1.getRow(i).createCell(j).setCellStyle(row3Style2);
            }
        }
        sheet1.addMergedRegion(new CellRangeAddress(2, 6, 5, 7));
        modifyCell(cellF3, new CellOption().setStyle(row3Style2).setValue("训练模式选择High Frequency时\n训练安排不会对日程造成影响\n二级训练不属于7/5/3原版内容，可自行取舍"));

        TRAINING_MAX_POSITION.put(Training.深蹲, "D4");
        TRAINING_MAX_POSITION.put(Training.卧推, "D5");
        TRAINING_MAX_POSITION.put(Training.硬拉, "D6");
        TRAINING_MAX_POSITION.put(Training.直立杠铃推举, "D7");

        buildSchedule(sheet1, 8, 2, startDateCell, periodCell, deloadSkipCell, thisCycleDeloadSkipCell, row1Style1);

        //生成体重记录表格
        XSSFSheet sheet2 = workbook.createSheet("体重记录");
        buildBodyWeightRecord(sheet2);

        for (Map.Entry<String, Map<String, Map<String, Object>>> sheetValidation : VALIDATIONS.entrySet()) {
            XSSFSheet sheet = workbook.getSheet(sheetValidation.getKey());
            for (Map.Entry<String, Map<String, Object>> validation : sheetValidation.getValue().entrySet()) {
                Map<String, Object> validationParam = validation.getValue();
                XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) validationParam.get("constraint");
                CellRangeAddressList rangeAddressList = (CellRangeAddressList) validationParam.get("regions");
                XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
                sheet.addValidationData(helper.createValidation(constraint, rangeAddressList));
            }
        }

        writeFile(workbook, filePath);
    }

    private static void buildSchedule(XSSFSheet sheet, final int startRow, final int startCol, String startDateCell, String periodCell, String deloadSkipCell, String thisCycleDeloadSkipCell, XSSFCellStyle style) {
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);
        boldFont.setFontHeight(14);
        XSSFFont baseFont = sheet.getWorkbook().createFont();
        baseFont.setFontHeight(14);
        XSSFCellStyle baseStyle = sheet.getWorkbook().createCellStyle();
        baseStyle.cloneStyleFrom(style);
        baseStyle.setFont(baseFont);
        XSSFCellStyle headStyle = sheet.getWorkbook().createCellStyle();
        headStyle.cloneStyleFrom(baseStyle);
        headStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headStyle.setFont(boldFont);
        XSSFCellStyle firstLevelStyle = sheet.getWorkbook().createCellStyle();
        firstLevelStyle.cloneStyleFrom(baseStyle);
        firstLevelStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        XSSFCellStyle secondLevelStyle = sheet.getWorkbook().createCellStyle();
        secondLevelStyle.cloneStyleFrom(baseStyle);
        secondLevelStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        XSSFCellStyle textStyle = sheet.getWorkbook().createCellStyle();
        textStyle.cloneStyleFrom(baseStyle);
        textStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        XSSFCellStyle varStyle = sheet.getWorkbook().createCellStyle();
        varStyle.cloneStyleFrom(baseStyle);
        varStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        XSSFCellStyle dateStyle = sheet.getWorkbook().createCellStyle();
        dateStyle.cloneStyleFrom(textStyle);
        dateStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("m/d/yy"));
        XSSFCellStyle dateVarStyle = sheet.getWorkbook().createCellStyle();
        dateVarStyle.cloneStyleFrom(dateStyle);
        dateVarStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        int startScheduleRow = startRow + 1;
        for (int i = 0; i < 5; i++) {
            int rowNum = startRow + i * 10;
            int days = i * 7;
            String formula;
            for (int j = 0; j < 2; j++) {
                XSSFRow row = sheet.createRow(rowNum + j);
                for (int k = startCol; k < startCol + 2; k++) {
                    row.createCell(k).setCellStyle(headStyle);
                }
                if (j == 0) {
                    for (int k = startCol + 2; k < startCol + 5; k++) {
                        row.createCell(k).setCellStyle(firstLevelStyle);
                    }
                    for (int k = startCol + 5; k < startCol + 8; k++) {
                        row.createCell(k).setCellStyle(secondLevelStyle);
                    }
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 1, startCol, startCol + 1));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, startCol + 2, startCol + 4));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, startCol + 5, startCol + 7));
            XSSFCell cellWeek = sheet.getRow(rowNum).getCell(startCol);
            XSSFCell cellFirstLevel = sheet.getRow(rowNum).getCell(startCol + 2);
            XSSFCell cellSecondLevel = sheet.getRow(rowNum).getCell(startCol + 5);
            String weekStr = "Week" + (i + 1);
            if (i == 4) {
                weekStr += " (Deload)";
            }
            modifyCell(cellWeek, new CellOption().setStyle(headStyle).setValue(weekStr));
            modifyCell(cellFirstLevel, new CellOption().setStyle(firstLevelStyle).setValue("一级主项"));
            modifyCell(cellSecondLevel, new CellOption().setStyle(secondLevelStyle).setValue("二级辅项"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 2), new CellOption().setStyle(baseStyle).setValue("锻炼"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 3), new CellOption().setStyle(baseStyle).setValue("重量"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 4), new CellOption().setStyle(baseStyle).setValue("组数×次数"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 5), new CellOption().setStyle(baseStyle).setValue("锻炼"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 6), new CellOption().setStyle(baseStyle).setValue("重量"));
            modifyCell(sheet.getRow(rowNum + 1).createCell(startCol + 7), new CellOption().setStyle(baseStyle).setValue("组数×次数"));

            String firstTrainingCell = String.format("%s%s", formatCellCol(startCol + 2), formatCellRow(startScheduleRow + 1));
            for (int j = 1; j <= 7; j++) {
                int curRowNum = rowNum + 1 + j;
                int curDays = days + j;
                XSSFRow row = sheet.createRow(curRowNum);
                XSSFCell cellDayT = row.createCell(startCol);
                formula = String.format("TEXT(WEEKDAY(%s),\"dddd\")", formatCell(curRowNum, startCol + 1));
                if (i < 4) {
                    modifyCell(cellDayT, new CellOption().setStyle(textStyle).setType(FORMULA)
                                                         .setValue(formula));
                } else {
                    formula = String.format("IF(OR(C2=\"Normal\", C2=\"Balance\"), IF(%s=\"%s\", \"SKIP\", %s), \"SKIP\")", thisCycleDeloadSkipCell, YorN.Y.name(), formula);
                    modifyCell(cellDayT, new CellOption().setStyle(textStyle).setType(FORMULA)
                                                         .setValue(formula));
                }

                XSSFCell cellDayV = row.createCell(startCol + 1);
                if (i == 0 && j == 1) {
                    modifyCell(cellDayV, new CellOption().setStyle(dateStyle).setType(FORMULA).setValue(String.format("IF(%s<=1, %s, IF(OR(C2=\"Normal\", C2=\"Balance\"), %s+IF(%s-%s<=0, (%s - 1) * 28, %s * 28 + (%s - %s - 1) * 35), %s+(%s - 1) * 28))", periodCell, startDateCell, startDateCell, periodCell, deloadSkipCell, periodCell, deloadSkipCell, periodCell, deloadSkipCell, startDateCell, periodCell)));
                } else {
                    formula = String.format("%s+%s", formatCell(startScheduleRow + 1, startCol + 1), 7 * i + j - 1);
                    if (i < 4) {
                        modifyCell(cellDayV, new CellOption().setStyle(dateStyle).setType(FORMULA)
                                                             .setValue(formula));
                    } else {
                        formula = String.format("IF(OR(C2=\"Normal\", C2=\"Balance\"), IF(%s=\"%s\", \"SKIP\", %s), \"SKIP\")", thisCycleDeloadSkipCell, YorN.Y.name(), formula);
                        modifyCell(cellDayV, new CellOption().setStyle(dateStyle).setType(FORMULA)
                                                             .setValue(formula));
                    }
                }

                XSSFCell cellTrain1 = row.createCell(startCol + 2);
                Training training1 = HIGH_FREQUENCY_SCHEDULE[((curDays - 1) + BENCH_BASE) % 6];
                String training1Str = training1 == null ? "REST" : training1.name();
                Training training2 = HIGH_FREQUENCY_SCHEDULE[((curDays - 1) + SQUAT_BASE) % 6];
                String training2Str = training2 == null ? "REST" : training2.name();

                if (i == 0) {
                    if (j == 1) {
                        modifyCell(cellTrain1, new CellOption().setStyle(varStyle).setEnumClass(TrainingStart.class).setValue(TrainingStart.卧推.name()));
                    } else {
                        String fTrainingStr1 = MODE_DAYS_SEQUENCE[0][j - 1] == null ? "REST" : TRAINING_SEQUENCE[0][MODE_DAYS_SEQUENCE[0][j - 1]].name();
                        String fTrainingStr2 = MODE_DAYS_SEQUENCE[0][j - 1] == null ? "REST" : TRAINING_SEQUENCE[1][MODE_DAYS_SEQUENCE[0][j - 1]].name();
                        String formula1 = firstWeekDayNormalFormula(firstTrainingCell, fTrainingStr1, fTrainingStr2);
                        fTrainingStr1 = MODE_DAYS_SEQUENCE[1][j - 1] == null ? "REST" : TRAINING_SEQUENCE[0][MODE_DAYS_SEQUENCE[1][j - 1]].name();
                        fTrainingStr2 = MODE_DAYS_SEQUENCE[1][j - 1] == null ? "REST" : TRAINING_SEQUENCE[1][MODE_DAYS_SEQUENCE[1][j - 1]].name();
                        String formula2 = firstWeekDayNormalFormula(firstTrainingCell, fTrainingStr1, fTrainingStr2);
                        fTrainingStr1 = MODE_DAYS_SEQUENCE[2][j - 1] == null ? "REST" : TRAINING_SEQUENCE[0][MODE_DAYS_SEQUENCE[2][j - 1]].name();
                        fTrainingStr2 = MODE_DAYS_SEQUENCE[2][j - 1] == null ? "REST" : TRAINING_SEQUENCE[1][MODE_DAYS_SEQUENCE[2][j - 1]].name();
                        String formula3 = firstWeekDayNormalFormula(firstTrainingCell, fTrainingStr1, fTrainingStr2);
                        String normalFormula = firstWeekNormalFormula(formula1, formula2, formula3);
                        String balanceFormula = firstWeekBalanceFormula(formula1, formula2, formula3);
                        formula = firstWeekTrainingFormula(normalFormula, balanceFormula, firstTrainingCell, training1Str, training2Str);
                        modifyCell(cellTrain1, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                    }
                } else if (i < 4) {
                    if (curDays <= 24) {
                        formula = String.format("IF(OR(C2=\"Normal\", C2=\"Balance\"), %s, IF(%s=\"%s\", \"%s\", \"%s\"))", formatCell(startScheduleRow + j, startCol + 2), firstTrainingCell, TrainingStart.卧推.name(), training1Str, training2Str);
                    } else {
                        int deloadDay = curDays - 24 - 1;
                        training1Str = TRAINING_SEQUENCE[0][deloadDay].name();
                        training2Str = TRAINING_SEQUENCE[1][deloadDay].name();
                        formula = String.format("IF(OR(C2=\"Normal\", C2=\"Balance\"), %s, IF(%s=\"%s\", \"%s\", \"%s\"))", formatCell(startScheduleRow + j, startCol + 2), firstTrainingCell, TrainingStart.卧推.name(), training1Str, training2Str);
                    }
                    modifyCell(cellTrain1, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                } else {
                    formula = String.format("IF(OR(C2=\"Normal\", C2=\"Balance\"), IF(%s=\"%s\", \"SKIP\", %s), \"SKIP\")", thisCycleDeloadSkipCell, YorN.Y.name(), formatCell(startScheduleRow + j, startCol + 2));
                    modifyCell(cellTrain1, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                }

                String training1Cell = String.format("%s%s", formatCellCol(startCol + 2), formatCellRow(curRowNum));
                String training1SetsAndReps = String.format("%s%s", formatCellCol(startCol + 4), formatCellRow(curRowNum));

                XSSFCell cellTrain1Weight = row.createCell(startCol + 3);
                formula = weightFormula(training1Cell, training1Cell, curDays, CycleCalculatorInstance.FIRST_TRAIN);
                modifyCell(cellTrain1Weight, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));

                XSSFCell cellTrain1SetsAndReps = row.createCell(startCol + 4);
                formula = setsAndRepsFormula(training1Cell, training1Cell, curDays, CycleCalculatorInstance.FIRST_TRAIN);
                modifyCell(cellTrain1SetsAndReps, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));


                XSSFCell cellTrain2 = row.createCell(startCol + 5);
                formula = secondSupportTrainFormula(training1Cell, training1SetsAndReps);
                modifyCell(cellTrain2, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));

                String training2Cell = String.format("%s%s", formatCellCol(startCol + 5), formatCellRow(curRowNum));

                XSSFCell cellTrain2Weight = row.createCell(startCol + 6);
                formula = weightFormula(training1Cell, training2Cell, curDays, CycleCalculatorInstance.SECOND_TRAIN);
                modifyCell(cellTrain2Weight, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));

                XSSFCell cellTrain2SetsAndReps = row.createCell(startCol + 7);
                formula = setsAndRepsFormula(training1Cell, training2Cell, curDays, CycleCalculatorInstance.SECOND_TRAIN);
                modifyCell(cellTrain2SetsAndReps, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
            }
        }
    }

    private static String secondSupportTrainFormula(String mainTrainingCell, String mainTrainingSetsAndReps) {
        return String.format("IF(%s=\"REST\", \"REST\", IF(%s=\"SKIP\", \"SKIP\", IF(%s=\"10×3\", \"SKIP\", IF(%s=\"%s\", \"%s\", IF(%s=\"%s\", \"%s\", IF(%s=\"%s\", \"%s\", \"%s\"))))))",
                             mainTrainingCell, mainTrainingCell, mainTrainingSetsAndReps,
                             mainTrainingCell, Training.卧推.name(), Training.直立杠铃推举.name(),
                             mainTrainingCell, Training.深蹲.name(), Training.硬拉.name(),
                             mainTrainingCell, Training.硬拉.name(), Training.深蹲.name(),
                             Training.卧推.name());
    }

    private static String firstWeekDayNormalFormula(String firstTrainingCell, String training1Str, String training2Str) {
        return String.format("IF(%s=\"%s\", \"%s\", \"%s\")", firstTrainingCell, Training.卧推.name(), training1Str, training2Str);
    }

    private static String firstWeekNormalFormula(String formula1, String formula2, String formula3) {
        return String.format("IF(B2=\"12x3x4x\", %s, IF(B2=\"12x34xx\", %s, %s))", formula1, formula2, formula3);
    }

    private static String firstWeekBalanceFormula(String formula1, String formula2, String formula3) {
        return String.format("IF(B2=\"12x3x4x\", %s, IF(B2=\"12x34xx\", %s, %s))", formula1, formula2, formula3);
    }

    private static String firstWeekTrainingFormula(String normalFormula, String balanceFormula, String firstTrainingCell, String training1Str, String training2Str) {
        return String.format("IF(C2=\"Normal\", %s, IF(C2=\"Balance\", %s, IF(%s=\"%s\", \"%s\", \"%s\")))", normalFormula, balanceFormula, firstTrainingCell, Training.卧推.name(), training1Str, training2Str);
    }

    private static String setsAndRepsFormula(String mainTrainingCell, String trainingCell, int days, CycleCalculator calculator) {
        days--;
        int normalLoop = days / 7;
        int highLoop = calHighLoop(days);
        return String.format("IF(%s=\"REST\", \"REST\", IF(%s=\"SKIP\", \"SKIP\", IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, %s)))))",
                             trainingCell, trainingCell,
                             mainTrainingCell, Training.卧推.name(), judgeSetsAndRepsFormula(calculator.getSets(0, normalLoop, Training.卧推), calculator.getReps(0, normalLoop, Training.卧推), calculator.getSets(1, normalLoop, Training.卧推), calculator.getReps(1, normalLoop, Training.卧推), calculator.getSets(1, highLoop, Training.卧推), calculator.getReps(1, highLoop, Training.卧推)),
                             mainTrainingCell, Training.深蹲.name(), judgeSetsAndRepsFormula(calculator.getSets(0, normalLoop, Training.深蹲), calculator.getReps(0, normalLoop, Training.深蹲), calculator.getSets(1, normalLoop, Training.深蹲), calculator.getReps(1, normalLoop, Training.深蹲), calculator.getSets(1, highLoop, Training.深蹲), calculator.getReps(1, highLoop, Training.深蹲)),
                             mainTrainingCell, Training.硬拉.name(), judgeSetsAndRepsFormula(calculator.getSets(0, normalLoop, Training.硬拉), calculator.getReps(0, normalLoop, Training.硬拉), calculator.getSets(1, normalLoop, Training.硬拉), calculator.getReps(1, normalLoop, Training.硬拉), calculator.getSets(1, highLoop, Training.硬拉), calculator.getReps(1, highLoop, Training.硬拉)),
                             judgeSetsAndRepsFormula(calculator.getSets(0, normalLoop, Training.直立杠铃推举), calculator.getReps(0, normalLoop, Training.直立杠铃推举), calculator.getSets(1, normalLoop, Training.直立杠铃推举), calculator.getReps(1, normalLoop, Training.直立杠铃推举), calculator.getSets(1, highLoop, Training.直立杠铃推举), calculator.getReps(1, highLoop, Training.直立杠铃推举)));
    }

    private static String weightFormula(String mainTrainingCell, String trainingCell, int days, CycleCalculator calculator) {
        days--;
        int normalLoop = days / 7;
        int highLoop = calHighLoop(days);
        return String.format("IF(%s=\"REST\", \"REST\", IF(%s=\"SKIP\", \"SKIP\", IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, %s)))))",
                             trainingCell, trainingCell,
                             mainTrainingCell, Training.卧推.name(), calBaseWeightFormula(weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(0, normalLoop, Training.卧推)), calculator.getWeight(0, normalLoop, Training.卧推)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, normalLoop, Training.卧推)), calculator.getWeight(1, normalLoop, Training.卧推)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, highLoop, Training.卧推)), calculator.getWeight(1, highLoop, Training.卧推))),
                             mainTrainingCell, Training.深蹲.name(), calBaseWeightFormula(weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(0, normalLoop, Training.深蹲)), calculator.getWeight(0, normalLoop, Training.深蹲)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, normalLoop, Training.深蹲)), calculator.getWeight(1, normalLoop, Training.深蹲)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, highLoop, Training.深蹲)), calculator.getWeight(1, highLoop, Training.深蹲))),
                             mainTrainingCell, Training.硬拉.name(), calBaseWeightFormula(weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(0, normalLoop, Training.硬拉)), calculator.getWeight(0, normalLoop, Training.硬拉)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, normalLoop, Training.硬拉)), calculator.getWeight(1, normalLoop, Training.硬拉)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, highLoop, Training.硬拉)), calculator.getWeight(1, highLoop, Training.硬拉))),
                             calBaseWeightFormula(weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(0, normalLoop, Training.直立杠铃推举)), calculator.getWeight(0, normalLoop, Training.直立杠铃推举)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, normalLoop, Training.直立杠铃推举)), calculator.getWeight(1, normalLoop, Training.直立杠铃推举)), weightFormula(TRAINING_MAX_POSITION.get(calculator.getTraining(1, highLoop, Training.直立杠铃推举)), calculator.getWeight(1, highLoop, Training.直立杠铃推举))));
    }

    private static int calHighLoop(int days) {
        int highLoop = days / 6;
        return highLoop > 4 ? 4 : highLoop;
    }

    private static String weightFormula(String position, String weightPercent) {
        return String.format("%s*%s", position, weightPercent);
    }

    private static String calBaseWeightFormula(String normalFormula, String balanceFormula, String highFormula) {
        String cal1 = String.format("IF(A2= \"kg\", IF(MROUND(%s, 2.5) < 20, 20, MROUND(%s, 2.5)), IF(MROUND(%s, 5) < 45, 45, MROUND(%s, 5)))", normalFormula, normalFormula, normalFormula, normalFormula);
        String cal2 = String.format("IF(A2= \"kg\", IF(MROUND(%s, 2.5) < 20, 20, MROUND(%s, 2.5)), IF(MROUND(%s, 5) < 45, 45, MROUND(%s, 5)))", balanceFormula, balanceFormula, balanceFormula, balanceFormula);
        String cal3 = String.format("IF(A2= \"kg\", IF(MROUND(%s, 2.5) < 20, 20, MROUND(%s, 2.5)), IF(MROUND(%s, 5) < 45, 45, MROUND(%s, 5)))", highFormula, highFormula, highFormula, highFormula);
        return String.format("IF(C2=\"Normal\", %s, IF(C2=\"Balance\", %s, %s))", cal1, cal2, cal3);
    }

    private static String judgeSetsAndRepsFormula(int normalSets, int normalReps, int balanceSets, int balanceReps, int highSets, int highReps) {
        String normalFormula = String.format("\"%s×%s\"", normalSets, normalReps);
        String balanceFormula = String.format("\"%s×%s\"", balanceSets, balanceReps);
        String highFormula = String.format("\"%s×%s\"", highSets, highReps);
        return String.format("IF(C2=\"Normal\", %s, IF(C2=\"Balance\", %s, %s))", normalFormula, balanceFormula, highFormula);
    }

    private static String formatCell(int row, int col) {
        return "X".replace('X', (char) ('A' + col)) + (row + 1);
    }

    private static String formatCellCol(int col) {
        return "X".replace('X', (char) ('A' + col));
    }

    private static String formatCellRow(int row) {
        return "" + (row + 1);
    }

    private static void modifyCell(XSSFCell cell, CellOption option) {
        cell.setCellType(option.getType());
        if (option.getType() == FORMULA) {
            cell.setCellFormula(option.getValue());
        } else {
            if (option.getValue() != null) {
                cell.setCellValue(option.getValue().replaceAll("_", " ").trim());
            }
        }
        if (option.getStyle() != null) {
            cell.setCellStyle(option.getStyle());
        }
        if (option.getEnumClass() != null) {
            setEnum(cell, option.getEnumClass());
        }
    }

    private static void setEnum(XSSFCell cell, Class<? extends Enum> enumClass) {
        XSSFSheet sheet = cell.getSheet();
        String enumName = enumClass.getName();
        Map<String, Map<String, Object>> sheetValidation = VALIDATIONS.computeIfAbsent(sheet.getSheetName(), new Function<String, Map<String, Map<String, Object>>>() {
            @Override
            public Map<String, Map<String, Object>> apply(String s) {
                return new HashMap<>();
            }
        });
        Map<String, Object> validationParam = sheetValidation.computeIfAbsent(enumName, new Function<String, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(String s) {
                return new HashMap<>();
            }
        });
        CellRangeAddressList rangeAddressList;
        if (validationParam.isEmpty()) {
            Enum[] enumValues = enumClass.getEnumConstants();
            String[] values = new String[enumValues.length];
            for (int i = 0; i < enumValues.length; i++) {
                values[i] = enumValues[i].name().replaceAll("_", " ").trim();
            }
            XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(values);
            rangeAddressList = new CellRangeAddressList();
            validationParam.put("constraint", constraint);
            validationParam.put("regions", rangeAddressList);
        } else {
            rangeAddressList = (CellRangeAddressList) validationParam.get("regions");
        }
        CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
        CellRangeAddress[] rangeAddresses = rangeAddressList.getCellRangeAddresses();
        boolean addCellRangeAddress = true;
        for (CellRangeAddress rangeAddress : rangeAddresses) {
            if (StringUtils.equals(rangeAddress.formatAsString(), cellRangeAddress.formatAsString())) {
                addCellRangeAddress = false;
                break;
            }
        }
        if (addCellRangeAddress) {
            rangeAddressList.addCellRangeAddress(cellRangeAddress);
        }
    }

    static class CellOption {
        private String    value;
        private CellStyle style;
        private CellType type = STRING;
        private Class<? extends Enum> enumClass;

        public String getValue() {
            return value;
        }

        public CellOption setValue(String value) {
            this.value = value;
            return this;
        }

        public CellStyle getStyle() {
            return style;
        }

        public CellOption setStyle(CellStyle style) {
            this.style = style;
            return this;
        }

        public CellType getType() {
            return type;
        }

        public CellOption setType(CellType type) {
            this.type = type;
            return this;
        }

        public Class<? extends Enum> getEnumClass() {
            return enumClass;
        }

        public CellOption setEnumClass(Class<? extends Enum> enumClass) {
            this.enumClass = enumClass;
            return this;
        }
    }

    static class TrainingParam {
        private final int             period;
        private final Training        training;
        private final int             sets;
        private final int             reps;
        private final String          weight;
        private       TrainingSupport trainingSupport;

        public TrainingParam(Training training, int sets, int reps, String weight, int period) {
            this.training = training;
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
            this.period = period;
        }

        public TrainingParam(Training training, int sets, int reps, String weight, int period, TrainingSupport trainingSupport) {
            this.training = training;
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
            this.period = period;
            this.trainingSupport = trainingSupport;
            this.trainingSupport.setMainParam(this);
        }

        public int getSets() {
            return sets;
        }

        public int getReps() {
            return reps;
        }

        public String getWeight() {
            return weight;
        }

        public int getPeriod() {
            return period;
        }

        public Training getTraining() {
            return training;
        }

        public TrainingSupport getTrainingSupport() {
            return trainingSupport;
        }

        public TrainingParam setTrainingSupport(TrainingSupport trainingSupport) {
            this.trainingSupport = trainingSupport;
            return this;
        }
    }

    static class TrainingSupport {
        private TrainingParam mainParam;
        private TrainingParam trainingParam = null;
        private final SupportCalculator calculator;

        interface SupportCalculator {
            TrainingParam calculate(TrainingParam mainParam);
        }

        public TrainingSupport(SupportCalculator calculator) {
            this.calculator = calculator;
        }

        public TrainingSupport(TrainingParam mainParam, SupportCalculator calculator) {
            this.mainParam = mainParam;
            this.calculator = calculator;
        }

        public TrainingSupport setMainParam(TrainingParam mainParam) {
            this.mainParam = mainParam;
            return this;
        }

        public TrainingParam getMainParam() {
            return mainParam;
        }

        public TrainingParam getTrainingParam() {
            if (this.trainingParam == null) {
                this.trainingParam = calculator.calculate(mainParam);
            }
            return trainingParam;
        }

        public SupportCalculator getCalculator() {
            return calculator;
        }
    }

    enum WeightUnit {
        kg,
        lbs;
    }

    enum TrainingMode {
        Normal,
        Balance,
        High_Frequency;
    }

    enum TrainingSchedule {
        _12x3x4x,
        _12x34xx,
        _1x2x34x;
    }

    enum TrainingStart {
        卧推,
        深蹲;
    }

    enum Training {
        卧推,
        深蹲,
        直立杠铃推举,
        硬拉;
    }

    enum YorN {
        Y,
        N
    }

    private static void writeFile(XSSFWorkbook workbook, String path) throws Exception {
        File file = new File(path);
        OutputStream os = new FileOutputStream(file);
        workbook.write(os);
    }

    private static void buildBodyWeightRecord(XSSFSheet sheet) {
        XSSFFont baseFont = sheet.getWorkbook().createFont();
        baseFont.setFontHeight(14);
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);
        boldFont.setFontHeight(14);
        XSSFFont valFont = sheet.getWorkbook().createFont();
        valFont.setBold(true);
        valFont.setColor(IndexedColors.DARK_RED.getIndex());
        valFont.setFontHeight(14);
        for (int i = 0; i < 20; i++) {
            sheet.setColumnWidth(i, 18 * 256);
        }
        XSSFCellStyle row1Style1 = sheet.getWorkbook().createCellStyle();
        row1Style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        row1Style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        row1Style1.setBorderLeft(BorderStyle.THIN);
        row1Style1.setBorderRight(BorderStyle.THIN);
        row1Style1.setBorderTop(BorderStyle.THIN);
        row1Style1.setBorderBottom(BorderStyle.THIN);
        row1Style1.setLeftBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setRightBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setTopBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setBottomBorderColor(IndexedColors.BLACK1.getIndex());
        row1Style1.setAlignment(HorizontalAlignment.CENTER);
        row1Style1.setVerticalAlignment(VerticalAlignment.CENTER);
        row1Style1.setFont(baseFont);

        XSSFCellStyle row1Style2 = sheet.getWorkbook().createCellStyle();
        row1Style2.cloneStyleFrom(row1Style1);
        row1Style2.setFont(boldFont);
        row1Style2.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        XSSFCellStyle dateBoldStyle = sheet.getWorkbook().createCellStyle();
        dateBoldStyle.cloneStyleFrom(row1Style2);
        dateBoldStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("m/d/yy"));

        XSSFRow row1 = sheet.createRow(0);
        XSSFCell cellA1 = row1.createCell(0);
        modifyCell(cellA1, new CellOption().setStyle(row1Style2).setEnumClass(WeightUnit.class).setValue(WeightUnit.kg.name()));
        XSSFCell cellB1 = row1.createCell(1);
        modifyCell(cellB1, new CellOption().setStyle(dateBoldStyle).setValue("2018/6/10"));
        XSSFCell cellC1 = row1.createCell(2);
        modifyCell(cellC1, new CellOption().setStyle(row1Style1).setType(FORMULA).setValue("CONCATENATE(\"体重(\",A1,\")\")"));
        XSSFCell cellD1 = row1.createCell(3);
        modifyCell(cellD1, new CellOption().setStyle(row1Style1).setValue("体脂率(%)"));
        XSSFCell cellE1 = row1.createCell(4);
        modifyCell(cellE1, new CellOption().setStyle(row1Style1).setType(FORMULA).setValue("CONCATENATE(\"瘦体重(\",A1,\")\")"));
        XSSFCell cellF1 = row1.createCell(5);
        modifyCell(cellF1, new CellOption().setStyle(row1Style1).setValue("目标热量"));
        XSSFCell cellG1 = row1.createCell(6);
        modifyCell(cellG1, new CellOption().setStyle(row1Style1).setValue("摄入热量"));
        XSSFCell cellH1 = row1.createCell(7);
        modifyCell(cellH1, new CellOption().setStyle(row1Style1).setType(FORMULA).setValue("CONCATENATE(\"平均体重(\",A1,\")\")"));
        XSSFCell cellI1 = row1.createCell(8);
        modifyCell(cellI1, new CellOption().setStyle(row1Style1).setValue("体重趋势(%)"));
        XSSFCell cellJ1 = row1.createCell(9);
        modifyCell(cellJ1, new CellOption().setStyle(row1Style1).setType(FORMULA).setValue("CONCATENATE(\"平均瘦体重(\",A1,\")\")"));
        XSSFCell cellK1 = row1.createCell(10);
        modifyCell(cellK1, new CellOption().setStyle(row1Style1).setValue("瘦体重趋势(%)"));
        XSSFCell cellL1 = row1.createCell(11);
        modifyCell(cellL1, new CellOption().setStyle(row1Style1).setValue("平均体脂(%)"));
        XSSFCell cellM1 = row1.createCell(12);
        modifyCell(cellM1, new CellOption().setStyle(row1Style1).setValue("体脂趋势(%)"));

        XSSFCellStyle plainStyle = sheet.getWorkbook().createCellStyle();
        plainStyle.cloneStyleFrom(row1Style1);
        plainStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        XSSFCellStyle inputStyle = sheet.getWorkbook().createCellStyle();
        inputStyle.cloneStyleFrom(plainStyle);
        inputStyle.setFont(boldFont);
        XSSFCellStyle valStyle = sheet.getWorkbook().createCellStyle();
        valStyle.cloneStyleFrom(plainStyle);
        valStyle.setFont(valFont);
        XSSFCellStyle dateStyle = sheet.getWorkbook().createCellStyle();
        dateStyle.cloneStyleFrom(plainStyle);
        dateStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("m/d/yy"));

        String formula;
        int dayCount = 0;
        for (int i = 0; i < 52; i++) {
            StringBuilder weightAppender = new StringBuilder();
            StringBuilder leanWeightAppender = new StringBuilder();
            for (int j = 0; j < 7; j++) {
                XSSFRow row = sheet.createRow(dayCount + 1);

                if (j > 0) {
                    weightAppender.append(",");
                    leanWeightAppender.append(",");
                }

                String weightCell = formatCell(1 + dayCount, 2);
                String fatCell = formatCell(1 + dayCount, 3);
                String leanWeightCell = formatCell(1 + dayCount, 4);

                weightAppender.append(weightCell);
                leanWeightAppender.append(leanWeightCell);

                XSSFCell cellA = row.createCell(0);
                formula = String.format("TEXT(WEEKDAY(%s),\"dddd\")", formatCell(1 + dayCount, 1));
                modifyCell(cellA, new CellOption().setStyle(plainStyle).setType(FORMULA).setValue(formula));
                XSSFCell cellB = row.createCell(1);
                formula = String.format("B1+%s", dayCount);
                modifyCell(cellB, new CellOption().setStyle(dateStyle).setType(FORMULA).setValue(formula));
                XSSFCell cellC = row.createCell(2);
                modifyCell(cellC, new CellOption().setStyle(inputStyle).setType(NUMERIC));
                XSSFCell cellD = row.createCell(3);
                modifyCell(cellD, new CellOption().setStyle(inputStyle).setType(NUMERIC));
                XSSFCell cellE = row.createCell(4);
                formula = String.format("IF(AND(ISNUMBER(%s),ISNUMBER(%s)), %s*(1-%s/100), \"-\")", weightCell, fatCell, weightCell, fatCell);
                modifyCell(cellE, new CellOption().setStyle(plainStyle).setType(FORMULA).setValue(formula));
                XSSFCell cellF = row.createCell(5);
                modifyCell(cellF, new CellOption().setStyle(inputStyle).setType(NUMERIC));
                XSSFCell cellG = row.createCell(6);
                modifyCell(cellG, new CellOption().setStyle(inputStyle).setType(NUMERIC));
                XSSFCell cellH = row.createCell(7);
                modifyCell(cellH, new CellOption().setStyle(valStyle).setType(STRING));
                XSSFCell cellI = row.createCell(8);
                modifyCell(cellI, new CellOption().setStyle(valStyle).setType(STRING));
                XSSFCell cellJ = row.createCell(9);
                modifyCell(cellJ, new CellOption().setStyle(valStyle).setType(STRING));
                XSSFCell cellK = row.createCell(10);
                modifyCell(cellK, new CellOption().setStyle(valStyle).setType(STRING));
                XSSFCell cellL = row.createCell(11);
                modifyCell(cellL, new CellOption().setStyle(valStyle).setType(STRING));
                XSSFCell cellM = row.createCell(12);
                modifyCell(cellM, new CellOption().setStyle(valStyle).setType(STRING));

                dayCount++;
            }
            int rowNum = 1 + i*7;
            String wcCell = formatCell(rowNum, 7);
            String wpCell = formatCell(rowNum - 7, 7);
            String lcCell = formatCell(rowNum, 9);
            String lpCell = formatCell(rowNum - 7, 9);
            String fcCell = formatCell(rowNum, 11);
            String fpCell = formatCell(rowNum - 7, 11);
            String aveWeightFormula = String.format("IF(ISNUMBER(AVERAGE(%s)), ROUND(AVERAGE(%s),2), \"-\")", weightAppender.toString(), weightAppender.toString());
            String aveLeanWeightFormula = String.format("IF(ISNUMBER(AVERAGE(%s)), ROUND(AVERAGE(%s),2), \"-\")", leanWeightAppender.toString(), leanWeightAppender.toString());
            String aveFatFormula = String.format("IF(ISNUMBER((%s-%s)/%s), ROUND((%s-%s)/%s*100,2), \"-\")", wcCell, lcCell, wcCell, wcCell, lcCell, wcCell);
            String weightTendencyFormula = i == 0 ? "\"-\"" : String.format("IF(ISNUMBER(ROUND((%s-%s)/%s,4)), CONCATENATE(IF(%s-%s>=0,\"+\",\"\"),ROUND((%s-%s)/%s,4)*100), \"-\")", wcCell, wpCell, wpCell, wcCell, wpCell, wcCell, wpCell, wpCell);
            String leanWeightTendencyFormula = i == 0 ? "\"-\"" : String.format("IF(ISNUMBER(ROUND((%s-%s)/%s,4)), CONCATENATE(IF(%s-%s>=0,\"+\",\"\"),ROUND((%s-%s)/%s,4)*100), \"-\")", lcCell, lpCell, lpCell, lcCell, lpCell, lcCell, lpCell, lpCell);
            String fatTendencyFormula = i == 0 ? "\"-\"" : String.format("IF(ISNUMBER(ROUND((%s-%s)/%s,4)), CONCATENATE(IF(%s-%s>=0,\"+\",\"\"),ROUND((%s-%s)/%s,4)*100), \"-\")", fcCell, fpCell, fpCell, fcCell, fpCell, fcCell, fpCell, fpCell);

            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 7, 7));
            formula = aveWeightFormula;
            modifyCell(sheet.getRow(rowNum).getCell(7), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 8, 8));
            formula = weightTendencyFormula;
            modifyCell(sheet.getRow(rowNum).getCell(8), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 9, 9));
            formula = aveLeanWeightFormula;
            modifyCell(sheet.getRow(rowNum).getCell(9), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 10, 10));
            formula = leanWeightTendencyFormula;
            modifyCell(sheet.getRow(rowNum).getCell(10), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 11, 11));
            formula = aveFatFormula;
            modifyCell(sheet.getRow(rowNum).getCell(11), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
            sheet.addMergedRegion(new CellRangeAddress(rowNum, 7 + i*7, 12, 12));
            formula = fatTendencyFormula;
            modifyCell(sheet.getRow(rowNum).getCell(12), new CellOption().setStyle(valStyle).setType(FORMULA).setValue(formula));
        }

    }

}

package com.tauros.kaleido.web.test;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    private static final Training[]                                    HIGH_FREQUENCY_SCHEDULE = new Training[]{Training.卧推, Training.硬拉, null, Training.直立杠铃推举, Training.深蹲, null};
    private static final Training[][]                                  TRAINING_SEQUENCE       = new Training[][]{{Training.卧推, Training.硬拉, Training.直立杠铃推举, Training.深蹲},
                                                                                                                  {Training.深蹲, Training.卧推, Training.硬拉, Training.直立杠铃推举}};
    private static final Integer[][]                                   MODE_DAYS_SEQUENCE      = new Integer[][]{{0, 1, null, 2, null, 3, null},
                                                                                                                 {0, 1, null, 2, 3, null, null},
                                                                                                                 {0, null, 1, null, 2, 3, null}};
    private static final List<Map<Training, TrainingParam>>            CYCLE                   = new ArrayList<Map<Training, TrainingParam>>() {{
        Map<Training, TrainingParam> loop1 = new HashMap<>();
        loop1.put(Training.直立杠铃推举, new TrainingParam(5, 7, "0.75"));
        loop1.put(Training.深蹲, new TrainingParam(10, 3, "0.85"));
        loop1.put(Training.卧推, new TrainingParam(7, 5, "0.80"));
        loop1.put(Training.硬拉, new TrainingParam(4, 9, "0.70"));
        Map<Training, TrainingParam> loop2 = new HashMap<>();
        loop2.put(Training.直立杠铃推举, new TrainingParam(4, 9, "0.70"));
        loop2.put(Training.深蹲, new TrainingParam(7, 5, "0.80"));
        loop2.put(Training.卧推, new TrainingParam(10, 3, "0.85"));
        loop2.put(Training.硬拉, new TrainingParam(5, 7, "0.75"));
        Map<Training, TrainingParam> loop3 = new HashMap<>();
        loop3.put(Training.直立杠铃推举, new TrainingParam(7, 5, "0.80"));
        loop3.put(Training.深蹲, new TrainingParam(4, 9, "0.70"));
        loop3.put(Training.卧推, new TrainingParam(5, 7, "0.75"));
        loop3.put(Training.硬拉, new TrainingParam(10, 3, "0.85"));
        Map<Training, TrainingParam> loop4 = new HashMap<>();
        loop4.put(Training.直立杠铃推举, new TrainingParam(10, 3, "0.85"));
        loop4.put(Training.深蹲, new TrainingParam(5, 7, "0.75"));
        loop4.put(Training.卧推, new TrainingParam(4, 9, "0.70"));
        loop4.put(Training.硬拉, new TrainingParam(7, 5, "0.80"));
        Map<Training, TrainingParam> loop5 = new HashMap<>();
        loop5.put(Training.直立杠铃推举, new TrainingParam(4, 8, "0.60"));
        loop5.put(Training.深蹲, new TrainingParam(4, 8, "0.60"));
        loop5.put(Training.卧推, new TrainingParam(4, 8, "0.60"));
        loop5.put(Training.硬拉, new TrainingParam(4, 8, "0.60"));
        add(loop1);
        add(loop2);
        add(loop3);
        add(loop4);
        add(loop5);
    }};

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

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);

        XSSFSheet sheet1 = workbook.createSheet("753 Program");
        for (int i = 0; i < 20; i++) {
            sheet1.setColumnWidth(i, 15 * 256);
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

        XSSFCell cellA1 = row1.createCell(0);
        modifyCell(cellA1, new CellOption().setStyle(row1Style1).setValue("单位"));
        XSSFCell cellB1 = row1.createCell(1);
        modifyCell(cellB1, new CellOption().setStyle(row1Style1).setValue("训练安排"));
        XSSFCell cellC1 = row1.createCell(2);
        modifyCell(cellC1, new CellOption().setStyle(row1Style1).setValue("训练频率"));
        XSSFCell cellD1 = row1.createCell(3);
        modifyCell(cellD1, new CellOption().setStyle(row1Style1).setValue("周期"));

        XSSFRow row2 = sheet1.createRow(1);
        CellStyle row2Style1 = workbook.createCellStyle();
        row2Style1.cloneStyleFrom(row1Style1);
        row2Style1.setFont(boldFont);
        row2Style1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        XSSFCell cellA2 = row2.createCell(0);
        modifyCell(cellA2, new CellOption().setStyle(row2Style1).setEnumClass(WeightUnit.class).setValue(WeightUnit.kg.name()));
        XSSFCell cellB2 = row2.createCell(1);
        modifyCell(cellB2, new CellOption().setStyle(row2Style1).setEnumClass(TrainingSchedule.class).setValue(TrainingSchedule._12x3x4x.name()));
        XSSFCell cellC2 = row2.createCell(2);
        modifyCell(cellC2, new CellOption().setStyle(row2Style1).setEnumClass(TrainingMode.class).setValue(TrainingMode.Normal.name()));
        XSSFCell cellD2 = row2.createCell(3);
        modifyCell(cellD2, new CellOption().setStyle(row2Style1).setType(NUMERIC).setValue("1"));

        XSSFRow row3 = sheet1.createRow(2);
        CellStyle row3Style = workbook.createCellStyle();
        row3Style.cloneStyleFrom(row1Style1);

        XSSFCell cellA3 = row3.createCell(0);
        modifyCell(cellA3, new CellOption().setStyle(row3Style).setValue("锻炼"));
        XSSFCell cellB3 = row3.createCell(1);
        modifyCell(cellB3, new CellOption().setStyle(row3Style).setValue("初始1RM"));
        XSSFCell cellC3 = row3.createCell(2);
        modifyCell(cellC3, new CellOption().setStyle(row3Style).setValue("初始训练MAX"));
        XSSFCell cellD3 = row3.createCell(3);
        modifyCell(cellD3, new CellOption().setStyle(row3Style).setValue("本周期训练MAX"));

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
        modifyCell(cellC4, new CellOption().setStyle(row4Style1).setType(FORMULA).setValue("IF($A$2=\"kg\", IF($B$4=\"BAR\" , 20, IF(MROUND(B4*0.9, 2.5) < 20, 20, MROUND(B4*0.9, 2.5))), IF($B$4=\"BAR\" , 45, IF(MROUND(B4*0.9, 5) < 45, 45, MROUND(B4*0.9, 5))))"));
        XSSFCell cellD4 = row4.createCell(3);
        modifyCell(cellD4, new CellOption().setStyle(row4Style1).setType(FORMULA).setValue("IF($D$2<=1, C4, IF($A$2=\"kg\", C4+MROUND((D2-1)*5, 5), C4+MROUND((D2-1)*10, 10)))"));

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
        modifyCell(cellC5, new CellOption().setStyle(row5Style1).setType(FORMULA).setValue("IF($A$2=\"kg\", IF($B$5=\"BAR\" , 20, IF(MROUND(B5*0.9, 2.5) < 20, 20, MROUND(B5*0.9, 2.5))), IF($B$5=\"BAR\" , 45, IF(MROUND(B5*0.9, 5) < 45, 45, MROUND(B5*0.9, 5))))"));
        XSSFCell cellD5 = row5.createCell(3);
        modifyCell(cellD5, new CellOption().setStyle(row5Style1).setType(FORMULA).setValue("IF($D$2<=1, C5, IF($A$2=\"kg\", C5+MROUND((D2-1)*2.5, 2.5), C5+MROUND((D2-1)*5, 5)))"));

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
        modifyCell(cellC6, new CellOption().setStyle(row6Style1).setType(FORMULA).setValue("IF($A$2=\"kg\", IF($B$6=\"BAR\" , 20, IF(MROUND(B6*0.9, 2.5) < 20, 20, MROUND(B6*0.9, 2.5))), IF($B$6=\"BAR\" , 45, IF(MROUND(B6*0.9, 5) < 45, 45, MROUND(B6*0.9, 5))))"));
        XSSFCell cellD6 = row6.createCell(3);
        modifyCell(cellD6, new CellOption().setStyle(row6Style1).setType(FORMULA).setValue("IF($D$2<=1, C6, IF($A$2=\"kg\", C6+MROUND((D2-1)*5, 5), C6+MROUND((D2-1)*10, 10)))"));

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
        modifyCell(cellC7, new CellOption().setStyle(row7Style1).setType(FORMULA).setValue("IF($A$2=\"kg\", IF($B$7=\"BAR\" , 20, IF(MROUND(B7*0.9, 2.5) < 20, 20, MROUND(B7*0.9, 2.5))), IF($B$7=\"BAR\" , 45, IF(MROUND(B7*0.9, 5) < 45, 45, MROUND(B7*0.9, 5))))"));
        XSSFCell cellD7 = row7.createCell(3);
        modifyCell(cellD7, new CellOption().setStyle(row7Style1).setType(FORMULA).setValue("IF($D$2<=1, C7, IF($A$2=\"kg\", C7+MROUND((D2-1)*2.5, 2.5), C7+MROUND((D2-1)*5, 5)))"));

        XSSFCell cellE1 = row1.createCell(4);
        CellStyle row1Style2 = workbook.createCellStyle();
        row1Style2.cloneStyleFrom(row1Style1);
        row1Style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        for (int i = 0; i <= 2; i++) {
            for (int j = 4; j <= 7; j++) {
                sheet1.getRow(i).createCell(j).setCellStyle(row1Style2);
            }
        }
        sheet1.addMergedRegion(new CellRangeAddress(0, 2, 4, 7));
        modifyCell(cellE1, new CellOption().setStyle(row1Style2).setValue("训练频率选择High Frequency时，训练安排不会对日程造成影响"));

        buildSchedule(sheet1, 8, 2, row1Style1);

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

    private static void buildSchedule(XSSFSheet sheet, final int startRow, final int startCol, XSSFCellStyle style) {
        XSSFFont boldFont = sheet.getWorkbook().createFont();
        boldFont.setBold(true);
        XSSFCellStyle baseStyle = sheet.getWorkbook().createCellStyle();
        baseStyle.cloneStyleFrom(style);
        XSSFCellStyle textStyle = sheet.getWorkbook().createCellStyle();
        textStyle.cloneStyleFrom(baseStyle);
        textStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        XSSFCellStyle varStyle = sheet.getWorkbook().createCellStyle();
        varStyle.cloneStyleFrom(baseStyle);
        varStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        varStyle.setFont(boldFont);
        XSSFCellStyle dateStyle = sheet.getWorkbook().createCellStyle();
        dateStyle.cloneStyleFrom(textStyle);
        dateStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("m/d/yy"));
        XSSFCellStyle dateVarStyle = sheet.getWorkbook().createCellStyle();
        dateVarStyle.cloneStyleFrom(dateStyle);
        dateVarStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());

        for (int i = 0; i < 5; i++) {
            int rowNum = startRow + i * 9;
            int days = i * 7;
            String formula;
            XSSFRow row1 = sheet.createRow(rowNum);
            for (int j = startCol; j < startCol + 2; j++) {
                row1.createCell(j).setCellStyle(baseStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, startCol, startCol + 1));
            XSSFCell cellWeek = row1.createCell(startCol);
            modifyCell(cellWeek, new CellOption().setStyle(baseStyle).setValue("Week" + (i + 1)));
            modifyCell(row1.createCell(startCol + 2), new CellOption().setStyle(baseStyle).setValue("锻炼"));
            modifyCell(row1.createCell(startCol + 3), new CellOption().setStyle(baseStyle).setValue("重量"));
            modifyCell(row1.createCell(startCol + 4), new CellOption().setStyle(baseStyle).setValue("组数×次数"));

            String firstTrainingCell = String.format("$%s$%s", formatCellCol(startCol + 2), formatCellRow(startRow + 1));
            for (int j = 1; j <= 7; j++) {
                int curRowNum = rowNum + j;
                int curDays = days + j;
                XSSFRow row = sheet.createRow(curRowNum);
                XSSFCell cellDayT = row.createCell(startCol);
                formula = String.format("TEXT(WEEKDAY(%s),\"dddd\")", formatCell(curRowNum, startCol + 1));
                if (i < 4) {
                    modifyCell(cellDayT, new CellOption().setStyle(textStyle).setType(FORMULA)
                                                         .setValue(formula));
                } else {
                    formula = String.format("IF($C$2=\"Normal\", %s, \"SKIP\")", formula);
                    modifyCell(cellDayT, new CellOption().setStyle(textStyle).setType(FORMULA)
                                                         .setValue(formula));
                }

                XSSFCell cellDayV = row.createCell(startCol + 1);
                if (i == 0 && j == 1) {
                    modifyCell(cellDayV, new CellOption().setStyle(dateVarStyle).setValue("2018/3/12"));
                } else {
                    formula = String.format("%s+%s", formatCell(startRow + 1, startCol + 1), 7 * i + j - 1);
                    if (i < 4) {
                        modifyCell(cellDayV, new CellOption().setStyle(dateStyle).setType(FORMULA)
                                                             .setValue(formula));
                    } else {
                        formula = String.format("IF($C$2=\"Normal\", %s, \"SKIP\")", formula);
                        modifyCell(cellDayV, new CellOption().setStyle(dateStyle).setType(FORMULA)
                                                             .setValue(formula));
                    }
                }

                XSSFCell cellTrain = row.createCell(startCol + 2);
                Training training1 = HIGH_FREQUENCY_SCHEDULE[((curDays - 1) + BENCH_BASE) % 6];
                String training1Str = training1 == null ? "REST" : training1.name();
                Training training2 = HIGH_FREQUENCY_SCHEDULE[((curDays - 1) + SQUAT_BASE) % 6];
                String training2Str = training2 == null ? "REST" : training2.name();

                if (i == 0) {
                    if (j == 1) {
                        modifyCell(cellTrain, new CellOption().setStyle(varStyle).setEnumClass(TrainingStart.class).setValue(TrainingStart.卧推.name()));
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
                        formula = firstWeekTrainingFormula(normalFormula, firstTrainingCell, training1Str, training2Str);
                        modifyCell(cellTrain, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                    }
                } else if (i < 4) {
                    if (curDays <= 24) {
                        formula = String.format("IF($C$2=\"Normal\", %s, IF(%s=\"%s\", \"%s\", \"%s\"))", formatCell(startRow + j, startCol + 2), firstTrainingCell, TrainingStart.卧推.name(), training1Str, training2Str);
                    } else {
                        int deloadDay = curDays - 24 - 1;
                        training1Str = TRAINING_SEQUENCE[0][deloadDay].name();
                        training2Str = TRAINING_SEQUENCE[1][deloadDay].name();
                        formula = String.format("IF($C$2=\"Normal\", %s, IF(%s=\"%s\", \"%s\", \"%s\"))", formatCell(startRow + j, startCol + 2), firstTrainingCell, TrainingStart.卧推.name(), training1Str, training2Str);
                    }
                    modifyCell(cellTrain, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                } else {
                    formula = String.format("IF($C$2=\"Normal\", %s, \"SKIP\")", formatCell(startRow + j, startCol + 2));
                    modifyCell(cellTrain, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
                }

                String trainingCell = String.format("$%s$%s", formatCellCol(startCol + 2), formatCellRow(curRowNum));

                XSSFCell cellTrain1Weight = row.createCell(startCol + 3);
                formula = weightFormula(trainingCell, curDays);
                modifyCell(cellTrain1Weight, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));

                XSSFCell cellTrain1SetsAndReps = row.createCell(startCol + 4);
                formula = setsAndRepsFormula(trainingCell, curDays);
                modifyCell(cellTrain1SetsAndReps, new CellOption().setStyle(textStyle).setType(FORMULA).setValue(formula));
            }
        }
    }

    private static String firstWeekDayNormalFormula(String firstTrainingCell, String training1Str, String training2Str) {
        return String.format("IF(%s=\"%s\", \"%s\", \"%s\")", firstTrainingCell, Training.卧推.name(), training1Str, training2Str);
    }

    private static String firstWeekNormalFormula(String formula1, String formula2, String formula3) {
        return String.format("IF($B$2=\"12x3x4x\", %s, IF($B$2=\"12x34xx\", %s, %s))", formula1, formula2, formula3);
    }

    private static String firstWeekTrainingFormula(String normalFormula, String firstTrainingCell, String training1Str, String training2Str) {
        return String.format("IF($C$2=\"Normal\", %s, IF(%s=\"%s\", \"%s\", \"%s\"))", normalFormula, firstTrainingCell, Training.卧推.name(), training1Str, training2Str);
    }

    private static String setsAndRepsFormula(String trainingCell, int days) {
        days--;
        int normalLoop = days / 7;
        int highLoop = days / 6;
        if (highLoop > 4) {
            highLoop = 4;
        }
        return String.format("IF(%s=\"REST\", \"REST\", IF(%s=\"SKIP\", \"SKIP\", IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, %s)))))",
                             trainingCell, trainingCell,
                             trainingCell, Training.卧推.name(), judgeSetsAndRepsFormula(getSets(normalLoop, Training.卧推), getReps(normalLoop, Training.卧推), getSets(highLoop, Training.卧推), getReps(highLoop, Training.卧推)),
                             trainingCell, Training.深蹲.name(), judgeSetsAndRepsFormula(getSets(normalLoop, Training.深蹲), getReps(normalLoop, Training.深蹲), getSets(highLoop, Training.深蹲), getReps(highLoop, Training.深蹲)),
                             trainingCell, Training.硬拉.name(), judgeSetsAndRepsFormula(getSets(normalLoop, Training.硬拉), getReps(normalLoop, Training.硬拉), getSets(highLoop, Training.硬拉), getReps(highLoop, Training.硬拉)),
                             judgeSetsAndRepsFormula(getSets(normalLoop, Training.直立杠铃推举), getReps(normalLoop, Training.直立杠铃推举), getSets(highLoop, Training.直立杠铃推举), getReps(highLoop, Training.直立杠铃推举)));
    }

    private static String weightFormula(String trainingCell, int days) {
        days--;
        int normalLoop = days / 7;
        int highLoop = days / 6;
        if (highLoop > 4) {
            highLoop = 4;
        }
        return String.format("IF(%s=\"REST\", \"REST\", IF(%s=\"SKIP\", \"SKIP\", IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, IF(%s=\"%s\", %s, %s)))))",
                             trainingCell, trainingCell,
                             trainingCell, Training.卧推.name(), calBaseWeightFormula(String.format("$D$5*%s", getWeight(normalLoop, Training.卧推)), String.format("$D$5*%s", getWeight(highLoop, Training.卧推))),
                             trainingCell, Training.深蹲.name(), calBaseWeightFormula(String.format("$D$4*%s", getWeight(normalLoop, Training.深蹲)), String.format("$D$4*%s", getWeight(highLoop, Training.深蹲))),
                             trainingCell, Training.硬拉.name(), calBaseWeightFormula(String.format("$D$6*%s", getWeight(normalLoop, Training.硬拉)), String.format("$D$6*%s", getWeight(highLoop, Training.硬拉))),
                             calBaseWeightFormula(String.format("$D$7*%s", getWeight(normalLoop, Training.直立杠铃推举)), String.format("$D$7*%s", getWeight(highLoop, Training.直立杠铃推举))));
    }

    private static String calBaseWeightFormula(String normalFormula, String highFormula) {
        String cal1 = String.format("IF($A$2= \"kg\", IF(MROUND(%s, 2.5) < 20, 20, MROUND(%s, 2.5)), IF(MROUND(%s, 5) < 45, 45, MROUND(%s, 5)))", normalFormula, normalFormula, normalFormula, normalFormula);
        String cal2 = String.format("IF($A$2= \"kg\", IF(MROUND(%s, 2.5) < 20, 20, MROUND(%s, 2.5)), IF(MROUND(%s, 5) < 45, 45, MROUND(%s, 5)))", highFormula, highFormula, highFormula, highFormula);
        return String.format("IF($C$2=\"Normal\", %s, %s)", cal1, cal2);
    }

    private static String judgeSetsAndRepsFormula(int normalSets, int normalReps, int highSets, int highReps) {
        String normalFormula = String.format("\"%s×%s\"", normalSets, normalReps);
        String highFormula = String.format("\"%s×%s\"", highSets, highReps);
        return String.format("IF($C$2=\"Normal\", %s, %s)", normalFormula, highFormula);
    }

    private static String getWeight(int loops, Training training) {
        return CYCLE.get(loops).get(training).getWeight();
    }

    private static int getSets(int loops, Training training) {
        return CYCLE.get(loops).get(training).getSets();
    }

    private static int getReps(int loops, Training training) {
        return CYCLE.get(loops).get(training).getReps();
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
            cell.setCellValue(option.getValue().replaceAll("_", " ").trim());
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
        private final int    sets;
        private final int    reps;
        private final String weight;

        public TrainingParam(int sets, int reps, String weight) {
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
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
    }

    enum WeightUnit {
        kg,
        lbs;
    }

    enum TrainingMode {
        Normal,
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

    private static void writeFile(XSSFWorkbook workbook, String path) throws Exception {
        File file = new File(path);
        OutputStream os = new FileOutputStream(file);
        workbook.write(os);
    }

}

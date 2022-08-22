package com.shwlong.qsn.util;

import com.shwlong.qsn.entity.vo.*;
import com.shwlong.qsn.exception.QsnFileException;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
public class FileInOut {

    /**
     * 将问卷结果导出到文件
     */
    public static XSSFWorkbook ExcelExport(List<SaveQsTitleVo> qsTitleVos, List<SaveToExcelVo> excelVos) throws IOException {
        // 1.创建excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2.创建工作表
        XSSFSheet sheet = workbook.createSheet();
        // 3.创建标题行
        XSSFRow titleRow = sheet.createRow(0);
        int i = 0;
        titleRow.createCell(i++).setCellValue("序号");
        titleRow.createCell(i++).setCellValue("提交时间");
        for(SaveQsTitleVo qsTitleVo : qsTitleVos) {
            titleRow.createCell(i++).setCellValue(qsTitleVo.getQsOrder() + "."
                    + qsTitleVo.getQsTitle() + "[" +
                    StringUtils.qsType2String(qsTitleVo.getQsType()) + "]");
        }

        // 4.创建数据行
        int j = 1, k;
        for (SaveToExcelVo excelVo : excelVos) {
            k = 0;
            XSSFRow dataRow = sheet.createRow(j++);
            dataRow.createCell(k++).setCellValue(excelVo.getIndex());
            dataRow.createCell(k++).setCellValue(DateTimeUtils.date2FormatStr(excelVo.getSubmitTime()));
            List<SaveAnswerVo> ans = excelVo.getAns();
            for(SaveAnswerVo answerVo : ans) {
                dataRow.createCell(k++).setCellValue(answerVo.getOptions());
            }
        }

        return workbook;
    }

    /**
     * 将问卷内容导出到word文档
     */
    public static XWPFDocument WordExport(AddPaperVo paperVo) throws IOException {
        // 1.创建word文档
        XWPFDocument document = new XWPFDocument();
        // 2.创建段落
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setText(paperVo.getTitle());
        run.addCarriageReturn();

        // 3.写入问卷信息
        List<AddQuestionVo> qsList = paperVo.getContent();
        for(AddQuestionVo qs : qsList) {
            run.setText(qs.getQs_order() + "." + qs.getQs_title() + "[" +
                    StringUtils.qsType2String(Integer.parseInt(qs.getQs_type())) + "]");
            run.addCarriageReturn();
            List<String> qsOption = qs.getQs_option();
            for(String opt : qsOption) {
                run.addTab();
                run.setText(opt);
                run.addCarriageReturn();
            }
            run.addCarriageReturn();
        }
        return document;
    }

    /**
     * 将问卷模板文件导入到问卷内容
     */
    public static AddPaperVo WordImport(FileInputStream in) throws QsnFileException, IOException {
        XWPFDocument document = new XWPFDocument(in);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String text = extractor.getText();
        String[] strList = text.split("\n");
        List<String> list = new ArrayList<>(Arrays.asList(strList));
        AddPaperVo paperVo = new AddPaperVo();

        // 设置标题
        paperVo.setTitle(list.get(0));
        list.remove(0);

        // 设置题目
        List<AddQuestionVo> qsList = new ArrayList<>();
        Iterator<String> iterator = list.listIterator();

        try {
            while (iterator.hasNext()) {
                AddQuestionVo qs = new AddQuestionVo();
                String qsTitle = iterator.next();
                String[] title = qsTitle.split("\\.", 2);
                qs.setQs_order(Integer.parseInt(title[0]));
                qs.setQs_number(Integer.parseInt(title[0]));
                qs.setQs_title(title[1].split("\\[")[0]);
                qs.setQs_type(StringUtils.string2QsType(qsTitle).toString());
                List<String> optList = new ArrayList<>();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    if ("".equals(s)) break;
                    optList.add(s.trim());
                }
                qs.setQs_option(optList);
                qsList.add(qs);
            }
            paperVo.setContent(qsList);
            paperVo.setQs_count(qsList.size());
        } catch (Exception e) {
            throw new QsnFileException(QsnEnum.FILE_UPLOAD_FORMAT_ERROR.getMsg());
        }
        return paperVo;
    }

}

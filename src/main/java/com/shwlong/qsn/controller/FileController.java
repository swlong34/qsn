package com.shwlong.qsn.controller;

import com.shwlong.qsn.entity.vo.AddPaperVo;
import com.shwlong.qsn.exception.QsnException;
import com.shwlong.qsn.exception.QsnFileException;
import com.shwlong.qsn.service.PaperService;
import com.shwlong.qsn.util.FileInOut;
import com.shwlong.qsn.util.QsnEnum;
import com.shwlong.qsn.util.R;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private PaperService paperService;

    @RequestMapping(value = "/excel/out/{id}", method = RequestMethod.GET)
    public void downloadExcel(@PathVariable("id") String paperId, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = paperService.downloadExcel(paperId);
        //String fileName = new Date().getTime() + "data.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
    }

    @RequestMapping(value = "/word/out/{id}", method = RequestMethod.GET)
    public void downloadWord(@PathVariable("id") String paperId, HttpServletResponse response) throws IOException {
        XWPFDocument document = paperService.downloadWord(paperId);
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        ServletOutputStream outputStream = response.getOutputStream();
        document.write(outputStream);
    }

    @PostMapping("/word/upload")
    public R mockPaper(@RequestParam("file") MultipartFile file) throws IOException, QsnFileException {

        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();

        assert fileName != null;
        if(!fileName.endsWith(".docx"))
            throw new QsnException(QsnEnum.FILE_FORMAT_ERROR.getMsg());

        if(fileSize > 100 * 1024)
            throw new QsnException(QsnEnum.FILE_SIZE_ERROR.getMsg());

        AddPaperVo paperVo = FileInOut.WordImport((FileInputStream) file.getInputStream());
        return R.ok(QsnEnum.FILE_UPLOAD_SUCCESS.getMsg()).put("data", paperVo);
    }

}

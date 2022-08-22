package com.shwlong.qsn;

import com.shwlong.qsn.dao.PaperMapper;
import com.shwlong.qsn.dao.QuestionMapper;
import com.shwlong.qsn.entity.PaperEntity;
import com.shwlong.qsn.entity.QuestionEntity;
import com.shwlong.qsn.entity.vo.AddPaperVo;
import com.shwlong.qsn.service.PaperService;
import com.shwlong.qsn.service.UserService;
import com.shwlong.qsn.util.Constant;
import com.shwlong.qsn.util.FileInOut;
import com.shwlong.qsn.util.JWTUtils;
import com.shwlong.qsn.util.QsnEnum;
import net.sf.json.JSONArray;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SpringBootTest
class QsnApplicationTests {

    @Autowired
    private FileInOut fileInOut;

    @Autowired
    private UserService userService;

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionMapper qsMapper;

    @Autowired
    private JWTUtils jwt;

    private String token;

    @Test
    void test_file_in() throws Exception {
        AddPaperVo paperVo = FileInOut.WordImport(new FileInputStream("./file/t3.docx"));
        System.out.println(paperVo);
    }

    @Test
    void test_file_out2() throws IOException {
        paperService.downloadWord("1519118324703723522");
    }

    @Test
    void test_file_out() throws IOException {
        paperService.downloadExcel("1519118324703723522");
    }

    @Test
    void contextLoads() {
        System.out.println(userService.getUsers());
    }

    @Test
    void test_lr() {
        System.out.println(userService.getUserByUserName("swlong"));
    }

    @Test
    void test_enum() {

        System.out.println(QsnEnum.REGISTER_USER_EXIST.getMsg());
    }

    @Test
    void test_jwt() throws InterruptedException {
        // 生成token
        token = jwt.generateToken(1, "swlong");
        System.out.println(token);
        System.out.println("================");
        // 解析token
        System.out.println(jwt.getClaimByToken(token).get("username"));
        System.out.println(jwt.getClaimByToken(token).get("userid"));
        System.out.println(jwt.getClaimByToken(token).getExpiration());

    }

    @Test
    void test_jwt_expire() {
        // 测试token 是否过期
        System.out.println(jwt.isTokenExpired(jwt.getClaimByToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InN3bG9uZyIsInN1YiI6InNod2xvbmcucXNuLmNvbSIsImV4cCI6MTY1MDg1ODgyMn0.BXvnclFkv1vnhagILWpQ286ue-biaE7JOvU9wcG3ZBg").getExpiration()));
        System.out.println(jwt.isTokenExpired(jwt.getClaimByToken("111.11.11").getExpiration()));
    }

    @Test
    void test_add_paper() {
        PaperEntity paper = new PaperEntity();
        paper.setCreateTime(new Date());
        paper.setPaperStatus(1);
        paper.setPaperTitle("这是一个测试问卷");
        paper.setPaperFooter("欢迎使用");
        paper.setUserId(1);
        paperMapper.insert(paper);
        System.out.println(paper.getId());
    }

    @Test
    void test_add_qs() {
        QuestionEntity qs = new QuestionEntity();
        qs.setPaperId("123");
        qs.setQsType(12);
        qs.setQsTitle("这是一个测试问题");
        qs.setQsOption("1;2;3");
        qsMapper.insert(qs);
    }

    @Test
    void test_list2String() {
        List<String> list = new ArrayList<>();
        list.add("信1801");
        list.add("信1802");
        list.add("信1803");
        //System.out.println(JSONArray.fromObject(list).toString());

        //System.out.println("====================");

        //String str = "[\"信1801\",\"信1802\",\"信1803\"]";
        Object[] objs = JSONArray.fromObject("[\"1\",\"2\",\"3\"]").toArray();
        for (Object obj : objs)
            System.out.println(obj.toString());
    }

    @Test
    void test_date_time() throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String s = formatter.format(LocalDateTime.parse("2022-04-26T04:55:27.000+00:00"));
        //System.out.println(s);

        //LocalDateTime date = LocalDateTime.parse("Sat Apr 30 2022 00:00:00 GMT+0800 (中国标准时间)", formatter);
        //System.out.println(LocalDateTime.now());
        //System.out.println(date);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateStr = format.format(new Date("Sat Apr 30 2022 12:25:03 GMT+0800 (中国标准时间)"));
//        System.out.println(dateStr);

//        System.out.println(LocalDateTime.parse(dateStr, formatter));
//        Date date = format.parse(dateStr);
//        System.out.println(date);
        //System.out.println(format.parse("2022-04-26 04:55:27"));
        //System.out.println(new Date());
        //System.out.println(new Date("2022-04-26 04:55:27"));
//        System.out.println(new Date("2022-04-27T17:55:03.000Z"));

        SimpleDateFormat sim1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s1 = "Fri Apr 29 15:00:00 CST 2022";
        try {
            Date date = sim1.parse(s1);
            System.out.println(date);
            System.out.println(sim2.format(date));
        }catch (ParseException e ){
            e.printStackTrace();
        }

    }

    @Test
    public void test_updatePaperById() {
        PaperEntity paper = paperMapper.selectById("1518815999195910145");
        paper.setStartTime(new Date());
        paper.setEndTime(new Date());
        paperMapper.updatePaperById(paper);
    }

    @Test
    public void test_updatePaperStatusById() {
        paperMapper.updatePaperStatusById("1518815999195910145", Constant.IS_PUBLISHED);
    }
}

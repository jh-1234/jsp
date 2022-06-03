package dao;

import static db.JdbcUtil.*;//jdbc 연결 안 하고 작성

import java.sql.*;
import java.util.ArrayList;
import javax.sql.DataSource;
import vo.CardBean;

public class CardDAO {

    DataSource ds;
    Connection con;
    private static CardDAO cardDAO;

    private CardDAO() {}

    public static CardDAO getInstance() {
        if(cardDAO == null) {
            cardDAO = new CardDAO();
        }
        return cardDAO;
    }

    public void setConnection(Connection con) {
        this.con = con;
    }

    public int selectListCount() {
        int listCount = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement("Select count(*) from bcard");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                listCount = rs.getInt(1);
             }
            } catch (Exception ex) {
            System.out.println("getListCount 에러 : " + ex);
            } finally {
            close(rs);
            close(pstmt);
        }
        return listCount;
    }

    /***** 명함 목록보기 *****/
    public ArrayList<CardBean> selectCardList(int page, int limit){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String card_list_sql="select * from bcard order by pos_num, emp_num asc limit ?, 10";
        ArrayList<CardBean> cardList = new ArrayList<CardBean>();
        CardBean card = null;
        int startRow = (page -1) * 10;

        try {
            pstmt = con.prepareStatement(card_list_sql);
            pstmt.setInt(1, startRow);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                card = new CardBean();
                card.setEMP_NUM(rs.getInt("EMP_NUM"));
                card.setNAME_KOR(rs.getString("NAME_KOR"));
                card.setSOC_NUM(rs.getInt("SOC_NUM"));
                card.setDEP_NUM(rs.getInt("DEP_NUM"));
                card.setPOS_NUM(rs.getInt("POS_NUM"));
                card.setMOBILE(rs.getInt("MOBILE"));
                card.setPHONE(rs.getInt("PHONE"));
                card.setFAX(rs.getInt("FAX"));
                card.setEMAIL(rs.getString("EMAIL"));
                card.setD_ENTRY(rs.getDate("D_ENTRY"));
                card.setD_RESIGN(rs.getDate("D_RESIGN"));
                cardList.add(card);
            }
        } catch(Exception e) {
            System.out.println("getCardList 에러 : " + e);
        } finally {
            close(rs);
            close(pstmt);
        } return cardList;
    }

    /***** 명함 삽입 *****/
    public int insertCardList(CardBean list) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";
        int insertCount = 0;

        //패스워드 포함 13개의 db 콜론 전부 입력(초기 입력일 경우, 패스워드와 퇴사일 입력은 고려해볼 내용!!)
        try {
            sql = "insert into bcard"
                    + "(emp_num," + "emp_passwd," + "soc_num," + "name_kor," + "name_eng," + "dep_num," + "pos_num,"
                    + "mobile," + "phone," + "fax,"	+ "email," + "d_entry," + "d_resign"
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, list.getEMP_NUM());
            pstmt.setString(2, list.getEMP_PASSWD());
            pstmt.setInt(3, list.getSOC_NUM());
            pstmt.setString(4, list.getNAME_KOR());
            pstmt.setString(5, list.getNAME_ENG());
            pstmt.setInt(6, list.getDEP_NUM());
            pstmt.setInt(7, list.getPOS_NUM());
            pstmt.setInt(8, list.getMOBILE());
            pstmt.setInt(9, list.getPHONE());
            pstmt.setInt(10, list.getFAX());
            pstmt.setString(11, list.getEMAIL());
            pstmt.setInt(12, 0);
            pstmt.setInt(13, 0);

            //insert 실행하고 성공한 횟수를 insertCount에 반환
            insertCount = pstmt.executeUpdate();
        } catch(Exception e) {
            System.out.println("insertCardList 에러 : e");
        } finally {
            close(rs);
            close(pstmt);
        } return insertCount;
    }

}

<%-- test_list.jsp の完全修正版 - インラインスタイル --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点一覧</c:param>
  <c:param name="content">
    <section class="me-4">

      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">得点一覧（科目）</h2>

      <!-- ======== 検索フォーム ======== -->
      <form method="get">
        <!-- 1 段目 -->
        <div class="row border mx-3 py-2 rounded mb-2 align-items-center">
          <!-- 入学年度 -->
          <div class="col-3">
            <label class="form-label">入学年度</label>
            <select name="f1" class="form-select">
              <option value="">--</option>
              <c:forEach var="y" items="${ent_year_set}">
                <option value="${y}" <c:if test="${conditions['entYear']==y.toString()}">selected</c:if>>${y}</option>
              </c:forEach>
            </select>
          </div>

          <!-- クラス -->
          <div class="col-3">
            <label class="form-label">クラス</label>
            <select name="f2" class="form-select">
              <option value="">--</option>
              <c:forEach var="c" items="${class_num_set}">
                <option value="${c}" <c:if test="${conditions['classNum']==c}">selected</c:if>>${c}</option>
              </c:forEach>
            </select>
          </div>

          <!-- 科目 -->
          <div class="col-3">
            <label class="form-label">科目</label>
            <select name="f3" class="form-select">
              <option value="">--</option>
              <c:forEach var="s" items="${subjects}">
                <option value="${s.cd}" <c:if test="${conditions['subjectCd']==s.cd}">selected</c:if>>${s.name}</option>
              </c:forEach>
            </select>
          </div>

          <!-- ボタン -->
          <div class="col-3 text-center">
            <label class="form-label d-block">&nbsp;</label>
            <button class="btn btn-secondary">絞込</button>
          </div>
        </div>

        <!-- 2 段目（学生情報）-->
        <div class="row border mx-3 py-2 rounded align-items-center">
          <div class="col-9">
            <label class="form-label">学生番号 / 氏名</label>
            <input type="text" name="studentInfo" class="form-control"
                   value="${conditions['studentInfo']}">
          </div>
          <div class="col-3 text-center">
            <label class="form-label d-block">&nbsp;</label>
            <button class="btn btn-primary">検索</button>
          </div>
        </div>
      </form>
      <!-- ======== /検索フォーム ======== -->

      <!-- ======== 検索結果 ======== -->
      <c:choose>
        <c:when test="${tests.size()>0}">
          <div class="mt-2">検索結果: ${tests.size()} 件</div>
          <div class="table-responsive">
            <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; border: 1px solid #e0e0e0;">
              <tr>
                <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">入学年度</th>
                <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">クラス</th>
                <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">学生番号</th>
                <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">氏名</th>
                <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">点数</th>
              </tr>
              <c:forEach var="t" items="${tests}">
                <tr>
                  <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${t.student.entYear}</td>
                  <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${t.classNum}</td>
                  <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${t.student.no}</td>
                  <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${empty t.student.name ? '－' : t.student.name}</td>
                  <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${t.point}</td>
                </tr>
              </c:forEach>
            </table>
          </div>
        </c:when>
        <c:otherwise>
          <div class="text-muted mt-2">該当するデータはありません。</div>
        </c:otherwise>
      </c:choose>
      <!-- ======== /検索結果 ======== -->

    </section>
  </c:param>
</c:import>
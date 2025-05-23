<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

      <!-- 登録完了メッセージ -->
      <c:if test="${registrationSuccess}">
        <div class="alert alert-success mx-3">
          登録が完了しました
        </div>
      </c:if>

      <!-- エラーメッセージ -->
      <c:if test="${not empty error}">
        <div class="alert alert-danger mx-3">
          ${error}
        </div>
      </c:if>

      <!-- 検索フォーム -->
      <form action="TestRegist.action" method="post">
        <div class="border mx-4 mb-3 py-3 px-4 rounded" style="width: calc(100% - 2rem);">
          <table style="width: 100%;">
            <tr>
              <td style="width: 15%;"><label class="form-label">入学年度</label></td>
              <td style="width: 15%;"><label class="form-label">クラス</label></td>
              <td style="width: 25%;"><label class="form-label">科目</label></td>
              <td style="width: 15%;"><label class="form-label">回数</label></td>
              <td style="width: 30%;"></td>
            </tr>
            <tr>
              <td>
                <select class="form-select" name="ent_year" style="width: 90%;">
                  <option value="">-------</option>
                  <c:forEach var="year" items="${entYearList}">
                    <option value="${year}" <c:if test="${selectedEntYear == year}">selected</c:if>>${year}</option>
                  </c:forEach>
                </select>
              </td>
              <td>
                <select class="form-select" name="class_num" style="width: 90%;">
                  <option value="">-------</option>
                  <c:forEach var="cls" items="${classNumList}">
                    <option value="${cls}" <c:if test="${selectedClassNum == cls}">selected</c:if>>${cls}</option>
                  </c:forEach>
                </select>
              </td>
              <td>
                <select class="form-select" name="subject_cd" style="width: 90%;">
                  <option value="">-------</option>
                  <c:forEach var="subject" items="${subjectList}">
                    <option value="${subject.cd}" <c:if test="${selectedSubjectCd == subject.cd}">selected</c:if>>${subject.name}</option>
                  </c:forEach>
                </select>
              </td>
              <td>
                <select class="form-select" name="no" style="width: 90%;">
                  <option value="">-------</option>
                  <c:forEach var="num" items="${noList}">
                    <option value="${num}" <c:if test="${selectedNo == num}">selected</c:if>>${num}</option>
                  </c:forEach>
                </select>
              </td>
              <td>
                <button type="submit" class="btn btn-secondary">検索</button>
              </td>
            </tr>
          </table>
        </div>
      </form>

      <!-- 検索結果・成績入力 -->
      <c:if test="${not empty studentList}">
        <!-- 科目情報表示 -->
        <div class="mx-4 mb-3">
          <strong>科目：${selectedSubjectName}（${selectedNo}回）</strong>
        </div>

        <form action="TestRegistExecute.action" method="post">
          <!-- 検索条件を保持 -->
          <input type="hidden" name="ent_year" value="${selectedEntYear}">
          <input type="hidden" name="class_num" value="${selectedClassNum}">
          <input type="hidden" name="subject_cd" value="${selectedSubjectCd}">
          <input type="hidden" name="no" value="${selectedNo}">

          <table class="table table-hover mx-4">
            <thead>
              <tr>
                <th>入学年度</th>
                <th>クラス</th>
                <th>学生番号</th>
                <th>氏名</th>
                <th>点数</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="student" items="${studentList}" varStatus="status">
                <tr>
                  <td>${selectedEntYear}</td>
                  <td>${selectedClassNum}</td>
                  <td>${student.no}</td>
                  <td>${student.name}</td>
                  <td>
                    <input type="hidden" name="student_no" value="${student.no}">
                    <input type="number"
                           class="form-control d-inline-block"
                           name="point"
                           style="width: 100px;"
                           value="${existingPoints[student.no]}"
                           required>
                    <c:if test="${not empty pointErrors[student.no]}">
                      <br><span style="color: orange; font-size: 12px;">
                        <i class="fas fa-exclamation-circle"></i>
                        ${pointErrors[student.no]}
                      </span>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>

          <div class="mx-4 mt-3">
            <button type="submit" class="btn btn-primary">登録して終了</button>
          </div>
        </form>
      </c:if>

      <div class="mt-3 mx-4">

      </div>
    </section>
  </c:param>
</c:import>